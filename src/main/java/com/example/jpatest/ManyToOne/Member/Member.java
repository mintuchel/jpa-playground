package com.example.jpatest.ManyToOne.Member;

import com.example.jpatest.ManyToOne.Team.Team;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name="ManyToOne_Member")
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private int id;

    private String nickName;

    // 보편적인 다대일에서 "다" 쪽을 주인으로 설정
    // JoinColumn에 의해 Team의 PK가 Member 테이블에 teamId 라는 FK로 저장됨
    @ManyToOne
    // ManyToOne에서는 Cascade를 적용하지 않는 것이 맞음
    // 애초에 "다"쪽이 변경되면 "일"쪽에도 영향을 끼친다는게 말이 안되고
    // Member가 여러 개의 Team을 공유할 수 있는 구조에서는 cascade로 인해 한 Member가 Team을 갱신하면서,
    // 다른 Member들에도 영향을 미칠 수 있음
    @JoinColumn(name="teamId")
    private Team team;

    public Member(String nickName){
        this.nickName = nickName;
    }

    public void setTeam(Team team){
        this.team = team;
    }
}
