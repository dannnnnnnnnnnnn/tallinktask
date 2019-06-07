package com.dan.conferenceReservation.repositories;

import com.dan.conferenceReservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAll();

}
