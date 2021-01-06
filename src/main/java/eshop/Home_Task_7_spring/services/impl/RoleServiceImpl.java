package eshop.Home_Task_7_spring.services.impl;

import eshop.Home_Task_7_spring.entities.Role;
import eshop.Home_Task_7_spring.repositories.RoleRepository;
import eshop.Home_Task_7_spring.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }
}
