package com.schimidt.observability.users;

import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;

@Observed(contextualName = "user-repository")
interface UserRepository extends JpaRepository<User, Long> {
}
