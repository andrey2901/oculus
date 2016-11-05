package ua.com.hedgehogsoft.oculus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import ua.com.hedgehogsoft.oculus.model.User;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.name = ?1")
    User findByName(String name);
}
