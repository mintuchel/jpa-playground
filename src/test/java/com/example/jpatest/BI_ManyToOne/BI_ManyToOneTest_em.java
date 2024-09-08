package com.example.jpatest.BI_ManyToOne;

import com.example.jpatest.BI_ManyToOne.Member.Member;
import com.example.jpatest.BI_ManyToOne.Team.Team;
import com.example.jpatest.BI_ManyToOne.Team.TeamRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class BI_ManyToOneTest_em {
    @Autowired
    private EntityManager em;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("다대일 양방향 멤버 추가 (EntityManager 사용)")
    void addMemberToTeam(){
        Team team = new Team("chelsea");
        em.persist(team);

        Member member = new Member("felix");
        member.setTeam(team);
        em.persist(member);

        em.flush();

        Team savedTeam = em.find(Team.class,team.getId());
        //Team savedTeam = teamRepository.findById(team.getId()).orElseThrow();
        Assertions.assertThat(savedTeam.getMembers().contains(member)).isTrue();

        Member savedMember = em.find(Member.class, member.getId());
        Assertions.assertThat(savedMember.getTeam().getId()).isEqualTo(savedTeam.getId());
    }
}