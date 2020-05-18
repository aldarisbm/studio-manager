package com.jberrio.studiomanager.repository;

import com.jberrio.studiomanager.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventDao extends JpaRepository<Event, Integer> {
    List<Event> findByIsActive(int isActive);

}
