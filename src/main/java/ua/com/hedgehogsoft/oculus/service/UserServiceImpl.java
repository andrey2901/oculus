package ua.com.hedgehogsoft.oculus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.hedgehogsoft.oculus.model.User;
import ua.com.hedgehogsoft.oculus.repository.RoleRepository;
import ua.com.hedgehogsoft.oculus.repository.UserRepository;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService
{
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private RoleRepository roleRepository;
   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Override
   public void save(User user)
   {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      user.setRoles(new HashSet<>(roleRepository.findAll()));
      userRepository.save(user);
   }

   @Override
   public User findByUsername(String username)
   {
      return userRepository.findByName(username);
   }

   @Override
   public User findById(Long id)
   {
      return userRepository.findOne(id);
   }
}
