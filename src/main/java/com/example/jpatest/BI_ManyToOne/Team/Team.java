package com.example.jpatest.BI_ManyToOne.Team;

import com.example.jpatest.BI_ManyToOne.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="BI_MTO_Team")
@Getter
public class Team {
    @Id
    @GeneratedValue
    @Column(name="team_id")
    private int id;

    private String teamName;

    // mappedBy 에는 주인 엔티티 변수명. DB 칼럼명이 아님!!
    @OneToMany(mappedBy="id")
    private List<Member> members = new ArrayList<>();

    public Team(final String teamName){
        this.teamName = teamName;
    }
}
