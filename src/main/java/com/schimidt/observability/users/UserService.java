package com.schimidt.observability.users;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
class UserService {

    private final UserRepository userRepository;

    @Observed(name = "user-service.create", contextualName = "creating-new-user")
    public User create(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> list() {
        return userRepository.findAll();
    }
}
