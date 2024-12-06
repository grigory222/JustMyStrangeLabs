package web.backend.lab4.db;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.backend.lab4.entity.UserEntity;

import java.util.Optional;

@Stateless
public class UserDAOImpl implements UserDAO{
    @PersistenceContext(name="idk")
    private EntityManager entityManager;

    @Override
    public void addNewUser(UserEntity user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public Optional<UserEntity> getUserById(Long userId) {
        UserEntity user = entityManager.find(UserEntity.class, userId);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        TypedQuery<UserEntity> query = entityManager
                .createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

}
