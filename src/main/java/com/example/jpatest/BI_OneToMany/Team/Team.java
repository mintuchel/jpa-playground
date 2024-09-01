package com.example.jpatest.BI_OneToMany.Team;

import com.example.jpatest.BI_OneToMany.Member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private List<Member> members = new ArrayList<>();

    public Team(String teamName){this.teamName = teamName;}
}
