package uk.ac.leedsbeckett.student.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Enrolment {
    @EmbeddedId
    private EnrolmentKey id = new EnrolmentKey();

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name="student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name="course_id")
    private Course course;

    public Enrolment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Enrolment() {

    }
}
