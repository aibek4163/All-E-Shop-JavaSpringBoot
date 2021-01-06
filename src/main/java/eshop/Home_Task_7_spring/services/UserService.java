package eshop.Home_Task_7_spring.services;

import eshop.Home_Task_7_spring.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserByEmail(String email);
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(User user);
    User changePassword(User user, String old, String newPass, String reNew);
    User editPassword(User user,String pass);
    void deleteUser(User user);
}
