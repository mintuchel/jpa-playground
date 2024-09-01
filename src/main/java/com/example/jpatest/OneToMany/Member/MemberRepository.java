package com.example.jpatest.OneToMany.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("OTM_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
