package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SubjectBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CourseBean courseBean;

    public Subject find(Long code) {
        return em.find(Subject.class, code);
    }

    public Subject findOrFail(Long code) {
        var subject = em.getReference(Subject.class, code);
        Hibernate.initialize(subject);

        return subject;
    }

    public void create(Long code, String name, String courseYear, String scholarYear, Long courseCode) {
        var course = courseBean.findOrFail(courseCode);
        var subject = new Subject(code, name, courseYear, scholarYear, course);

        em.persist(subject);
        course.addSubject(subject);
    }

    public List<Subject> all(int offset, int limit) {
        return em.createNamedQuery("getAllSubjects", Subject.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long count() {
        return em.createQuery("SELECT COUNT(*) FROM " + Subject.class.getSimpleName(), Long.class).getSingleResult();
    }
}
