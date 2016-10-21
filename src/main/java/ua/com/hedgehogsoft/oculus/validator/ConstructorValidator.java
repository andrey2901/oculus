package ua.com.hedgehogsoft.oculus.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.com.hedgehogsoft.oculus.model.Constructor;
import ua.com.hedgehogsoft.oculus.repository.ConstructorRepository;

@Component
public class ConstructorValidator implements Validator {
    @Autowired
    private ConstructorRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Constructor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Constructor constructor = (Constructor) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "not.empty");
        if (constructor.getName().length() < 4 || constructor.getName().length() > 32) {
            errors.rejectValue("name", "size.constructor.name");
        }
        if (repository.findByName(constructor.getName()) != null) {
            errors.rejectValue("name", "duplicate.constructor.name");
        }
    }
}
