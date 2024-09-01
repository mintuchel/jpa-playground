package com.example.jpatest.OneToMany.Member;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name="OneToMany_Member")
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private int id;

    private String nickName;

    public Member(String nickName){
        this.nickName = nickName;
    }
}
