package ua.com.hedgehogsoft.oculus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.hedgehogsoft.oculus.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
   @Query("SELECT o FROM Order o WHERE o.isArchive = ?1")
   List<Order> findArchived(boolean find);
}
