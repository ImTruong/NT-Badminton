package com.dev.NT_Badminton.services.contact;

import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.ContactType;
import com.dev.NT_Badminton.repositories.contact.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public boolean checkPhoneNumberExistence(String phoneNumber) {
        System.out.println(contactRepository.existsByPhoneAndType(phoneNumber, ContactType.MAIN.getTypeId()));
        return contactRepository.existsByPhoneAndType(phoneNumber, ContactType.MAIN.getTypeId());
    }

    @Override
    public Contact getUserMainContact(int userId) {
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

    @Override
    public List<Contact> getUserContactsByUserId(int userId) {
        return contactRepository.getContactsByUserId(userId);
    }

    @Override
    public Contact getContactById(int id) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent())
            return optionalContact.get();
        else
            throw new EntityNotFoundException("Contact not found with given id");
    }

}
