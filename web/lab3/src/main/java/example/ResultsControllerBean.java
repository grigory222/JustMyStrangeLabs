package example;

import example.db.DAOFactory;
import example.entity.ResultEntity;
import example.util.Calculator;
import example.util.ValidationException;
import example.util.Validator;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Managed bean for handling results in JSF application.
 * This bean is responsible for managing operations related to result entities.
 */
@Data
@Slf4j
public class ResultsControllerBean implements Serializable {
    private XBean xBean;
    private YBean yBean;
    private RBean rBean;


    private ArrayList<ResultEntity> results = new ArrayList<>();

    @PostConstruct
    public void init() {
        var resultsEntities = DAOFactory.getInstance().getResultDAO().getAllResults();
        results = new ArrayList<>(resultsEntities);
        log.info("Results initialized with {} entries.", results.size());
    }

    public void addResult(Double x, Integer y, Integer r) {
        try {
            Validator.getValidator().validateParams(x, y, r);
        } catch (ValidationException e) {
            log.info(e.getMessage());
            return;
        }


        boolean result = Calculator.getCalculator().calculate(x, y, r);
        ResultEntity entity = ResultEntity.builder().x(x).y(y).r(r).result(result).build();
        results.add(entity);
        DAOFactory.getInstance().getResultDAO().addNewResult(entity);
        log.info("Added new result to the db: X={}, Y={}, R={}", x, y, r);
    }
}

