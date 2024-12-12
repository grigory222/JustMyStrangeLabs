package web.backend.lab4.services;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import lombok.extern.slf4j.Slf4j;
import web.backend.lab4.auth.JwtProvider;
import web.backend.lab4.auth.PasswordHasher;
import web.backend.lab4.db.UserDAO;
import web.backend.lab4.dto.ErrorDTO;
import web.backend.lab4.dto.UserDTO;
import web.backend.lab4.entity.UserEntity;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Slf4j
@Path("/auth")
public class AuthService {
    @EJB
    private UserDAO userDAO;

    @Inject
    private JwtProvider jwtProvider;

    @POST
    @Path("/signup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(@Valid UserDTO userDTO) {
        if (userDAO.getUserByUsername(userDTO.getUsername()).isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO.of("User with this name already exists"))
                    .build();
        }

        UserEntity user = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(PasswordHasher.hashPassword(userDTO.getPassword().toCharArray()))
                .build();

        userDAO.addNewUser(user);
        log.info("Successfully added user: {}", user);

        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(@Valid UserDTO userDTO) {
        Optional<UserEntity> userOptional = userDAO.getUserByUsername(userDTO.getUsername());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (PasswordHasher.checkPassword(userDTO.getPassword().toCharArray(), user.getPassword())) {

                String refreshToken = jwtProvider.generateRefreshToken(user.getUsername(), user.getId());
                String accessToken = jwtProvider.generateAccessToken(user.getUsername(), user.getId());

                NewCookie refreshTokenCookie = new NewCookie.Builder("refresh_token")
                        .value(refreshToken)
                        .maxAge(JwtProvider.REFRESH_TOKEN_EXPIRATION)
                        .path("/")
                        .httpOnly(true)
                        .build();

                NewCookie accessTokenCookie = new NewCookie.Builder("access_token")
                        .value(accessToken)
                        .maxAge(JwtProvider.ACCESS_TOKEN_EXPIRATION)
                        .path("/")
                        .httpOnly(true)
                        .build();

                return Response.ok()
                        .cookie(accessTokenCookie, refreshTokenCookie)
                        .build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ErrorDTO.of("Wrong username or password"))
                .build();
    }



    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(@CookieParam("refresh_token") String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO.of("Refresh token is missing"))
                    .build();
        }

        try {
            // Проверяем валидность refresh_token
            String username = jwtProvider.getUsernameFromToken(refreshToken);
            Long userId = jwtProvider.getUserIdFromToken(refreshToken);

            // Генерируем новый access_token
            String newAccessToken = jwtProvider.generateAccessToken(username, userId);

            // Создаем cookie для access_token
            NewCookie accessTokenCookie = new NewCookie.Builder("access_token")
                    .value(newAccessToken)
                    .maxAge(JwtProvider.ACCESS_TOKEN_EXPIRATION)
                    .path("/")
                    .httpOnly(true)
                    .build();

            // Возвращаем успешный ответ без тела, но с cookie
            return Response.ok()
                    .cookie(accessTokenCookie)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorDTO.of("Invalid or expired refresh token"))
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout() {
        // Установка "пустых" токенов с истекшим сроком
        NewCookie accessTokenCookie = new NewCookie.Builder("access_token")
                .value("")
                .maxAge(0) // Удалить cookie
                .path("/")
                .httpOnly(true)
                .build();

        NewCookie refreshTokenCookie = new NewCookie.Builder("refresh_token")
                .value("")
                .maxAge(0) // Удалить cookie
                .path("/")
                .httpOnly(true)
                .build();

        // Ответ для клиента
        return Response.ok()
                .cookie(accessTokenCookie, refreshTokenCookie)
                .entity("{\"message\": \"Logged out successfully\"}")
                .build();
    }


}
