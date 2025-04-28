package com.example.jpatest.FetchJoin.Team;

import com.example.jpatest.FetchJoin.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FJ_TeamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {

}
