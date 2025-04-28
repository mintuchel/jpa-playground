package com.example.jpatest.FetchJoin.Member;

import com.example.jpatest.FetchJoin.Team.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="FJ_Member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    // 주인이 아닌 Team 쪽에 위치한 연관관계 편의 메서드 때문에 필요
    public void setTeam(Team team){
        this.team = team;
    }
}
