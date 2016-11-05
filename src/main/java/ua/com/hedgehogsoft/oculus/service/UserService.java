package ua.com.hedgehogsoft.oculus.service;

import ua.com.hedgehogsoft.oculus.model.User;

public interface UserService
{
   void save(User user);

   User findByUsername(String username);

   User findById(Long id);
}
