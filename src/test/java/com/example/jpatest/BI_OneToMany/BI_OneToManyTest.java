package com.example.jpatest.BI_OneToMany;

import com.example.jpatest.BI_OneToMany.Member.MemberRepository;
import com.example.jpatest.BI_OneToMany.Team.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class BI_OneToManyTest {
    @Autowired
    @Qualifier("BI_OTM_MemberRepository")
    MemberRepository memberRepository;

    @Autowired
    @Qualifier("BI_OTM_TeamRepository")
    TeamRepository teamRepository;

    @Test
    @DisplayName("일대다 양방향 멤버 추가 성공")
    void addMemberToTeamSuccess(){

    }

    @Test
    @DisplayName("일대다 양방향 멤버 추가 실패")
    void addMemberToTeamFail(){

    }
}
