package com.dev.NT_Badminton.repositories.contact;

import com.dev.NT_Badminton.entities.contacts.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepositoryCustom {

    List<Contact> getContactsByUserId(int userId);

    boolean existsByPhoneAndType(String phoneNumber,int type);

    Optional<Contact> findByUserIdAndType(int userId, int type);

    Optional<Contact> findById(int id);

}
