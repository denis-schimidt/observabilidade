package com.schimidt.observability.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor // Required by JPA
@Getter
@ToString
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String street;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 2)
    private String state;

    @NotBlank
    @Column(name = "zip_code", length = 9, nullable = false)
    private String zipCode;

    @NotBlank
    @Column(length = 2)
    private String country;

    @Min(1)  @Max(9999)
    @Column(nullable = false)
    private Short number;

    private Address(Long id, String street, String city, String state, String zipCode, String country, Short number) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.number = number;
    }

    public static Address of(String street, String city, String state, String zipCode, String country, Short number) {
        return new Address(null, street, city, state, zipCode, country, number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;
        return zipCode.equals(address.zipCode) && number.equals(address.number);
    }

    @Override
    public int hashCode() {
        int result = zipCode.hashCode();
        result = 31 * result + number.hashCode();
        return result;
    }
}

