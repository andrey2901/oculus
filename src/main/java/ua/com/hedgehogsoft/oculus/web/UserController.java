package ua.com.hedgehogsoft.oculus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.hedgehogsoft.oculus.model.User;
import ua.com.hedgehogsoft.oculus.service.SecurityService;
import ua.com.hedgehogsoft.oculus.service.UserService;
import ua.com.hedgehogsoft.oculus.validator.UpdateValidator;
import ua.com.hedgehogsoft.oculus.validator.UserValidator;

import java.security.Principal;

@Controller
public class UserController
{
   @Autowired
   private UserService userService;

   @Autowired
   private SecurityService securityService;

   @Autowired
   private UserValidator userValidator;

   @Autowired
   private UpdateValidator updateValidator;

   @RequestMapping(value = "/registration", method = RequestMethod.GET)
   public String registration(Model model)
   {
      model.addAttribute("userForm", new User());
      return "registration";
   }

   @RequestMapping(value = "/registration", method = RequestMethod.POST)
   public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model)
   {
      userValidator.validate(userForm, bindingResult);

      if (bindingResult.hasErrors())
      {
         return "registration";
      }

      userService.save(userForm);

      securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

      return "redirect:/console";
   }

   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public String login(Model model, String error, String logout)
   {
      if (error != null)
         model.addAttribute("error", "Your username and password is invalid.");

      if (logout != null)
         model.addAttribute("message", "You have been logged out successfully.");

      return "login";
   }

   @RequestMapping(value = "/console", method = RequestMethod.GET)
   public String console()
   {
      return "console";
   }

   @RequestMapping(value = "/update", method = RequestMethod.GET)
   public String update(Model model, Principal principal)
   {
      String name = principal.getName();
      User user = new User();
      user.setUsername(name);
      model.addAttribute("userForm", user);
      return "update";
   }

   @RequestMapping(value = "/update", method = RequestMethod.POST)
   public String update(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Principal principal)
   {
      String name = principal.getName();
      User user = userService.findByUsername(name);
      userForm.setId(user.getId());
      updateValidator.validate(userForm, bindingResult);
      if (bindingResult.hasErrors())
      {
         return "update";
      }
      userService.save(userForm);
      securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
      return "redirect:/console";
   }
}
