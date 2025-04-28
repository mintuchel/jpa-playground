package com.example.jpatest.EagerLoading.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("EAGER_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
