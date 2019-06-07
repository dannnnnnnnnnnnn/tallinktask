package com.dan.conferenceReservation.services;

import com.dan.conferenceReservation.domain.Conference;
import com.dan.conferenceReservation.domain.Participant;
import com.dan.conferenceReservation.domain.Room;
import com.dan.conferenceReservation.repositories.ConferenceRepository;
import com.dan.conferenceReservation.repositories.ParticipantRepository;
import com.dan.conferenceReservation.repositories.RoomRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component(value = "conferenceService")
public class ConferenceService implements IBaseService<Conference> {
    private ConferenceRepository conferenceRepository;
    private ParticipantRepository participantRepository;
    private RoomRepository roomRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, ParticipantRepository participantRepository,
                             RoomRepository roomRepository) {
        this.conferenceRepository = conferenceRepository;
        this.participantRepository = participantRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public ResponseEntity<List<Conference>> findAll() {
        List<Conference> conferences = conferenceRepository.findAll();
        return ResponseEntity.ok(conferences);
    }

    @Override
    public ResponseEntity save(Conference entity) {
        if (validate(entity)) {

            conferenceRepository.save(entity);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return ResponseEntity.unprocessableEntity().body("Conference isn't valid");
    }

    @Override
    public ResponseEntity<Conference> findById(long id) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(id);
        if (conferenceOptional.isPresent()) {
            return ResponseEntity.ok(conferenceOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity delete(Conference entity) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(entity.getId());
        if (conferenceOptional.isPresent()) {
            conferenceRepository.delete(entity);
            return ResponseEntity.ok(conferenceOptional.get());
        }
        return ResponseEntity.badRequest().body("Entity id " + entity.getId() + " not found");
    }

    @Override
    public ResponseEntity deleteById(long id) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(id);
        if (conferenceOptional.isPresent()) {
            conferenceRepository.deleteById(id);
            return ResponseEntity.ok(conferenceOptional.get());
        }
        return ResponseEntity.badRequest().body("Entity id " + id + " not found");
    }

    public boolean validate(Conference entity) {
        return (entity.getName().length() > 1);
    }

    public ResponseEntity addParticipant(Long conferenceId, Long participantId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        Optional<Participant> participantOptional = participantRepository.findById(participantId);
        if (conferenceOptional.isPresent() && participantOptional.isPresent()) {
            Conference conference = conferenceOptional.get();
            List<Participant> participants = conference.getParticipants();
            if (!participants.contains(participantOptional.get())) {
                if (conference.getRoom() == null || participants.size() < conference.getRoom().getSeats()) {
                    conference.addParticipant(participantOptional.get());
                    conferenceRepository.save(conference);
                    return ResponseEntity.ok(conference);
                } else {
                    return ResponseEntity.unprocessableEntity().body("Too many guests");
                }
            }
            return ResponseEntity.badRequest().body("Participant already registered");
        }
        return ResponseEntity.unprocessableEntity().body("Error in entity id");
    }

    public ResponseEntity removeParticipant(Long conferenceId, Long participantId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        Optional<Participant> participantOptional = participantRepository.findById(participantId);
        if (conferenceOptional.isPresent() && participantOptional.isPresent()) {
            Conference conference = conferenceOptional.get();
            conference.removeParticipant(participantOptional.get());
            conferenceRepository.save(conference);
            return ResponseEntity.ok(conference);
        }
        return ResponseEntity.badRequest().body("Conference or participant doesn't exist");
    }
    public ResponseEntity addRoom(Long conferenceId, Long roomId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if(roomOptional.isPresent() && conferenceOptional.isPresent()) {
            Conference conference = conferenceOptional.get();
            Room room = roomOptional.get();
            if (getAvailableRooms(conference).contains(room)) {
                conference.setRoom(room);
                conferenceRepository.save(conference);
                return ResponseEntity.ok(conference);
            }
        }
        return ResponseEntity.badRequest().body("Room or conference not available");
    }
    public boolean canBookRoom(Room room, Conference conference) {
        if (conference.getStartTime().isBefore(conference.getEndTime()) && room.getSeats() >= conference.getParticipants().size()) {
            List<Conference> conferences = room.getConferencesList();
            for (Conference c : conferences) {
                if (c.equals(conference)) {
                    continue;
                }
                if (isOverLapping(conference, c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isOverLapping(Conference c1, Conference c2) {
        return (c1.getStartTime().isBefore(c2.getEndTime()) && c2.getStartTime().isBefore(c1.getEndTime()));
    }

    public ResponseEntity getParticipants(Long id) {
        List<Participant> participants = conferenceRepository.getOne(id).getParticipants();
        return ResponseEntity.ok(participants);
    }

    public ResponseEntity returnAvailableRooms(Conference entity) {
        return ResponseEntity.ok(getAvailableRooms(entity));
    }

    public List<Room> getAvailableRooms(Conference entity) {
        List<Room> rooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (canBookRoom(room, entity)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;

    }
}
