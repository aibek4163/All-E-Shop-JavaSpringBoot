package eshop.Home_Task_7_spring.services.impl;

import eshop.Home_Task_7_spring.entities.Role;
import eshop.Home_Task_7_spring.entities.User;
import eshop.Home_Task_7_spring.repositories.RoleRepository;
import eshop.Home_Task_7_spring.repositories.UserRepository;
import eshop.Home_Task_7_spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User myUser = userRepository.findByEmail(s);
        if(myUser!=null){
            org.springframework.security.core.userdetails.User secUser = new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
            return secUser;
        }
        throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        User u = userRepository.findByEmail(user.getEmail());
        if(u==null){
            Role role = roleRepository.findByRole("ROLE_USER");
            if(role!=null){
                ArrayList<Role> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User changePassword(User user,String old,String newPass,String reNew) {
        if(passwordEncoder.matches(old,user.getPassword()) && newPass.equals(reNew)){
            user.setPassword(passwordEncoder.encode(reNew));
            return userRepository.save(user);
        }
//        else if(!passwordEncoder.matches(old,user.getPassword()) || !newPass.equals(reNew)){
//            return
//        }
        return null;
    }

    @Override
    public User editPassword(User user, String pass) {
        user.setPassword(passwordEncoder.encode(pass));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
