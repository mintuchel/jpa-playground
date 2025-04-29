package com.example.jpatest.ManyToOne.Member;

import com.example.jpatest.ManyToOne.Team.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="ManyToOne_Member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private int id;

    private String name;

    /**
     * 보편적인 일대다 관계에서는 many 쪽을 주인으로 설정
     * JoinColumn 에 의해 Team 의 PK가 Member 테이블에 team_id 라는 FK 로 저장됨
     *
     * 일대다 관계에서 Many 쪽에 Cascade 옵션을 적용하지 않는 것이 맞음
     * Many 쪽이 변경되면 One 쪽에도 영향을 끼친다는 것이 말이 안되고
     * 여러 명의 Member 가 한 개의 Team 을 공유할 수 있는 구조에서는
     * Cascade 로 인해 한 Member 가 Team 을 갱신하면서 연관된 타 Member 들에게도 영향을 미칠 수 있음
     */
    @ManyToOne
    @JoinColumn(name="teamId")
    private Team team;
}
