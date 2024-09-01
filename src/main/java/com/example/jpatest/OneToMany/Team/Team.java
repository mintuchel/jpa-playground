package com.example.jpatest.OneToMany.Team;

import com.example.jpatest.OneToMany.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="OneToMany_Team")
@Getter
public class Team {
    @Id @GeneratedValue
    @Column(name="team_id")
    private int id;

    private String teamName;

    @OneToMany
    @JoinColumn(name = "team_id")
    private List<Member> members = new ArrayList<>();

    public Team(final String teamName){
        this.teamName = teamName;
    }

    public void addMember(Member member){
        members.add(member);
    }
}
