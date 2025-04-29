package com.example.jpatest.ManyToOne.Team;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="ManyToOne_Team")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="team_id")
    private int id;

    private String name;
}
