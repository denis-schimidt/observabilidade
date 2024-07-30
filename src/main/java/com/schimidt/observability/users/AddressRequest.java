package com.schimidt.observability.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

record AddressRequest(@NotBlank String street,
                      @NotBlank String city,
                      @NotBlank @Size(min = 2, max = 2) String state,
                      @NotBlank String zipCode,
                      @NotBlank @Size(min = 2, max = 2) String country,
                      @NotNull @Min(1) Short number) {

    Address toEntity() {
        return Address.of(street(), city(), state(), zipCode(), country(), number());
    }
}
