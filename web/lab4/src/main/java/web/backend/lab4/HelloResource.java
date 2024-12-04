package web.backend.lab4;

import web.backend.lab4.db.DAOFactory;
import web.backend.lab4.entity.ResultEntity;
import web.backend.lab4.util.Calculator;


@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        boolean result = Calculator.getCalculator().calculate(1.0, 1, 1);
        ResultEntity entity = ResultEntity.builder().x(1.0).y(1).r(1).result(result).build();
        //results.add(entity);
        DAOFactory.getInstance().getResultDAO().addNewResult(entity);
        return "Hello, SE!";
    }
}