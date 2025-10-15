package com.medicconnect.repositories;

import com.medicconnect.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Find person by email (used in AuthService)
    Optional<Person> findByEmail(String email);

    // Optional: only keep this if your Person entity has a "userId" field
    Optional<Person> findByUserId(String userId);
}
