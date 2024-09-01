package com.example.jpatest.BI_ManyToOne.Member;

import com.example.jpatest.BI_ManyToOne.Team.Team;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name="BI_MTO_Member")
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private int id;

    private String nickName;

    // 보편적인 다대일에서 "다" 쪽을 주인으로 설정
    // JoinColumn에 의해 Team의 PK가 Member 테이블에 teamId 라는 FK로 저장됨
    @ManyToOne
    @JoinColumn(name="teamId")
    private Team team;

    public Member(String nickName){
        this.nickName = nickName;
    }

    public void setTeam(Team team){
        this.team = team;
    }
}
