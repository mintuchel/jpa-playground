package com.example.jpatest.ManyToOne.Team;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name="ManyToOne_Team")
@Getter
public class Team {
    @Id
    @GeneratedValue
    @Column(name="team_id")
    private int id;

    private String teamName;

    public Team(final String teamName){
        this.teamName = teamName;
    }
}
