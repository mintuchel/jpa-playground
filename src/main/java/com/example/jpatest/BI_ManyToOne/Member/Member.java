package com.example.jpatest.BI_ManyToOne.Member;

import com.example.jpatest.BI_ManyToOne.Team.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name="BI_MTO_Member")
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    private int id;

    private String nickName;

    // 보편적인 다대일에서 many쪽을 주인으로 설정
    // JoinColumn에 의해 Team의 PK가 Member 테이블에 team_id 라는 FK로 저장됨
    // 주인에도 cascade를 사용할 수 있지만 안하는게 좋음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public Member(String nickName){
        this.nickName = nickName;
    }
}
