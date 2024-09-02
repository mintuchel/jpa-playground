package com.example.jpatest.BI_OneToMany.Member;

import com.example.jpatest.BI_OneToMany.Team.Team;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name="BI_OTM_Member")
@Getter
public class Member {
    @Id
    @GeneratedValue
    private int id;

    private String nickName;

    // @ManyToOne은 mappedBy 가 존재하지 않음!!!
    // 그래서 이 테스트는 진행 조차 못함!
    // @ManyToOne(mappedBy = "id")
    // private Team team;

    public Member(String nickName){ this.nickName = nickName; }

    // public void setTeam(Team team){ this.team = team; }
}
