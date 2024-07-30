package com.schimidt.observability.users;

import com.schimidt.observability.common.Password;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor // Required by JPA
@Getter
@Table(name = "users")
@NamedEntityGraph(name = "User.addresses", attributeNodes = @NamedAttributeNode("addresses"))
@Entity
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private Set<Address> addresses;

    private User(Long id, String name, String email, Password password, Set<Address> addresses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.addresses = addresses;
    }

    public static User of(String name, String email, Password password) {
        return new User(null, name, email, password, Set.of());
    }

    public static User of(String name, String email, Password password, Set<Address> addresses) {
        return new User(null, name, email, password, addresses);
    }
}

