package repository;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.EntityManagerFactoryProvider;
import webserver.RequestHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final EntityManagerFactory emf = EntityManagerFactoryProvider.getInstance();

    public void save(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            tx.rollback();
        } finally {
            em.close();
        }
    }

    public User findByUserId(String userId) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT user FROM User user WHERE user.userId = ?1";
        return em.createQuery(jpql, User.class).setParameter(1, userId).getSingleResult();
    }

    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT user FROM User user";
        return em.createQuery(jpql, User.class).getResultList();
    }
}
