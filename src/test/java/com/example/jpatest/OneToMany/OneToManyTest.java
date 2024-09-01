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

// 일대다 단방향 테스트
// 이걸 하면 update 쿼리가 세개가 나감!
// 이 점 다시 찾아보기
@DataJpaTest
public class OneToManyTest {
    @Autowired
    @Qualifier("OTM_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("OTM_TeamRepository")
    TeamRepository teamRepository;

    @Test
    @DisplayName("일대다 단방향 멤버 추가 성공")
    public void addMemberToTeamSuccess(){
        Member member = new Member("gakpo");
        memberRepository.save(member);

        Team team = new Team("liverpool");
        team.addMember(member);
        teamRepository.save(team);

        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();

        Assertions.assertThat(team.getMembers().contains(savedMember));
    }

    @Test
    @DisplayName("일대다 단방향 멤버 추가 실패")
    public void addMemberToTeamFail(){

    }
}