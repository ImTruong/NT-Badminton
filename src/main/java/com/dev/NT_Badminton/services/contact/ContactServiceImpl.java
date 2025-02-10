package com.dev.NT_Badminton.services.contact;

import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.ContactType;
import com.dev.NT_Badminton.repositories.contact.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public boolean checkPhoneNumberExistence(String phoneNumber) {
        System.out.println(contactRepository.existsByPhoneAndType(phoneNumber, ContactType.MAIN.getTypeId()));
        return contactRepository.existsByPhoneAndType(phoneNumber, ContactType.MAIN.getTypeId());
    }

    @Override
    public Contact findUserMainContact(int userId) {
        Optional<Contact> optinonalContact = contactRepository.findByUserIdAndType(userId, ContactType.MAIN.getTypeId());
        if (optinonalContact.isPresent())
            return optinonalContact.get();
        else
            throw new EntityNotFoundException("Contact not found with given user id");
    }

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

}
