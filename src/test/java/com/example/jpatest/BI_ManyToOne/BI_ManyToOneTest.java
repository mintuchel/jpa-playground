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
import org.springframework.test.context.ActiveProfiles;

// DataJpaTest 는 DB 까지 안감
// 모두 영속성 컨텍스트 안에서 노는거임
// teamrepository.save(team) 하면 영속성 컨텍스트 안에 저장하고
// teamrepository.findbyId() 도 영속성 컨텍스트 안에서 찾는거임
@DataJpaTest
@ActiveProfiles("test")
public class BI_ManyToOneTest {
    @Autowired
    @Qualifier("BI_MTO_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("BI_MTO_TeamRepository")
    TeamRepository teamRepository;

    @Test
    @DisplayName("CascadeType.ALL 영속성 전이 성공")
    void cascadeTypeALLSuccess(){

        Team team = new Team("real madrid");

        Member member1 = new Member("vini");
        Member member2 = new Member("benzema");
        Member member3 = new Member("rodrygo");

        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);

        // 영속성 컨텍스트에 team 엔티티 저장
        // cascade all 이므로 member들도 영속성 컨텍스트에 저장됨
        teamRepository.save(team);

        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
        Assertions.assertThat(team.getMembers().size()).isEqualTo(3);

        // member들을 영속성 컨텍스트에서 조회하면 memberrepository.save() 를 안했어도 cascade 덕에 조회가 됨!
        Member savedMember = memberRepository.findById(member1.getId()).orElseThrow();
        // 연관관계 편의 메서드 덕에 아래 assert가 통과됨
        // jpa 에서 양방향 연관관계를 동기화 하지 않기 때문에 무조건 편의메서드를 작성해야함
        Assertions.assertThat(savedMember.getTeam().getId()).isEqualTo(savedTeam.getId());
    }

    @Test
    @DisplayName("다대일 양방향 멤버 추가 성공")
    void addMemberToTeamSuccess(){
        Member member = new Member("palmer");

        Team team = new Team("chelsea");
        team.addMember(member);
        teamRepository.save(team); // 영속성 컨텍스트에 team 저장 -> cascade all 에 의해 member 에게도 영속성 전이되어 영속성 컨텍스트에 저장됨

        // 영속성 컨텍스트에서 team 찾고
        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();

        // team의 members에 member가 저장되어 있는지 확인
        // team.getMembers()를 호출하면 JPA가 이 쿼리를 생성
        // SELECT *
        //         FROM Member
        // WHERE team_id = ?
        Assertions.assertThat(savedTeam.getMembers().contains(member)).isTrue();

        // Member 쪽에서 Team이 team으로 저장되었는지 확인
        Assertions.assertThat(savedMember.getTeam().getId()).isEqualTo(team.getId());
    }

    @Test
    @DisplayName("다대일 양방향 멤버 추가 실패")
    void addMemberToTeamFail(){
        Team team = new Team("realmadrid");
        teamRepository.save(team);

        Member member = new Member("mbappe");
        member.setTeam(team);
        memberRepository.save(member);

        // Member 쪽에서 Team이 team으로 저장되었는지 확인
        // setTeam 으로 추가하여 통과해야하는게 맞음
        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();
        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team.getId());

        // Team 쪽에서 Member 가 memberList 에 저장되었는지 확인
        // 연관관계 편의 메서드로 연관관계를 설정하지 않았기 때문에 에러가 떠야함
        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
        Assertions.assertThat(savedTeam.getMembers().contains(member)).isFalse();
    }
}
