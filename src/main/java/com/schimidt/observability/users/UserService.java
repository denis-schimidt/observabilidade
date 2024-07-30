package com.schimidt.observability.users;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Observed(contextualName = "user-service")
@RequiredArgsConstructor
@Service
class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public List<Phone> listPhonesByIds(Set<Integer> phoneIds) {
        return userRepository.findPhonesByPhoneIds(phoneIds);
    }
}
