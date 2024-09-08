package com.example.jpatest.OneToMany;

import com.example.jpatest.OneToMany.Member.Member;
import com.example.jpatest.OneToMany.Member.MemberRepository;
import com.example.jpatest.OneToMany.Team.Team;
import com.example.jpatest.OneToMany.Team.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

// 일대다 단방향 테스트
// 이걸 하면 update 쿼리가 세개가 나감!
@DataJpaTest
@ActiveProfiles("test")
public class OneToManyTest {
    @Autowired
    @Qualifier("OTM_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("OTM_TeamRepository")
    TeamRepository teamRepository;

    @Test
    @DisplayName("일대다 단방향 멤버 한명 추가 성공")
    public void addMemberToTeamSuccess(){
        Member member = new Member("gakpo");

        Team team = new Team("liverpool");
        team.addMember(member);

        // cascade.ALL이라 team 저장할때 team의 members에 있는 member도 모두 persist 되어 영속성 컨텍스트에 들어가게 됨
        teamRepository.save(team);

        // @OneToMany(cascade = CascadeType.ALL) 로 해서 이게 통과가 되는거임
        // 만약 Cascade 명시 안했으면 member는 자동저장 안됨!
        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();
        Assertions.assertThat(savedMember.getId()).isEqualTo(member.getId());

        // Team 쪽에서 member가 memberList에 저장되었는지 확인
        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
        Assertions.assertThat(savedTeam.getMembers().contains(member)).isTrue();
    }

    @Test
    @DisplayName("일대다 단방향 멤버 여러명 추가 성공")
    public void addMembersToTeamSuccess(){
        Member m1 = new Member("nunez");
        Member m2 = new Member("salah");
        Member m3 = new Member("diaz");

        Team liverpool = new Team("liverpool");
        liverpool.addMember(m1);
        liverpool.addMember(m2);
        liverpool.addMember(m3);
        teamRepository.save(liverpool);

        // @OneToMany(cascade = CascadeType.ALL) 로 해서 이게 통과가 되는거임
        // 만약 cascade 명시 안했으면 member는 자동저장 안됨!
        Member savedMember = memberRepository.findById(m2.getId()).orElseThrow();
        Assertions.assertThat(savedMember.getId()).isEqualTo(m2.getId());

        // Team 쪽에서 member가 memberList에 저장되었는지 확인
        Team savedTeam = teamRepository.findById(liverpool.getId()).orElseThrow();
        Assertions.assertThat(savedTeam.getMembers().size()).isEqualTo(3);
    }
}