package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends User {

    @NotNull
    private String office;

    @ManyToMany(mappedBy = "teachers", fetch = FetchType.EAGER)
    private List<Subject> subjects;

    public Teacher() {
        this.subjects = new ArrayList<>();
    }

    public Teacher(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
        this.office = office;
        this.subjects = new ArrayList<>();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        if (! this.subjects.contains(subject)) {
            this.subjects.add(subject);
        }
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
    }
}
