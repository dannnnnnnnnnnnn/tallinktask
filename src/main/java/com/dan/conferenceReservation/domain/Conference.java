package com.dan.conferenceReservation.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.Cascade;


import javax.persistence.*;
import javax.servlet.http.Part;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Conference {
    @Id @GeneratedValue
    private Long Id;
    @NonNull @Size(max = 150)
    private String name;
   @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
   @JoinTable(name = "conference_participant", joinColumns = @JoinColumn(name = "conference_id"),
   inverseJoinColumns = @JoinColumn(name="participant_id"))
   private List<Participant> participants = new ArrayList<>();
   @JsonInclude(JsonInclude.Include.NON_NULL)
   @ManyToOne
   private Room room;
   @NonNull
   private LocalDateTime startTime;
   @NonNull
   private LocalDateTime endTime;

    public Conference() {

    }

    public Conference(String name, List<Participant> participants, String startTime, String endTime) {
        this.name = name;
        this.participants = participants;
        this.startTime = LocalDateTime.parse(startTime);
        this.endTime = LocalDateTime.parse(endTime);
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
        participant.getConferences().add(this);
    }
    public void removeParticipant(Participant participant) {
        this.participants.remove(participant);
        participant.removeConference(this);
    }
    public void setRoom(Room room) {
        this.room = room;
        if (room != null) {
            room.getConferencesList().add(this);
        }
    }
}
