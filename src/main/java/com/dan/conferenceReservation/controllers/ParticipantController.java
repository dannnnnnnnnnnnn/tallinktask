package com.dan.conferenceReservation.controllers;

import com.dan.conferenceReservation.domain.Participant;
import com.dan.conferenceReservation.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ParticipantController {
    private ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/participants")
    public ResponseEntity<List<Participant>> getParticipants() {
        return participantService.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/participants/{id}")
    public ResponseEntity<Participant> getParticipant(@PathVariable("id") Long id) {
        return participantService.findById(id);
    }

    @PostMapping(value = "/participants")
    @CrossOrigin(origins = "*")
    public ResponseEntity createConference(@RequestBody @Valid  Participant participant) throws URISyntaxException {
        return participantService.save(participant);
    }
    @DeleteMapping(value = "/participants/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") Long id) {
        return participantService.deleteById(id);
    }
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/participants/")
    public ResponseEntity updateParticipant(@Valid @RequestBody Participant participant) {
        return participantService.save(participant);
    }
}
