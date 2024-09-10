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
    @DisplayName("casacde로 Team 저장 시 Member 저장 성공")
    public void cascadeInTeamSaveSuccess(){
        // given
        Member member = new Member("gakpo");

        Team team = new Team("liverpool");
        team.addMember(member);

        // when
        // cascadeType.ALL로 team 저장할때 연관된 member도 모두 persist 되어 영속성 컨텍스트에 들어가게 됨
        teamRepository.save(team);

        // then
        // @OneToMany(cascade = CascadeType.ALL) 로 해서 이게 통과가 되는거임
        // 만약 Cascade 명시 안했으면 member는 자동저장 안됨!
        Assertions.assertThat(memberRepository.findAll()).hasSize(1);
        // Team 쪽에서 member가 memberList에 저장되었는지 확인
        Assertions.assertThat(team.getMembers().contains(member)).isTrue();
    }

    @Test
    @DisplayName("casacde로 Team 삭제 시 Member 삭제 성공")
    public void cascadeInTeamRemoveSuccess(){
        // given
        Member m1 = new Member("nunez");
        Member m2 = new Member("salah");
        Member m3 = new Member("diaz");

        Team liverpool = new Team("liverpool");
        liverpool.addMember(m1);
        liverpool.addMember(m2);
        liverpool.addMember(m3);
        teamRepository.save(liverpool);

        // when
        teamRepository.delete(liverpool);

        // then
        Assertions.assertThat(teamRepository.findAll()).hasSize(0); // cascade에 의해 부모 삭제 시 자식들도 모두 자동 삭제됨
        Assertions.assertThat(memberRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("orphanRemoval로 고아 Member 자동 삭제")
    public void orphanRemovalInTeamSuccess(){
        // given
        Member member = new Member("bruno");

        Team team = new Team("manunited");
        team.addMember(member);
        teamRepository.save(team);

        // when
        team.removeMember(member);

        // then
        Assertions.assertThat(teamRepository.findAll()).hasSize(1); 
        Assertions.assertThat(memberRepository.findAll()).hasSize(0); // orphanRemoval에 의해 부모와의 관계가 끊겨 고아로 판단되어 삭제됨
    }
}