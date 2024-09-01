package com.example.jpatest.BI_ManyToOne.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BI_MTO_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
