package com.example.jpatest.OneToMany.Team;

import com.example.jpatest.OneToMany.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="OneToMany_Team")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="team_id")
    private int id;

    private String name;

    /**
     * 여기가 가장 헷갈리는 곳
     *
     * OneToMany 에서는 보편적으로 외래키가 존재하는 Many 쪽을 주인으로 하지만 만약 One 쪽을 주인으로 한다면?
     * 우선 One 쪽에서 @JoinColumn 으로 주인임을 명시하고
     * JPA 에서는 @JoinColumn(name="team_id")를 @OneToMany 와 함께 사용한 경우,
     * 외래키가 Member 엔티티에 위치해야하지만, 외래키의 이름을 명시적으로 지정하겠다는 의미임
     * 즉, @JoinColumn(name="team_id")는 Member 테이블에 있는 team_id 컬럼이 Team 테이블의 기본 키(일반적으로 id)를 참조하는 외래 키라는 것을 나타냄.
     *
     * 추가적으로 Cascade 옵션을 통해 영속성 전이를 적용하여
     * members 에 Member 를 저장하고 Team 을 저장하기만 하면
     * Member 에 저장한 Member 엔티티도 영속성컨텍스트로 영속상태로 들어가게 됨!
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_id")
    @Builder.Default
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member){
        members.add(member);
    }

    public void removeMember(Member member){
        members.remove(member);
    }
}
