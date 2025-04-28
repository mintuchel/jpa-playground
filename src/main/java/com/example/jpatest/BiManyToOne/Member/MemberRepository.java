package com.example.jpatest.BiManyToOne.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BiMTO_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
