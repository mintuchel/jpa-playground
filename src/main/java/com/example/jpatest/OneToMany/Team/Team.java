package com.example.jpatest.OneToMany.Team;

import com.example.jpatest.OneToMany.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="OneToMany_Team")
@Getter
public class Team {
    @Id @GeneratedValue
    @Column(name="team_id")
    private int id;

    private String teamName;

    // 여기가 가장 헷갈리는 곳
    // OneToMany에서 보편적으로는 외래키가 존재하는 Many 쪽을 주인으로 하지만
    // One 쪽을 주인으로 한다면?
    // 우선 One 쪽에 @JoinColumn으로 주인임을 명시하고
    // JPA에서는 @JoinColumn(name="team_id")를 @OneToMany와 함께 사용한 경우,
    // 외래 키가 Member 엔티티에 위치해야 하지만, 외래 키의 이름을 명시적으로 지정하겠다는 의미입니다.
    // 즉, @JoinColumn(name="team_id")는 Member 테이블에 있는 team_id 컬럼이 Team 테이블의 기본 키(일반적으로 id)를 참조하는 외래 키라는 것을 나타냅니다
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // 영속성 전이 적용 (부모에서 자식 객체로의 영속성 전이)
    // members에 member를 저장해놓고 team을 저장하기만 하면
    // members에 저장한 member 엔티티도 persist 가 자동으로 나감!
    @JoinColumn(name = "team_id")
    private List<Member> members = new ArrayList<>();

    public Team(final String teamName){
        this.teamName = teamName;
    }

    public void addMember(Member member){
        members.add(member);
    }

    public void removeMember(Member member){
        members.remove(member);
    }
}
