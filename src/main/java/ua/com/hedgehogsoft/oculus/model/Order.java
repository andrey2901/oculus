package ua.com.hedgehogsoft.oculus.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Order() {
        id = new Long(-1);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "plan_date", nullable = false)
    private LocalDate plannedDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "cipher")
    private String cipher;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "constructor_id", nullable = false)
    private Constructor constructor;

    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
        updateArchive();
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
        updateArchive();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public boolean isInTime() {
        if ((actualDate != null & plannedDate != null) && isArchive())
            return plannedDate.compareTo(actualDate) >= 0;
        else
            return false;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderNumber=" + orderNumber + ", plannedDate=" + plannedDate + ", actualDate="
                + actualDate + ", cipher=" + cipher + ", productName=" + productName + ", isArchive=" + isArchive
                + ", constructor" + constructor + "]";
    }

    private void updateArchive() {
        if (actualDate != null && plannedDate != null && cipher != null && cipher.length() > 0) {
            setArchive(true);
        }
    }

    public String getFormattedPlannedDate() {
        if (plannedDate == null) {
            return null;
        }
        return plannedDate.format(formatter);
    }

    public String getFormattedActualDate() {
        if (actualDate == null) {
            return null;
        }
        return actualDate.format(formatter);
    }
}
