package com.example.jpatest.ManyToOne.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MTO_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
