package com.example.jpatest.FetchType;

import com.example.jpatest.EagerLoading.Member.Member;
import com.example.jpatest.EagerLoading.Member.MemberRepository;
import com.example.jpatest.EagerLoading.Team.Team;
import com.example.jpatest.EagerLoading.Team.TeamRepository;
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
public class EagerLoadingTest {
    @Autowired
    @Qualifier("EAGER_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("EAGER_TeamRepository")
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("즉시로딩 프록시 객체가 아닌 실제 객체 타입 확인")
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
        // em.find 는 즉시로딩으로 호출되고 연관관계인 객체들도 지금은 FetchType.EAGER 로 선언되었으므로 모두 즉시로딩되어 가져옴
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow();

        // Eager Loading 으로 인해 Member 를 조회할때 연관관계가 있는 객체도 프록시 객체가 아닌 실제 객체로 조회가 됨!
        System.out.println("현재 Member의 Team 객체 클래스 확인 - 즉시로딩이므로 프록시 객체가 아닌 실제 객체 클래스 조회가 됨");
        System.out.println(foundMember.getTeam().getClass());

        System.out.println("===============================");

        System.out.println("foundMember.getTeam().getName() 호출 후");
        System.out.println("즉시 로딩이므로 이미 Member 조회할때 Team 도 조회가 됨 -> 추가적인 쿼리가 나가지 않음!");
        System.out.println("foundMember.getTeam().getName() = " + foundMember.getTeam().getName());
    }
}
