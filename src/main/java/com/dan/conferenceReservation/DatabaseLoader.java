package com.dan.conferenceReservation;

import com.dan.conferenceReservation.domain.Conference;
import com.dan.conferenceReservation.domain.Participant;
import com.dan.conferenceReservation.domain.Room;
import com.dan.conferenceReservation.repositories.ConferenceRepository;
import com.dan.conferenceReservation.repositories.ParticipantRepository;
import com.dan.conferenceReservation.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private final RoomRepository repository;
    private final ConferenceRepository conferenceRepository;
    private final ParticipantRepository participantRepository;


    @Override
    public void run(String... args) throws Exception {
        /*Room room1 = new Room("main house", "paldiski mnt 1", 5);
        this.repository.save(room1);
        Conference conference = new Conference("conference 1", new ArrayList<>(), "2019-05-10T16:20:00", "2019-05-10T17:20:00");
        Participant participant = new Participant("participant 1", "2015-02-20");
        Participant participant1 = new Participant("participant 2", "2005-02-20");
        conference.setRoom(room1);
        List<Participant> conferenceParticipants = conference.getParticipants();
        conferenceParticipants.add(participant);
        conferenceParticipants.add(participant1);
        conference.setParticipants(conferenceParticipants);
        this.repository.save(room1);
        this.conferenceRepository.save(conference);
        this.participantRepository.save(participant);*/

    }

    @Autowired
    public DatabaseLoader(RoomRepository repository, ConferenceRepository cRepository, ParticipantRepository participantRepository) {
        this.repository = repository;
        this.conferenceRepository = cRepository;
        this.participantRepository = participantRepository;
    }
}
