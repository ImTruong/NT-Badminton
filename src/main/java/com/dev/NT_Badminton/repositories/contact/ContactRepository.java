package com.dev.NT_Badminton.repositories.contact;

import com.dev.NT_Badminton.entities.contacts.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, ContactRepositoryCustom {
}
