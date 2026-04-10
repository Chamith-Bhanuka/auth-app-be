package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user full name
    private String name;

    // must be unique email for login
    @Column(unique = true)
    private String email;

    // encrypted password stored here
    private String password;

    // role decides access level
    @Enumerated(EnumType.STRING)
    private Role role;
}
