package com.example.jpatest.EagerLoading.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("EAGER_MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
