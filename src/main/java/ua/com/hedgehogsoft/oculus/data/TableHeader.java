package ua.com.hedgehogsoft.oculus.data;

/**
 * Created by Andrey on 13.10.2016.
 */
public enum TableHeader {
    SURNAME("Фамилия"),
    ORDER_NUMBER("№ заказа"),
    PLANNED_DATE("Плановая дата исполнения"),
    ACTUAL_DATE("Фактическая дата исполнения"),
    CIPHER("Шифр"),
    PRODUCT_NAME("Изделие");

    private String  name;

    TableHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
