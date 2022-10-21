package pt.ipleiria.estg.dei.ei.dae.academics.entities.pivots;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subjects_students")
@IdClass(SubjectStudent.SubjectStudentId.class)
public class SubjectStudent {
    public class SubjectStudentId implements Serializable {
        @Column(name = "subject_code")
        private Long subjectCode;

        @Column(name = "student_username")
        private String studentUsername;

        public SubjectStudentId() {}

        public SubjectStudentId(Long subjectCode, String studentUsername) {
            this.subjectCode = subjectCode;
            this.studentUsername = studentUsername;
        }
    }

    @Id
    private Long subjectCode;

    @Id
    private String studentUsername;

    public SubjectStudent() {
    }

    public Long getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(Long subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }
}
