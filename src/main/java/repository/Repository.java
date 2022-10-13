package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.EntityManagerFactoryProvider;
import webserver.RequestHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class Repository<T, I> {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    protected final EntityManagerFactory emf = EntityManagerFactoryProvider.getInstance();
    private final Class<T> entityType = getGenericTypeClass();

    public void save(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            tx.rollback();
            throw new RuntimeException("Failed to save entity. " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public T findById(I id) {
        EntityManager em = emf.createEntityManager();
        return em.find(entityType, id);
    }

    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT e FROM " + entityType.getName() + " e";
        return em.createQuery(jpql, entityType).getResultList();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return (Class<T>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("generic type을 명시해 주세요.");
        }
    }
}
