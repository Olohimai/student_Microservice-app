package uk.ac.leedsbeckett.student.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PORTAL_USER")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String userName;
    private Role role;
    @Column(nullable = false, unique = true, length = 45)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "portal_user_student",
            joinColumns =
                    { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "student_id", referencedColumnName = "id") })
    @ToString.Exclude
    private Student student;

    public User() {
    }

    public User(String userName, Role role, String email, String password) {
        this.userName = userName;
        this.role = role;
        this.email = email;
        this.password = password;
    }
}