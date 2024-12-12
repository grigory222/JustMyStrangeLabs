package web.backend.lab4.dao;

import web.backend.lab4.entity.ResultEntity;
import web.backend.lab4.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface ResultDAO {

    void addNewResult(ResultEntity result);

    List<ResultEntity> getResultsByUserId(Long userId);

    Optional<UserEntity> findUserByUserId(Long userId);
}
