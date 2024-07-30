package com.schimidt.observability.users;

import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Observed(contextualName = "user-repository")
interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "User.addresses")
    @Override
    List<User> findAll();

    @EntityGraph(value = "User.addresses")
    @Override
    Optional<User> findById(Long aLong);

    @Query("SELECT p FROM Phone p WHERE p.id IN :phoneIds")
    List<Phone> findPhonesByPhoneIds(Set<Integer> phoneIds);
}
