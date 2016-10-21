package ua.com.hedgehogsoft.oculus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.hedgehogsoft.oculus.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
