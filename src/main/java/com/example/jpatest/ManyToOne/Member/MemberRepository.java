package com.example.jpatest.ManyToOne.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MTO_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Integer>{
}
