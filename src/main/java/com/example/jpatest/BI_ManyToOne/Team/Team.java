package com.example.jpatest.BI_ManyToOne.Team;

import com.example.jpatest.BI_ManyToOne.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="BI_MTO_Team")
@Getter
public class Team {
    @Id
    @GeneratedValue
    private int id;

    private String teamName;

    // mappedBy 에는 주인 엔티티 변수명. DB 칼럼명이 아님!!
    // FetchType.LAZY : 실제로 참조하기 전에는 프록시 객체로 구성
    // CascadeType.ALL : 부모인 Team entity에 영속성 변화가 일어나면 Member에도 영속성 전이를 함
    @OneToMany(mappedBy="team", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    public Team(final String teamName){
        this.teamName = teamName;
    }

    // 연관관계 편의 메서드 작성
    public void addMember(Member member){
        members.add(member);
        member.setTeam(this);
    }

    public void removeMember(Member member){
        members.remove(member);
        member.setTeam(null);
    }
}
