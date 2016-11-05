package ua.com.hedgehogsoft.oculus.service;

public interface SecurityService
{
   String findLoggedInUsername();

   void autologin(String username, String password);
}
