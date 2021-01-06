package eshop.Home_Task_7_spring.services;

import eshop.Home_Task_7_spring.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role addRole(Role role);
    Role getRoleById(Long id);
    void deleteRole(Role role);
    Role updateRole(Role role);
}
