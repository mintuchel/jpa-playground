package com.example.jpatest.BI_OneToMany.Member;

import com.example.jpatest.BI_OneToMany.Team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity(name="BI_OTM_Member")
@Getter
public class Member {
    @Id
    @GeneratedValue
    private int id;

    private String nickName;

    @ManyToOne
    private Team team;

    public Member(String nickName){ this.nickName = nickName; }

    public void setTeam(Team team){ this.team = team; }
}
