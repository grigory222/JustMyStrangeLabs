package web.backend.lab4;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import web.backend.lab4.db.ResultDAO;
import web.backend.lab4.db.UserDAO;
import web.backend.lab4.entity.ResultEntity;
import web.backend.lab4.entity.UserEntity;
import web.backend.lab4.util.Calculator;


@Path("/hello-world")
public class HelloResource {
    @EJB
    private ResultDAO resultDAO;
    @EJB
    private UserDAO userDAO;

    @GET
    @Produces("text/plain")
    public Response hello() {
//        boolean result = Calculator.getCalculator().calculate(1.0, 1, 1);
//
////        UserEntity user = UserEntity.builder().password("secret").username("nickname").build();
////        userDAO.addNewUser(user);
////        ResultEntity entity = ResultEntity.builder().x(1.0).y(1).r(1).user(user).result(result).build();
////        resultDAO.addNewResult(entity);
        return Response.ok().entity("hello world").build();
    }
}