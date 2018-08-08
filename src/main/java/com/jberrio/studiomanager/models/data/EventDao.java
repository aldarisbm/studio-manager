package com.jberrio.studiomanager.models.data;

import com.jberrio.studiomanager.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventDao extends JpaRepository<Event, Integer> {
    List<Event> findByIsActive(int isActive);

}
