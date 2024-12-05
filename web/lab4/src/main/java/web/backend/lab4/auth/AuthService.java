package web.backend.lab4.auth;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import web.backend.lab4.db.UserDAO;
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

    private static final AuthValidator validator = new AuthValidator();


    @POST
    @Path("/signup")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response signup(UserEntity user){
        if (userDAO.getUserByUsername(user.getUsername()).isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User with this name already exists")
                    .build();
        }

        // validate name & password
        if (!validator.validateName(user.getUsername())){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username must be between 4 and 16 characters")
                    .build();
        }
        if (!validator.validatePassword(user.getPassword())){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Password must be between 4 and 16 characters")
                    .build();
        }

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
