package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public void create(Long code, String name, String courseYear, String scholarYear, Long courseCode) {
        var course = courseBean.find(courseCode);
        var subject = new Subject(code, name, courseYear, scholarYear, course);

        em.persist(subject);
        course.addSubject(subject);
    }

    public List<Subject> getAllSubjects() {
        return em.createNamedQuery("getAllSubjects", Subject.class).getResultList();
    }
}
