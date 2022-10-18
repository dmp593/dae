package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private StudentBean studentBean;

    @EJB
    private CourseBean courseBean;

    @EJB
    private SubjectBean subjectBean;

    private Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        courseBean.create(9119L, "EI");
        courseBean.create(9281L, "EE");
        courseBean.create(9823L, "EM");
        courseBean.create(9822L, "EA");
        courseBean.create(9738L, "ET");

        subjectBean.create(1L, "Desenvolvimento de Aplicações Empresariais", "2017/18", "2021/22", 9119L);

        try {
            studentBean.create("foo", "bar", "foo", "foo@bar.com", 9119L);
        } catch (MyConstraintViolationException e) {
            logger.severe(e.getMessage());
        }

        studentBean.enroll("foo", 1L);
    }

}
