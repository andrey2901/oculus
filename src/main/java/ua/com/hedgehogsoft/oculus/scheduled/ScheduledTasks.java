package ua.com.hedgehogsoft.oculus.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ua.com.hedgehogsoft.oculus.model.Constructor;
import ua.com.hedgehogsoft.oculus.model.Order;
import ua.com.hedgehogsoft.oculus.model.Task;
import ua.com.hedgehogsoft.oculus.print.FilePrinter;
import ua.com.hedgehogsoft.oculus.repository.OrderRepository;
import ua.com.hedgehogsoft.oculus.repository.TaskRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Component
public class ScheduledTasks
{
   @Autowired
   private TaskRepository taskRepository;

   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private FilePrinter printer;

   @Scheduled(fixedRate = 14400000)
   public void reportCurrentTime()
   {
      List<Task> tasks = taskRepository.findAll();
      Task task = tasks.get(0);
      LocalDate today = LocalDate.now();
      LocalDate nextExecution = task.getNextExecution();
      LocalDate lastExecuted = task.getLastExecuted();
      if (today.isEqual(nextExecution) || today.isAfter(nextExecution))
      {
         LocalDate startSearch = lastExecuted.minusMonths(1).with(lastDayOfMonth());
         LocalDate endSearch = today.with(firstDayOfMonth());
         Map<Month, List<Order>> orders = orderRepository.findArchived(true).stream()
               .filter(order -> order.getActualDate().isAfter(startSearch) && order.getActualDate().isBefore(endSearch))
               .collect(groupingBy(order -> order.getActualDate().getMonth()));

         orders.forEach((month, _orders) ->
         {
            Map<Constructor, List<Order>> constructorsWithOrders = _orders.stream()
                  .sorted(comparing(order -> order.getConstructor().getName())).sorted(comparing(Order::getPlannedDate))
                  .collect(groupingBy(Order::getConstructor));
            printer.print(constructorsWithOrders);
         });
         orders.forEach((month, _orders) ->
         {
            for (Order ord : _orders)
            {
               orderRepository.delete(ord.getId());
            }
         });
         nextExecution = today.plusMonths(1).withDayOfMonth(5);
         lastExecuted = today;
         task.setNextExecution(nextExecution);
         task.setLastExecuted(lastExecuted);
         taskRepository.save(task);
      }
   }
}
