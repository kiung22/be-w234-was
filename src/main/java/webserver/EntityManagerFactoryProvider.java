package webserver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProvider {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("was");

    private EntityManagerFactoryProvider() {}

    public static EntityManagerFactory getInstance() {
        return emf;
    }
}
