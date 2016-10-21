package ua.com.hedgehogsoft.oculus.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.com.hedgehogsoft.oculus.model.Order;
import ua.com.hedgehogsoft.oculus.model.Task;
import ua.com.hedgehogsoft.oculus.repository.OrderRepository;
import ua.com.hedgehogsoft.oculus.repository.TaskRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.MONTHS;

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
        LocalDate nextExecution = task.getNextExecution();
        LocalDate lastExecuted = task.getLastExecuted();
        if (today.isEqual(nextExecution) || today.isAfter(nextExecution)) {
            List<Order> orders = orderRepository.findArchived(true);
            long months = MONTHS.between(lastExecuted, today);
            switch(Long.toString(months)){
                case "0":
                case "1":
                    System.out.println("!!!Print one report");
                    break;
                case "2":
                    System.out.println("!!!Print two report");
                    break;
                default:
                    System.out.println("!!!Print default report");
                    break;
            }
            orders.forEach(o -> {
                System.out.print("!!!" + o.getOrderNumber());
                System.out.print(":");
                System.out.println(o.isArchive());
            });
        }
    }
}
