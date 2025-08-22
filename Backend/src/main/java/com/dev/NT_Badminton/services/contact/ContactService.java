package com.dev.NT_Badminton.services.contact;

import com.dev.NT_Badminton.dto.request.contact.UserContactRequest;
import com.dev.NT_Badminton.dto.response.contact.UserContactResponse;
import com.dev.NT_Badminton.entities.contacts.Contact;

import java.util.List;

public interface ContactService {

    boolean checkPhoneNumberExistence(String phoneNumber);

    Contact getUserMainContact(int userId);

    Contact saveContact(Contact contact);

    Contact getContactById(int id);

    List<UserContactResponse> getUserContacts();

    Contact addContact(UserContactRequest userContactRequest);

    void deleteContact(int contactId);

    void switchMainContact(int contactId);

    void updateContact(UserContactRequest userContactRequest);


}
