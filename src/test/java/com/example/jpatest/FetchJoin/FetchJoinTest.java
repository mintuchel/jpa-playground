package com.example.jpatest.FetchJoin;


import com.example.jpatest.FetchJoin.Member.Member;
import com.example.jpatest.FetchJoin.Member.MemberRepository;
import com.example.jpatest.FetchJoin.Team.Team;
import com.example.jpatest.FetchJoin.Team.TeamRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FetchJoinTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("JPQL 일반 JOIN 쿼리 테스트")
    public void DefaultOuterJoinTest() {
        Team team1 = Team.builder()
                .name("liverpool")
                .build();

        Team team2 = Team.builder()
                .name("brighton")
                .build();

        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = Member.builder()
                .name("mac allister")
                .team(team1)
                .build();

        Member member2 = Member.builder()
                .name("mitoma")
                .team(team2)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberByTeamOuterJoin();
        members.forEach( m -> System.out.println("member:"+m));

        System.out.println("=======================");

        /**
         * JPQL 은 결과를 반환할때 연관관계까지 고려하지 않음.
         * 단지 SELECT 절에 지정된 엔티티만 조회할 뿐임
         */
        members.forEach( m -> System.out.println(m.getTeam().getClass()));
    }

    @Test
    @DisplayName("JPQL Fetch JOIN 쿼리 테스트")
    public void fetchJoinTest() {
        Team team1 = Team.builder()
                .name("liverpool")
                .build();

        Team team2 = Team.builder()
                .name("brighton")
                .build();

        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = Member.builder()
                .name("mac allister")
                .team(team1)
                .build();

        Member member2 = Member.builder()
                .name("mitoma")
                .team(team2)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberByTeamFetchJoin();
        members.forEach( m -> System.out.println("member:"+m));

        System.out.println("=======================");

        /**
         * JPQL 은 결과를 반환할때 연관관계까지 고려하지 않음.
         * 단지 SELECT 절에 지정된 엔티티만 조회할 뿐임
         */
        members.forEach( m -> System.out.println(m.getTeam().getClass()));
    }
}
