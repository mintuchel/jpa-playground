package com.example.jpatest.BI_OneToMany.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BI_OTM_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
