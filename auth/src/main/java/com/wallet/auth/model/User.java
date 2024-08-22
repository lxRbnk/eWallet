package com.wallet.auth.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

}
