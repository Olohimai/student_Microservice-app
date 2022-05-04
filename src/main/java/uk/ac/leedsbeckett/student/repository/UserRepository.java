package uk.ac.leedsbeckett.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.leedsbeckett.student.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserName(String username);
    User findUserByEmail(String email);
}
