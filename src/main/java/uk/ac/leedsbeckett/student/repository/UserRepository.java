package uk.ac.leedsbeckett.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.leedsbeckett.student.model.User;

/**
 * Inherit database interaction functionality from JpaRepository for User class, of ID type Long
 * Create new User *
 * Update existing User *
 * Delete User *
 * Find User (one, all, or search by simple or complex properties)
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserName(String username);
    User findUserByEmail(String email);
}
