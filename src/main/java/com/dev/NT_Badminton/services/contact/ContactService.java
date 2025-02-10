package com.dev.NT_Badminton.services.contact;

import com.dev.NT_Badminton.entities.contacts.Contact;

public interface ContactService {

    Contact createContact(Contact contact);

    boolean checkPhoneNumberExistence(String phoneNumber);

    Contact findUserMainContact(int userId);

    Contact saveContact(Contact contact);

}
