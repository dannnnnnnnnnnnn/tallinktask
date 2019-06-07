package com.dan.conferenceReservation.controllers;

import com.dan.conferenceReservation.domain.Conference;
import com.dan.conferenceReservation.services.ConferenceService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ConferenceController {
    private ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/conferences")
    public ResponseEntity<List<Conference>> getConferences() {
        return conferenceService.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/conferences/{id}")
    public ResponseEntity<Conference> getConference(@PathVariable("id") Long id) {
        return conferenceService.findById(id);
    }

    @PostMapping(value = "/conferences")
    @CrossOrigin(origins = "*")
    public ResponseEntity createConference(@Valid @RequestBody  Conference conference) throws URISyntaxException {
       return conferenceService.save(conference);
    }

    @DeleteMapping(value = "/conferences/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteConference(@PathVariable("id") Long id) {
        return conferenceService.deleteById(id);
    }
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/conferences/{id}")
    public ResponseEntity updateConference(@Valid @RequestBody Conference conference) {
        return conferenceService.save(conference);
    }
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/conferences/{id}/participants/{id2}")
    public ResponseEntity addParticipant(@PathVariable("id") Long conferenceId, @PathVariable("id2") Long participantId) {
        return conferenceService.addParticipant(conferenceId, participantId);
    }
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/conferences/{id}/participants")
    public ResponseEntity getParticipants(@PathVariable("id") Long id) {
        return conferenceService.getParticipants(id);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/conferences/{id}/participants/{id2}")
    public ResponseEntity deleteParticipant(@PathVariable("id") Long conferenceId, @PathVariable("id2") Long participantId) {
        return conferenceService.removeParticipant(conferenceId, participantId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/conferences/rooms")
    public ResponseEntity getAvailableRooms(@RequestBody Conference conference) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return conferenceService.returnAvailableRooms(conference);
    }
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/conferences/{id}/room/{id2}")
    public ResponseEntity addRoom(@PathVariable("id") Long conferenceId, @PathVariable("id2") Long roomId) {
        return conferenceService.addRoom(conferenceId, roomId);
    }
}
