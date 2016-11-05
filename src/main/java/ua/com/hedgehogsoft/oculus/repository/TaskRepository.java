package ua.com.hedgehogsoft.oculus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.hedgehogsoft.oculus.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>
{
}
