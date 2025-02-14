package com.dev.NT_Badminton.services.contact;

import com.dev.NT_Badminton.dto.response.UserContactResponse;
import com.dev.NT_Badminton.entities.contacts.Contact;

import java.util.List;

public interface ContactService {

    Contact createContact(Contact contact);

    boolean checkPhoneNumberExistence(String phoneNumber);

    Contact getUserMainContact(int userId);

    Contact saveContact(Contact contact);

    List<Contact> getUserContactsByUserId(int userId);

    Contact getContactById(int id);

}
