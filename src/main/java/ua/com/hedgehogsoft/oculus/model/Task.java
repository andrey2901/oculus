package ua.com.hedgehogsoft.oculus.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "last_executed", nullable = false)
    private LocalDate lastExecuted;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "next_execution", nullable = false)
    private LocalDate nextExecution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(LocalDate lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public LocalDate getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(LocalDate nextExecution) {
        this.nextExecution = nextExecution;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", lastExecuted=" + lastExecuted + ", nextExecution=" + nextExecution + "]";
    }
}
