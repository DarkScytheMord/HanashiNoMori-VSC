package com.HanashiNoMori.HanashiNoMori.Service;

import com.HanashiNoMori.HanashiNoMori.Model.Role;
import com.HanashiNoMori.HanashiNoMori.Model.User;
import com.HanashiNoMori.HanashiNoMori.Repository.RoleRepository;
import com.HanashiNoMori.HanashiNoMori.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User assignRoleToUser(String username, String roleName) {
        // Buscar usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar o crear rol
        Role role = roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(roleName);
            newRole.setDescription("Rol " + roleName);
            return roleRepository.save(newRole);
        });

        // Agregar rol al usuario
        user.getRoles().add(role);
        
        // Guardar usuario
        return userRepository.save(user);
    }
}
