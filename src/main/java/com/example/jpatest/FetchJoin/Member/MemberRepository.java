package com.example.jpatest.FetchJoin.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FJ_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from FJ_Member m left join m.team")
    List<Member> findMemberByTeamOuterJoin();

    @Query("select m from FJ_Member m left join fetch m.team")
    List<Member> findMemberByTeamFetchJoin();
}
