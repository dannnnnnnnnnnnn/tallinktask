package com.dan.conferenceReservation.services;

import com.dan.conferenceReservation.domain.Conference;
import com.dan.conferenceReservation.domain.Room;
import com.dan.conferenceReservation.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component(value = "roomService")
public class RoomService implements IBaseService<Room> {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    public boolean validate(Room entity) {
        return (entity.getName().length() > 1 && entity.getSeats() > 1 && entity.getLocation().length() > 1);
    }

    @Override
    public ResponseEntity<List<Room>> findAll() {
        List<Room> rooms = roomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }

    @Override
    public ResponseEntity<Room> save(Room entity) {
        if (validate(entity) && canUpdateRoom(entity)) {
            roomRepository.save(entity);
            return ResponseEntity.ok(entity);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Room> findById(long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return ResponseEntity.ok(room.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity delete(Room entity) {
        Optional<Room> room = roomRepository.findById(entity.getId());
        if (room.isPresent()) {
            roomRepository.deleteById(entity.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity deleteById(long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            roomRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    public boolean canUpdateRoom(Room room) {
        List<Conference> conferences = room.getConferencesList();
        for (Conference c: conferences) {
            if(room.getSeats() < c.getParticipants().size()) {
                return false;
            }
        }
        return true;
    }
}
