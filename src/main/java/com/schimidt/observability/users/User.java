package com.schimidt.observability.users;

import com.schimidt.observability.common.Password;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "users")
@Entity
@NoArgsConstructor
@ToString
class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    private String name;

    @Column(unique = true, nullable = false)
    @Getter
    private String email;

    @Column(nullable = false)
    private Password password;

    private User(Long id, String name, String email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User of(String name, String email, Password password) {
        return new User(null, name, email, password);
    }
}
