package web.backend.lab4.dao;

import web.backend.lab4.entity.UserEntity;

import java.util.Optional;


public interface UserDAO {


    void addNewUser(UserEntity user);

    Optional<UserEntity> getUserById(Long userId);

    Optional<UserEntity> getUserByUsername(String userName);
}
