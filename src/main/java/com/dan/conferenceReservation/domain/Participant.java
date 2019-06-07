package com.dan.conferenceReservation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Participant {
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY)  Long Id;
    @NonNull @Size(max = 100)
    private  String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "participants")
    private List<Conference> conferences = new ArrayList<>();
    @NonNull
    private LocalDate birthDate;

    public Participant() {
    }

    public Participant (String name, String birthdate) {
        this.name = name;
        this.birthDate = LocalDate.parse(birthdate);
    }
    public void removeConference (Conference c) {
        this.conferences.remove(c);
    }
}
