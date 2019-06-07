package com.dan.conferenceReservation.controllers;

import com.dan.conferenceReservation.domain.Room;
import com.dan.conferenceReservation.services.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class RoomController {
    private RoomService roomService;
    public RoomController(RoomService rs) {
        roomService = rs;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/rooms")
    public ResponseEntity<List<Room>> getRooms() {
        return roomService.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/rooms/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable("id") Long id) {
      return roomService.findById(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/rooms")
    public ResponseEntity<Room> createRoom(@RequestBody  Room room) throws URISyntaxException {
        return roomService.save(room);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        return roomService.deleteById(id);
    }
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/rooms/")
    public ResponseEntity<Room> updateRoom(@Valid @RequestBody Room room) {
            return roomService.save(room);
    }
}
