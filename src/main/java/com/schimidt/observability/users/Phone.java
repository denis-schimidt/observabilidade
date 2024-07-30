package com.schimidt.observability.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor // Required by JPA
@Getter
@Entity
@Table(name = "phones")
class Phone {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(length = 6, nullable = false)
    private PhoneType type;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    private Phone(String number, PhoneType type) {
        this.number = number;
        this.type = type;
    }

    static Phone home(String number) {
        return new Phone(number, PhoneType.HOME);
    }

    static Phone mobile(String number) {
        return new Phone(number, PhoneType.MOBILE);
    }

    static Phone work(String number) {
        return new Phone(number, PhoneType.WORK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;
        return number.equals(phone.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}

enum PhoneType {
    HOME, MOBILE, WORK
}
