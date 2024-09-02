package com.example.jpatest.BI_OneToMany.Team;

import com.example.jpatest.BI_OneToMany.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="BI_OTM_Team")
@Getter
public class Team {
    @Id
    @GeneratedValue
    private int id;

    private String teamName;

    @OneToMany
    @JoinColumn(name="id")
    private List<Member> members = new ArrayList<>();

    public Team(String teamName){this.teamName = teamName;}
}
