package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import javax.validation.constraints.NotNull;

public class StudentCreateDTO extends StudentDTO {

    @NotNull
    private String password;

    public StudentCreateDTO() {}

    public StudentCreateDTO(String username, String password, String name, String email, Long courseCode, String courseName) {
        super(username, name, email, courseCode, courseName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
