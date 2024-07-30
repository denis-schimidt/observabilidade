package com.schimidt.observability.users;

import java.util.Set;
import java.util.stream.Collectors;

record UserResponse(Long id, String name, String email, Set<AddressResponse> addresses) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(),
                user.getAddresses().stream().map(AddressResponse::from).collect(Collectors.toSet()));
    }
}
