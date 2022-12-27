package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AdministratorBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(String username, String password, String name, String email) {
        var admin = new Administrator(username, hasher.hash(password), name, email);
        em.persist(admin);
    }

    public Administrator find(String username) {
        return em.find(Administrator.class, username);
    }

    public Administrator findOrFail(String username) {
        var admin = em.getReference(Administrator.class, username);
        Hibernate.initialize(admin);

        return admin;
    }
}
