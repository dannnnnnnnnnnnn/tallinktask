package com.dan.conferenceReservation.services;

import com.dan.conferenceReservation.domain.Conference;
import com.dan.conferenceReservation.domain.Participant;
import com.dan.conferenceReservation.repositories.ConferenceRepository;
import com.dan.conferenceReservation.repositories.ParticipantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component(value = "participantService")
public class ParticipantService implements IBaseService<Participant> {
    private ConferenceRepository conferenceRepository;
    private ParticipantRepository participantRepository;


    public ParticipantService(ParticipantRepository participantRepository, ConferenceRepository conferenceRepository) {
        this.participantRepository = participantRepository;
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    public ResponseEntity<List<Participant>> findAll() {
        List<Participant> participants = participantRepository.findAll();
        return ResponseEntity.ok(participants);
    }

    @Override
    public ResponseEntity save(Participant entity) {
        if (validate(entity)) {
            participantRepository.save(entity);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body("Invalid entity");
    }

    @Override
    public ResponseEntity<Participant> findById(long id) {
        Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isPresent()) {
            return ResponseEntity.ok(participant.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity delete(Participant entity) {
        Optional<Participant> participant = participantRepository.findById(entity.getId());
        if (participant.isPresent()) {
            for (Conference c: participant.get().getConferences()) {
                c.removeParticipant(entity);
                conferenceRepository.save(c);
            }
            participantRepository.deleteById(entity.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().body("Entity id " + entity.getId() + " not found");
    }

    @Override
    public ResponseEntity deleteById(long id) {
        Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isPresent()) {
            Participant p = participant.get();
            List<Conference> conferences = new ArrayList<>(p.getConferences());
            for (Conference c: conferences) {
                c.removeParticipant(participant.get());
                conferenceRepository.save(c);
            }
            participantRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    public boolean validate(Participant entity) {

        return (entity.getName().length() > 1 && entity.getBirthDate() != null);
    }
}
