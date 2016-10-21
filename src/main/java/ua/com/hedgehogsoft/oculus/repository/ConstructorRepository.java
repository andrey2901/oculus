package ua.com.hedgehogsoft.oculus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.hedgehogsoft.oculus.model.Constructor;

public interface ConstructorRepository extends JpaRepository<Constructor, Long> {
    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT с FROM Constructor с WHERE с.name = ?1")
    Constructor findByName(String name);
}
