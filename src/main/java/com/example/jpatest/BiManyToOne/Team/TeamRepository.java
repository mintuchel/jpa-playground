package com.example.jpatest.BiManyToOne.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BiMTO_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
