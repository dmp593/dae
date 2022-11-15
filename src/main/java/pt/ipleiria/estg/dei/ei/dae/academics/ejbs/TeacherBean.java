package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class TeacherBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubjectBean subjectBean;

    @Inject
    private Hasher hasher;

    public void create(String username, String password, String name, String email, String office) {
        var teacher = new Teacher(username, hasher.hash(password), name, email, office);
        em.persist(teacher);
    }

    public Teacher find(String username) {
        return em.find(Teacher.class, username);
    }

    public Teacher findOrFail(String username) {
        var teacher = em.getReference(Teacher.class, username);
        Hibernate.initialize(teacher);

        return teacher;
    }

    public void teach(String teacherUsername, Long subjectCode) {
        var teacher = findOrFail(teacherUsername);
        var subject = subjectBean.findOrFail(subjectCode);

        teacher.addSubject(subject);
        subject.addTeacher(teacher);
    }

    public void unteach(String teacherUsername, Long subjectCode) {
        var teacher = findOrFail(teacherUsername);
        var subject = subjectBean.findOrFail(subjectCode);

        subject.removeTeacher(teacher);
        teacher.removeSubject(subject);
    }
}
