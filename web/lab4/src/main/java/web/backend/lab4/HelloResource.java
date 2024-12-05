package web.backend.lab4;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import web.backend.lab4.db.ResultDAO;
import web.backend.lab4.db.ResultDAOImpl;
import web.backend.lab4.entity.ResultEntity;
import web.backend.lab4.util.Calculator;


@Path("/hello-world")
public class HelloResource {
    @Inject
    private ResultDAO resultDAO;

    @GET
    @Produces("text/plain")
    public String hello() {
        boolean result = Calculator.getCalculator().calculate(1.0, 1, 1);
        ResultEntity entity = ResultEntity.builder().x(1.0).y(1).r(1).result(result).build();
        resultDAO.addNewResult(entity);
        return "Hello, SE!";
    }
}