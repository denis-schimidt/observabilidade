package com.schimidt.observability.users;

import com.schimidt.observability.security.PasswordEncryptor;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Timed("user-controller")
@RequiredArgsConstructor
@Controller
class UserController {

    private final UserService userService;

    private final PasswordEncryptor passwordEncryptor;

    @MutationMapping
    UserResponse newUser(@Argument(value = "user") @Valid UserRequest userRequest) {
        User newUser = userRequest.toEntity(passwordEncryptor);
        User userCreated = userService.create(newUser);

        return UserResponse.from(userCreated);
    }

    @QueryMapping
    Optional<UserResponse> userById(@Argument @Min(1) Long id) {
        return userService.findById(id)
                .map(UserResponse::from);
    }

//    @SchemaMapping
//    List<PhoneResponse> phones(UserResponse user) {
//        return userService.listPhonesByIds(Set.of(1,2))
//                .stream()
//                .map(PhoneResponse::from)
//                .toList();
//    }

    @QueryMapping
    List<UserResponse> users() {
        return userService.list()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}
