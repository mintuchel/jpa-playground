package com.example.jpatest.BI_ManyToOne.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BI_MTO_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
