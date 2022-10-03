package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CourseBean {

    @PersistenceContext
    private EntityManager em;

    public Long create(String name) {
        var course = new Course(name);
        em.persist(course);

        return course.getCode();
    }

    public List<Course> getAll() {
        return em.createNamedQuery("getAllCourses", Course.class).getResultList();
    }
}
