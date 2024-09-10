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

import java.util.List;

// DataJpaTest 는 DB 까지 안감
// 모두 영속성 컨텍스트 안에서 노는거임
// teamrepository.save(team) 하면 영속성 컨텍스트 안에 저장하고
// teamrepository.findbyId() 도 영속성 컨텍스트 안에서 찾는거임

// 영속성 컨텍스트가 변경을 감지하여 알아서 @Test 실행 도중에 실시간으로 영속성 컨텍스트 내 엔티티를 업데이트해줌
// 그래서 update 나 findById 같이 다시 변경되었을 entity를 재조회 할 필요가 없다

@DataJpaTest
@ActiveProfiles("test")
public class BI_ManyToOneTest {
    @Autowired
    @Qualifier("BI_MTO_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("BI_MTO_TeamRepository")
    TeamRepository teamRepository;

//    private int teamId;
//    private int memberId;
//
//    @BeforeEach
//    private void testSetUp(){
//        Team team = new Team("real madrid");
//
//        Member member1 = new Member("vini");
//        Member member2 = new Member("benzema");
//        Member member3 = new Member("rodrygo");
//
//        team.addMember(member1);
//        team.addMember(member2);
//        team.addMember(member3);
//
//        teamRepository.save(team);
//    }

    @Test
    @DisplayName("cascade로 인한 영속성 전이로 자동 저장 확인")
    void cascadeSaveInTeamSuccess(){
        // given
        Team team = new Team("real madrid");

        Member member1 = new Member("vini");
        Member member2 = new Member("benzema");
        Member member3 = new Member("rodrygo");

        // when
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);

        // 영속성 컨텍스트에 team 엔티티 저장
        // cascade all 이므로 member들도 영속성 컨텍스트에 저장됨
        teamRepository.save(team);

        // then
        Assertions.assertThat(team.getMembers().size()).isEqualTo(3);

        // member들을 영속성 컨텍스트에서 조회하면 memberrepository.save() 를 안했어도 cascade 덕에 조회가 됨!

        // 연관관계 편의 메서드 덕에 아래 assert가 통과됨
        // jpa 에서 양방향 연관관계를 동기화 하지 않기 때문에 무조건 편의메서드를 작성해야함
        Assertions.assertThat(member1.getTeam().getId()).isEqualTo(team.getId());
    }

    @Test
    @DisplayName("연관관계 편의 메서드 사용X 시 실패 확인")
    void saveInMemberFail(){
        // given
        Team team = new Team("realmadrid");
        teamRepository.save(team);

        Member member = new Member("mbappe");
        member.setTeam(team);
        memberRepository.save(member);

        // Member 쪽에서 Team이 team으로 저장되었는지 확인
        // setTeam 으로 추가하여 통과해야하는게 맞음
        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team.getId());

        // Team 쪽에서 Member 가 memberList 에 저장되었는지 확인
        // 연관관계 편의 메서드로 연관관계를 설정하지 않았기 때문에 에러가 떠야함
        Assertions.assertThat(team.getMembers().contains(member)).isFalse();
    }

    @Test
    @DisplayName("다대일 양방향 멤버 추가 성공 (연관관계 편의 메서드로 추가)")
    void cascadeInTeamSuccess(){
        // given
        Member member = new Member("palmer");
        Team team = new Team("chelsea");

        // when
        team.addMember(member);
        teamRepository.save(team); // 영속성 컨텍스트에 team 저장 -> cascade all 에 의해 member 에게도 영속성 전이되어 영속성 컨텍스트에 저장됨

        // then
        Assertions.assertThat(team.getMembers().contains(member)).isTrue();

        // Member 쪽에서 Team이 team으로 저장되었는지 확인
        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team.getId());
    }

    @Test
    @DisplayName("CasacdeType.ALL의 REMOVE 속성 사용 - 부모 엔티티(Team) 삭제 시 자녀 엔티티(Member) 자동 삭제")
    void casacadeInTeamRemoveSuccess(){
        // given
        Team team = new Team("arsenal");

        Member member1 = new Member("henry");
        Member member2 = new Member("pirez");

        team.addMember(member1);
        team.addMember(member2);

        teamRepository.save(team);

        // when
        // cascade 에 의해 Team 의 삭제가 연관된 Member 들에게도 전이되어 연관된 member들도 삭제됨
        teamRepository.delete(team);

        // then
        // team 삭제 시 member 도 다 같이 삭제됨 확인
        Assertions.assertThat(teamRepository.findAll()).hasSize(0);
        Assertions.assertThat(memberRepository.findAll()).hasSize(0);
    }
}
