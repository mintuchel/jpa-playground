package com.example.jpatest.EagerLoading.Team;

import com.example.jpatest.EagerLoading.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="EAGER_Team")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // mappedBy 에는 주인 엔티티 변수명. DB 칼럼명이 아님!!
    // FetchType.LAZY : 실제로 참조하기 전에는 프록시 객체로 구성
    // CascadeType.ALL (PERSIST + REMOVE) : 부모인 Team entity에 영속성 변화가 일어나면 Member에도 영속성 전이를 함
    // orphanRemoval : 부모인 Team과의 관계가 끊어져 Member가 고아가 되면 Member는 자동으로 삭제됨
    @OneToMany(mappedBy="team", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Member> members = new ArrayList<>();

    /**
     * 연관관계는 동기화가 되지 않는다!
     */

    // 연관관계 편의 메서드 작성
    public void addMember(Member member){
        members.add(member);
        member.setTeam(this);
    }

    // 연관관계 편의 메서드 작성
    public void removeMember(Member member){
        members.remove(member);
        member.setTeam(null);
    }
}

