package com.theduck.todoapp.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String ownerEmail);

    List<Person> findByEmailNot(String email);
}
