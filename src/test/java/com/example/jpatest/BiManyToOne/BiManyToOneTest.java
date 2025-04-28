package com.example.jpatest.BiManyToOne;

import com.example.jpatest.BiManyToOne.Member.Member;
import com.example.jpatest.BiManyToOne.Member.MemberRepository;
import com.example.jpatest.BiManyToOne.Team.Team;
import com.example.jpatest.BiManyToOne.Team.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BiManyToOneTest {
    @Autowired
    @Qualifier("BiMTO_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("BiMTO_TeamRepository")
    TeamRepository teamRepository;

    @Test
    @DisplayName("cascade 영속성 전이로 Member 자동 저장 확인")
    void cascadeSaveInTeamSuccess(){
        // given
        Team team = Team.builder()
                .name("real madrid")
                .build();

        Member member1 = Member.builder()
                .name("vini")
                .build();

        Member member2 = Member.builder()
                .name("benzema")
                .build();

        Member member3 = Member.builder()
                .name("rodrygo")
                .build();

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
    @DisplayName("연관관계 편의 메서드 사용 Member 추가 성공")
    void cascadeInTeamSuccess(){
        // given
        Member member = Member.builder()
                .name("palmer")
                .build();

        Team team = Team.builder()
                .name("chelsea")
                .build();

        // when
        team.addMember(member);
        teamRepository.save(team); // 영속성 컨텍스트에 team 저장 -> cascade all 에 의해 member 에게도 영속성 전이되어 영속성 컨텍스트에 저장됨

        // then
        Assertions.assertThat(team.getMembers().contains(member)).isTrue();

        // Member 쪽에서 Team이 team으로 저장되었는지 확인
        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team.getId());
    }

    @Test
    @DisplayName("연관관계 편의 메서드 사용X Member 추가 실패")
    void saveInMemberFail(){
        // given
        Team team = Team.builder()
                .name("real madrid")
                .build();

        teamRepository.save(team);

        Member member = Member.builder()
                .name("mbappe")
                .build();

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
    @DisplayName("cascade의 remove 속성 사용 - Team 삭제 시 연관된 Member 자동 삭제")
    void casacadeInTeamRemoveSuccess(){
        // given
        Team team = Team.builder()
                .name("arsenal")
                .build();

        Member member1 = Member.builder()
                .name("henry")
                .build();

        Member member2 = Member.builder()
                .name("pirez")
                .build();

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

    @Test
    @DisplayName("orphanRemoval = true 속성에 의해 부모와의 관계 제거 시 자식 엔티티 삭제 성공")
    void orphanRemovalInTeamSuccess(){
        // given
        Member member = Member.builder()
                .name("henry")
                .build();

        Team team = Team.builder()
                .name("arsenal")
                .build();

        team.addMember(member);

        teamRepository.save(team); // cascade 에 의해 team 에 속해있는 member 까지 모두 저장됨

        // when
        // 연관관계 편의 메서드로 삭제 -> 두쪽에 모두 적용
        team.removeMember(member);

        // then
        List<Member> members = memberRepository.findAll();

        Assertions.assertThat(team.getMembers().isEmpty());
        Assertions.assertThat(members).hasSize(0); // member 엔티티에 delete 쿼리 나가는거 로그 찍으면 확인가능

        // 만약 Team에서 orphanRemoval = false로 되어있었다면
        // Assertions.assertThat(members).hasSize(0)은 fail이 남
        // Assertions.assertThat(members).hasSize(1)로 해야지 통과가 됨
        // 왜냐하면 orphanRemoval = false 면 관계가 끊어져서 고아가 된 member라고 해당 엔티티를 자동 삭제해주지는 않기 때문
    }

    @Test
    @DisplayName("Member의 Team 연관관계 교체 성공")
    void changeMemberTeamSuccess(){
        // given
        Member member = Member.builder()
                .name("mbappe")
                .build();

        Team team1 = Team.builder()
                .name("psg")
                .build();

        Team team2 = Team.builder()
                .name("real madrid")
                .build();

        team1.addMember(member);

        teamRepository.save(team1);
        teamRepository.save(team2);

        // when
        team1.removeMember(member);
        team2.addMember(member); // 이때 update 쿼리 하나 나감

        // then
        Assertions.assertThat(memberRepository.findAll()).hasSize(1);
        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team2.getId());
        Assertions.assertThat(team1.getMembers()).hasSize(0);
        Assertions.assertThat(team2.getMembers()).hasSize(1);
    }
}
