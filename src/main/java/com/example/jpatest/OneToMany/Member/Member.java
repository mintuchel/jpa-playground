package com.example.jpatest.OneToMany.Member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="OneToMany_Member")
@Getter
@Builder
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private int id;

    private String name;
}
