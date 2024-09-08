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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
        // cascade all 이므로 member들도 저장됨
        teamRepository.save(team);

        Assertions.assertThat(team.getMembers().size()).isEqualTo(3);

        Member savedMember = memberRepository.findById(member1.getId()).orElseThrow();
    }

    @Test
    @DisplayName("다대일 양방향 멤버 추가 성공")
    void addMemberToTeamSuccess(){
        Team team = new Team("chelsea");
        teamRepository.save(team); // 영속성 컨텍스트에 team 저장

        Member member = new Member("palmer");
        member.setTeam(team); // 주인인 쪽에 추가
        memberRepository.save(member); // 영속성 컨텍스트에 member 저장

        // 영속성 컨텍스트에서 team 찾고
        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();

        // team의 members에 member가 저장되어 있는지 확인
        // team.getMembers()를 호출하면 JPA가 이 쿼리를 생성
        // SELECT *
        //         FROM Member
        // WHERE team_id = ?
        Assertions.assertThat(savedTeam.getMembers().contains(member)).isTrue();

        // Member 쪽에서 Team이 team으로 저장되었는지 확인
        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();
        Assertions.assertThat(savedMember.getTeam().getId()).isEqualTo(savedTeam.getId());
    }

//    @Test
//    @DisplayName("다대일 양방향 멤버 추가 실패")
//    void addMemberToTeamFail(){
//        Member member = new Member("mbappe");
//        memberRepository.save(member);
//
//        Team team = new Team("realmadrid");
//        team.getMembers().add(member); // 주인이 아닌 쪽에 추가
//        teamRepository.save(team);
//
//        // Team 쪽에서 Member 가 memberList 에 저장되었는지 확인
//        Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
//        Assertions.assertThat(savedTeam.getMembers().contains(member)).isTrue();
//
//        // Member 쪽에서 Team이 team으로 저장되지 않았는지 확인
//        // 주인이 아닌 쪽에서 추가한 것이기 때문에 주인 쪽에는 반영이 안된다
//        // 그래서 주인의 Team을 조회하면 Null로 떠야한다
//        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();
//        Assertions.assertThat(member.getTeam()).isNull();
//    }
}
