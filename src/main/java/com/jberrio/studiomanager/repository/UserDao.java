package com.jberrio.studiomanager.repository;

import com.jberrio.studiomanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
