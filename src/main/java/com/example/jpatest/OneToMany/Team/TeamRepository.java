package com.example.jpatest.OneToMany.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("OTM_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
