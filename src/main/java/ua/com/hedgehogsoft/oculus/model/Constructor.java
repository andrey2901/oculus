package ua.com.hedgehogsoft.oculus.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "constructors")
public class Constructor
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Long id;

   @Column(name = "name", nullable = false)
   private String name;

   @OneToMany(mappedBy = "constructor", cascade = CascadeType.REMOVE)
   private List<Order> orders;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<Order> getOrders()
   {
      return orders;
   }

   public void setOrders(List<Order> orders)
   {
      this.orders = orders;
   }

   @Override
   public String toString()
   {
      return "Constructor [id=" + id + ", name=" + name + "]";
   }
}
