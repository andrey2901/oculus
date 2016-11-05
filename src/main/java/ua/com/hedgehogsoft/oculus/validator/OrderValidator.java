package ua.com.hedgehogsoft.oculus.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.com.hedgehogsoft.oculus.model.Order;

@Component
public class OrderValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @SuppressWarnings("unused")
		Order order = (Order) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderNumber", "not.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plannedDate", "not.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productName", "not.empty");
    }
}
