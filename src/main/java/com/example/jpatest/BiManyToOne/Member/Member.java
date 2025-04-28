package com.example.jpatest.BiManyToOne.Member;

import com.example.jpatest.BiManyToOne.Team.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="BiMTO_Member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // 보편적인 다대일에서 many쪽을 주인으로 설정
    // JoinColumn에 의해 Team의 PK가 Member 테이블에 team_id 라는 FK로 저장됨
    // 주인에도 cascade를 사용할 수 있지만 안하는게 좋음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    // 연관관계 편의 메서드 때문에 필요
    public void setTeam(Team team){
        this.team = team;
    }
}
