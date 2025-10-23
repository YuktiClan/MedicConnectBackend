package com.medicconnect.repositories;

import com.medicconnect.models.Document;
import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Fetch all documents linked to a specific Person
    List<Document> findByPerson(Person person);

    // Fetch all documents linked to a specific Organization
    List<Document> findByOrganization(Organization organization);

    // Fetch only verified documents for a Person
    List<Document> findByPersonAndVerifiedTrue(Person person);

    // Fetch only verified documents for an Organization
    List<Document> findByOrganizationAndVerifiedTrue(Organization organization);

    // ---------------- Optimized ----------------
    // Fetch a single document by its unique documentId
    Optional<Document> findByDocumentId(String documentId);
}
