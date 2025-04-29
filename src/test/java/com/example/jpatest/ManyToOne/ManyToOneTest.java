package com.example.jpatest.ManyToOne;

import com.example.jpatest.ManyToOne.Member.Member;
import com.example.jpatest.ManyToOne.Member.MemberRepository;
import com.example.jpatest.ManyToOne.Team.Team;
import com.example.jpatest.ManyToOne.Team.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

// 다대일 단방향 테스트
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ManyToOneTest {

    @Autowired
    @Qualifier("MTO_TeamRepository")
    TeamRepository teamRepository;

    @Autowired
    @Qualifier("MTO_MemberRepository")
    MemberRepository memberRepository;

    @Test
    @DisplayName("다대일 단방향 Member 추가 성공")
    void addMemberToTeamSuccess() {
        Team team = Team.builder()
                .name("mancity")
                .build();

        teamRepository.save(team);

        // 연관관계의 주인이 Many 쪽이므로 Many 쪽에 One 추가
        Member member = Member.builder()
                .team(team)
                .name("gundogan")
                .build();

        memberRepository.save(member);

        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team.getId());
    }

    @Test
    @DisplayName("다대일 단방향 Member 추가 시 Team 조회 실패")
    void addMemberToTeamFail(){
        Team team = Team.builder()
                .name("mancity")
                .build();

        // 여기서는 teamRepository에 저장하지 않기
        // teamRepository.save(team);

        Member member = Member.builder()
                .team(team)
                .name("debruyne")
                .build();

        memberRepository.save(member);
        
        // 저장된 Member 를 통해 Team 조회
        Assertions.assertThat(member.getTeam().getId()).isEqualTo(team.getId());

        // Team이 비영속상태이고 DB에 저장되지 않았어도 위에 Assertions가 통과되는 이유는
        // Member에 Team에 대한 FK가 필요하므로 Team을 임시영속 상태로 만들어 외래키를 설정하기 때문
        // 따라서 Member의 teamId 외래키가 자동으로 설정되고 객체를 저장하지 않더라도 teamId 가 Member 테이블에 정상 반영됨
        // 즉 Member를 영속하려면 teamId가 필요하여 Team이 임시로 영속된 것이지
        // 실제로 영속상태(EntityManager에 저장되거나)이거나 DB에 저장된게 아님!!
        
        // team을 조회했을시에는 실제로 영속된게 아니므로 Exception을 던져야하는게 맞음
        assertThrows(NoSuchElementException.class, () -> {
            teamRepository.findById(team.getId()).orElseThrow();
        });
    }
}