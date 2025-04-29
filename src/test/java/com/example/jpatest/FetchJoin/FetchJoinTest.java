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
    public void defaultJoinTest() {
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

        /**
         * JPQL 로 명시적인 left outer join 을 해도 join 문이 쿼리에 찍히지 않는 이유는
         * JPQL 에서 JOIN 은 SELECT 문에 JOIN 할 테이블 칼럼명이 전제되어야 실제 SQL에 JOIN 이 생성되기 때문임
         * 현재는 Member m 만 SELECT 하는 것이므로 Team 관련 칼럼은 사용하지 않음
         * -> 따라서 Team 과의 JOIN 문 자체가 생성되지 않는 것이다
         */
        List<Member> members = memberRepository.findMemberByTeamOuterJoin();
        members.forEach( m -> System.out.println("member:"+m));

        System.out.println("=======================");

        /**
         * Team 객체를 JOIN 으로 가져온 것이 아니니,
         * 각 Member 의 Team 객체는 모두 프록시 객체로 나타남
         */
        members.forEach( m -> System.out.println(m.getTeam().getClass()));

        System.out.println("=======================");

        /**
         * Team 을 프록시 객체로 가져온 것이므로
         * Team 참조시 추가 쿼리가 찍힘
         * 1+N 문제가 발생하고 있음을 쿼리 찍히는 거로 확인가능!
         */
        members.forEach(m -> System.out.println(m.getTeam().getName()));
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

        /**
         * JPQL fetch join 을 활용하면 실제로 Member 만 SELECT 한다고 해도
         * SQL 문에 JOIN 을 하여 Team 객체까지 모두 가져옴
         */
        List<Member> members = memberRepository.findMemberByTeamFetchJoin();
        members.forEach( m -> System.out.println("member:"+m));

        System.out.println("=======================");

        /**
         * 따라서 Team 객체는 프록시 객체가 아닌 실제 객체로 찍힘!
         */
        members.forEach( m -> System.out.println(m.getTeam().getClass()));

        System.out.println("=======================");

        /**
         * 이미 각 Member 객체와 연관된 Team 객체를 가져와 추가적인 쿼리가 안나감
         * 1+N 문제가 안일어남!
         */
        members.forEach(m -> System.out.println(m.getTeam().getName()));
    }
}
