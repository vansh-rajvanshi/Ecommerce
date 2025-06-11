package com.example.E_commerce.Project.Service;


import com.example.E_commerce.Project.Entity.Role;
import com.example.E_commerce.Project.Entity.User;
import com.example.E_commerce.Project.dto.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
    List<Role> getAllRoles();
    Role getRoleById(Long roleId);

    List<User> getAllUsers();

    User getUserById(Long id);
    boolean checkIfEmailExists(String email);

    boolean updatePassword(String email, String newPassword);
}