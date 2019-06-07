package com.dan.conferenceReservation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity

public class Room {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long Id;
    @NonNull @Length(min = 1, max = 100) private String name;
    @NonNull @Length(min = 1, max = 150) private String location;
    @Min(1) @Max(10000) private int seats;
    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("room")
    @JsonIgnore
    private List<Conference> conferencesList = new ArrayList<>();
    public Room() {

    }
    public Room (String name, String location, int seats) {
        this.name = name;
        this.location = location;
        this.seats = seats;
    }
}
