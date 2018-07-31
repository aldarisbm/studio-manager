package com.jberrio.studiomanager.models.data;

import com.jberrio.studiomanager.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDao extends JpaRepository<Event, Integer> {

}
