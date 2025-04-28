package com.example.jpatest.FetchType;

import com.example.jpatest.BiManyToOne.Member.Member;
import com.example.jpatest.BiManyToOne.Member.MemberRepository;
import com.example.jpatest.BiManyToOne.Team.Team;
import com.example.jpatest.BiManyToOne.Team.TeamRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoadingTypeTest {
    @Autowired
    @Qualifier("BiMTO_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("BiMTO_TeamRepository")
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("지연로딩 프록시 객체 확인")
    public void checkProxyType() {
        Team team = Team.builder()
                .name("real madrid")
                .build();

        Member member = Member.builder()
                .name("jude")
                .team(team)
                .build();

        // em.save 동작 -> 영속성 컨텍스트에 엔티티 저장됨
        teamRepository.save(team);
        memberRepository.save(member);

        // em.flush 호출 -> 실제 db에 반영
        memberRepository.flush();
        teamRepository.flush();

        // 영속성 컨텍스트 비우기 -> 1차 캐시도 비워짐
        em.clear();

        // em.find 호출
        // em.find 는 즉시로딩으로 호출되지만 FetchType.LAZY 로 선언된 필드들은 지연로딩으로 가져옴
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow();

        // Lazy Loading 으로 인한 프록시 객체 확인 (초기화 전)
        System.out.println("현재 Member의 Team 프록시 객체 확인");
        System.out.println(foundMember.getTeam().getClass());
        System.out.println("===============================");

        System.out.println("foundMember.getTeam().getName() 호출 후");
        System.out.println("Team 이 프록시 객체라 쿼리가 한번 더 찍히게 됨!");
        System.out.println("foundMember.getTeam().getName() = " + foundMember.getTeam().getName());
    }
}
