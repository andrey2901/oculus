package ua.com.hedgehogsoft.oculus.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.com.hedgehogsoft.oculus.model.User;
import ua.com.hedgehogsoft.oculus.service.UserService;

@Component
public class UpdateValidator implements Validator
{
   @Autowired
   private UserService userService;
   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Override
   public boolean supports(Class<?> aClass)
   {
      return User.class.equals(aClass);
   }

   @Override
   public void validate(Object o, Errors errors)
   {
      User user = (User) o;

      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "not.empty");
      if (user.getUsername().length() < 5 || user.getUsername().length() > 32)
      {
         errors.rejectValue("username", "size.userForm.username");
      }

      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordOld", "not.empty");
      User existed = userService.findById(user.getId());
      if (!bCryptPasswordEncoder.matches(user.getPasswordOld(), existed.getPassword()))
      {
         errors.rejectValue("passwordOld", "diff.userForm.passwordOld");
      }

      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "not.empty");
      if (user.getPassword().length() < 6 || user.getPassword().length() > 32)
      {
         errors.rejectValue("password", "size.userForm.password");
      }

      if (!user.getPasswordConfirm().equals(user.getPassword()))
      {
         errors.rejectValue("passwordConfirm", "diff.userForm.passwordConfirm");
      }
   }
}
