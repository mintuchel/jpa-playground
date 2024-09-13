package com.example.jpatest.ManyToOne.Team;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity(name="ManyToOne_Team")
@Getter
@Builder
public class Team {
    @Id
    @GeneratedValue
    @Column(name="team_id")
    private int id;

    private String name;
}
