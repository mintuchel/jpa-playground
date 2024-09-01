package com.example.jpatest.BI_OneToMany.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BI_OTM_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
