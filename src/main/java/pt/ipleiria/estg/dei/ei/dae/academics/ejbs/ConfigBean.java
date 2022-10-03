package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private StudentBean studentBean;

    @EJB
    private CourseBean courseBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        var courseCode = courseBean.create("EI");
        studentBean.create("foo", "bar", "foo", "foo@bar.com", courseCode);
    }

}
