package ua.com.hedgehogsoft.oculus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.hedgehogsoft.oculus.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
