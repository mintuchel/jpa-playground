package com.example.jpatest.ManyToOne;

import com.example.jpatest.ManyToOne.Member.Member;
import com.example.jpatest.ManyToOne.Member.MemberRepository;
import com.example.jpatest.ManyToOne.Team.Team;
import com.example.jpatest.ManyToOne.Team.TeamRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

// 다대일 단방향 테스트
@DataJpaTest
@ActiveProfiles("test")
class ManyToOneTest {

    @Autowired
    @Qualifier("MTO_TeamRepository")
    TeamRepository teamRepository;

    @Autowired
    @Qualifier("MTO_MemberRepository")
    MemberRepository memberRepository;

    @Test
    @DisplayName("다대일 단방향 멤버 추가 성공")
    void addMemberToTeamSuccess() {
        Team team = new Team("mancity");
        teamRepository.save(team);

        Member member = new Member("gundogan");
        // 연관관계의 주인이 Many 쪽이므로 Many 쪽에 One 추가
        member.setTeam(team);
        memberRepository.save(member);

        Member savedMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("Member not found")
        );

        Assertions.assertThat(savedMember.getTeam()).isEqualTo(team);
    }

    // 단방향일때는 실패하는 경우가 없다
    @Test
    @DisplayName("다대일 단방향 멤버 추가 실패")
    void addMemberToTeamFail(){

    }
}