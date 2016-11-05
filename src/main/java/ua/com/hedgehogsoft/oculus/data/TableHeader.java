package ua.com.hedgehogsoft.oculus.data;

public enum TableHeader
{
   SURNAME("Фамилия"),
   ORDER_NUMBER("№ заказа"),
   PLANNED_DATE("Плановая дата исполнения"),
   ACTUAL_DATE("Фактическая дата исполнения"),
   CIPHER("Шифр"),
   PRODUCT_NAME("Изделие");

   private String name;

   private TableHeader(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }
}
