package web.backend.lab4.services;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import web.backend.lab4.auth.JwtProvider;
import web.backend.lab4.db.ResultDAO;
import web.backend.lab4.db.UserDAO;
import web.backend.lab4.dto.ErrorDTO;
import web.backend.lab4.dto.PointDTO;
import web.backend.lab4.dto.ResultDTO;
import web.backend.lab4.entity.ResultEntity;
import web.backend.lab4.entity.UserEntity;
import web.backend.lab4.util.Calculator;

import java.util.Optional;

@Slf4j
@Path("/point")
public class PointService {
    @EJB
    private UserDAO userDAO;

    @EJB
    private ResultDAO resultDAO;

    @Inject
    private JwtProvider jwtProvider;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPoint(PointDTO pointDTO, @CookieParam("access_token") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO.of("Access token is missing"))
                    .build();
        }

        try {
            String username = jwtProvider.getUsernameFromToken(accessToken);
            Long userId = jwtProvider.getUserIdFromToken(accessToken);
            Optional<UserEntity> userOptional = userDAO.getUserByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                var x = user.getPoints();
                boolean result = Calculator.getCalculator().calculate(pointDTO.getX(), pointDTO.getY(), pointDTO.getR());

                ResultEntity entity = ResultEntity.builder().x(1.0).y(1).r(1).user(user).result(result).build();
                try {
                    resultDAO.addNewResult(entity);
                } catch (Exception e) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
                }
                return Response.status(Response.Status.OK).entity(ResultDTO.of(entity.isResult())).build();
            } else{
                return Response.status(Response.Status.BAD_REQUEST).entity(ErrorDTO.of("User not found")).build();
            }


        } catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorDTO.of("Invalid or expired access token"))
                    .build();
        }

    }
}
