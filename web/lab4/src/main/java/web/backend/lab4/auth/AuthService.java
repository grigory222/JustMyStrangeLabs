package web.backend.lab4.auth;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import web.backend.lab4.db.UserDAO;
import web.backend.lab4.dto.ErrorDTO;
import web.backend.lab4.dto.UserDTO;
import web.backend.lab4.entity.UserEntity;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response signup(@Valid UserDTO userDTO){
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
//        String accessToken = jwtProvider.generateAccessToken(user.getUsername(), user.getId());
//        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername(), user.getId());
//
        return Response.ok().build();
    }

//    @POST
//    @Path("/login")
//    public Response login(){
//
//    }
//
//    @POST
//    @Path("/logout")
//    public Response signupUser(){
//
//    }
//
//    @POST
//    @Path("/refresh")
//    public Response refreshToken(){
//
//    }
}
