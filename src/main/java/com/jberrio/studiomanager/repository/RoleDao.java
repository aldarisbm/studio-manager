package com.jberrio.studiomanager.repository;

import com.jberrio.studiomanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}
