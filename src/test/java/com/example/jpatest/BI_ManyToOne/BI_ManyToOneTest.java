package com.example.jpatest.BI_ManyToOne;

import com.example.jpatest.BI_ManyToOne.Member.Member;
import com.example.jpatest.BI_ManyToOne.Member.MemberRepository;
import com.example.jpatest.BI_ManyToOne.Team.Team;
import com.example.jpatest.BI_ManyToOne.Team.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BI_ManyToOneTest {
    @Autowired
    @Qualifier("BI_MTO_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("BI_MTO_TeamRepository")
    TeamRepository teamRepository;

    @Test
    @DisplayName("다대일 양방향 멤버 추가 성공")
    void addMemberToTeamSuccess(){
        Team team = new Team("chelesa");
        teamRepository.save(team);

        Member member = new Member("palmer");
        member.setTeam(team);
        memberRepository.save(member);

        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();

        Assertions.assertThat(savedMember.getTeam()).isEqualTo(team);
    }

    @Test
    @DisplayName("다대일 양방향 멤버 추가 실패")
    void addMemberToTeamFail(){
        Member member = new Member("mbappe");
        memberRepository.save(member);

        Team team = new Team("realmadrid");
        team.getMembers().add(member);
        teamRepository.save(team);

        // Team 쪽에서 Member 가 memberList 에 저장되었는지 확인
        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
        Assertions.assertThat(savedTeam.getMembers().contains(member)).isTrue();

        // Member 쪽에서 Team이 team으로 저장되지 않았는지 확인
        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();
        Assertions.assertThat(member.getTeam()).isNull();
    }
}
