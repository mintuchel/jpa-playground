package com.example.jpatest.OneToMany.Member;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="OneToMany_Member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private int id;

    private String name;
}
