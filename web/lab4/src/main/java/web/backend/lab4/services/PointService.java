package web.backend.lab4.services;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import web.backend.lab4.auth.JwtProvider;
import web.backend.lab4.dao.ResultDAO;
import web.backend.lab4.dao.UserDAO;
import web.backend.lab4.dto.ErrorDTO;
import web.backend.lab4.dto.PointDTO;
import web.backend.lab4.dto.ResultDTO;
import web.backend.lab4.entity.ResultEntity;
import web.backend.lab4.entity.UserEntity;
import web.backend.lab4.util.Calculator;

import java.util.List;
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

        String username = jwtProvider.getUsernameFromToken(accessToken);
        Optional<UserEntity> userOptional = userDAO.getUserByUsername(username);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            boolean result = Calculator.getCalculator().calculate(pointDTO.getX(), pointDTO.getY(), pointDTO.getR());
            ResultEntity entity =
                    ResultEntity.builder()
                            .x(pointDTO.getX())
                            .y(pointDTO.getY())
                            .r(pointDTO.getR())
                            .user(user)
                            .result(result)
                            .build();
            try {
                resultDAO.addNewResult(entity);
                return Response.status(Response.Status.OK).entity(ResultDTO.of(entity.isResult())).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(ErrorDTO.of("User not found")).build();
        }

    }

    @Path("/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoints(@CookieParam("access_token") String accessToken) {
        Long userId = jwtProvider.getUserIdFromToken(accessToken);
        List<PointDTO> results = resultDAO
                        .getResultsByUserId(userId)
                        .stream()
                        .map(PointDTO::fromEntity)
                        .toList();
        return Response.ok(results).build();
    }
}
