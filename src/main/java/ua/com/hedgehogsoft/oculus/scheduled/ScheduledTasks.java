package ua.com.hedgehogsoft.oculus.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.com.hedgehogsoft.oculus.model.Order;
import ua.com.hedgehogsoft.oculus.model.Task;
import ua.com.hedgehogsoft.oculus.repository.OrderRepository;
import ua.com.hedgehogsoft.oculus.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = 14400000)
    public void reportCurrentTime() {
        List<Task> tasks = taskRepository.findAll();
        Task task = tasks.get(0);
        LocalDate today = LocalDate.now();
        if (today.isEqual(task.getNextExecution()) || today.isAfter(task.getNextExecution())) {
            List<Order> orders = orderRepository.findAll();
        }
    }
}
