package com.example.E_commerce.Project.Repository;

import com.example.E_commerce.Project.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}