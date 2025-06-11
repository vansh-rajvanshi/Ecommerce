package com.example.E_commerce.Project.Service;

import com.example.E_commerce.Project.Entity.Role;
import com.example.E_commerce.Project.Entity.User;
import com.example.E_commerce.Project.Repository.RoleRepository;
import com.example.E_commerce.Project.Repository.UserRepository;
import com.example.E_commerce.Project.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // Encrypt the password using Spring Security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Assign the default role "ROLE_USER" if not already exists
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = createDefaultRole();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    private Role createDefaultRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] nameParts = user.getName().split(" ", 2);
        userDto.setFirstName(nameParts[0]);
        userDto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public boolean checkIfEmailExists(String email) {
        return userRepository.findByEmail(email)!=null;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}