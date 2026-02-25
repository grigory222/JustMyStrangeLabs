package org.itmo.controller;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pool-status")
public class ConnectionPoolStatusController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public Map<String, Object> getStatus() {
        Map<String, Object> result = new HashMap<>();
        
        if (dataSource instanceof BasicDataSource) {
            BasicDataSource dbcp = (BasicDataSource) dataSource;
            result.put("type", "Apache Commons DBCP2");
            result.put("active", dbcp.getNumActive());
            result.put("idle", dbcp.getNumIdle());
            result.put("maxTotal", dbcp.getMaxTotal());
            result.put("maxIdle", dbcp.getMaxIdle());
            result.put("minIdle", dbcp.getMinIdle());
        } else {
            result.put("type", dataSource.getClass().getName());
            result.put("warning", "NOT DBCP2!");
        }
        
        return result;
    }
}
