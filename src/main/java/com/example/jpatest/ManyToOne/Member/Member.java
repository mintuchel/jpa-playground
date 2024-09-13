package com.example.jpatest.ManyToOne.Member;

import com.example.jpatest.ManyToOne.Team.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity(name="ManyToOne_Member")
@Getter
@Builder
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private int id;

    private String name;

    // 보편적인 다대일에서 하는 many 쪽을 주인으로 설정
    // JoinColumn에 의해 Team의 PK가 Member 테이블에 teamId 라는 FK로 저장됨
    @ManyToOne
    @JoinColumn(name="teamId")
    private Team team;
    // ManyToOne에서는 cascade를 적용하지 않는 것이 맞음
    // 애초에 many쪽이 변경되면 one쪽에도 영향을 끼친다는게 말이 안되고
    // 여러 명의 member가 한 개의 team을 공유할 수 있는 구조에서는
    // cascade로 인해 한 member가 team을 갱신하면서 연관된 타 member들에도 영향을 미칠 수 있음

    public void setTeam(Team team){
        this.team = team;
    }
}
