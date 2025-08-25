package com.dev.NT_Badminton.services.contact;

import com.dev.NT_Badminton.dto.request.contact.UserContactRequest;
import com.dev.NT_Badminton.dto.response.contact.locationsResponse.LocationsResponse;
import com.dev.NT_Badminton.dto.response.contact.userContactResponse.UserContactResponse;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.ContactType;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.contact.ContactRepository;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean checkPhoneNumberExistence(String phoneNumber) {
        return contactRepository.existsByPhoneAndType(phoneNumber, ContactType.MAIN);
    }

    @Override
    public Contact getUserMainContact(int userId) {
        Optional<Contact> optinonalContact = contactRepository.findByUserIdAndType(userId, ContactType.MAIN);
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
    public Contact getContactById(int id) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent())
            return optionalContact.get();
        else
            throw new EntityNotFoundException("Contact not found with given id");
    }

    @Override
    public List<UserContactResponse> getUserContacts() {
        AppUser user = userService.getUserFromSecurityContext();
        return contactRepository.findAllByUserId(user.getId());
    }

    @Transactional
    @Override
    public Contact addContact(UserContactRequest userContactRequest) {
        AppUser user = userService.getUserFromSecurityContext();
        Contact contact = modelMapper.map(userContactRequest, Contact.class);
        if (userContactRequest.getType() == ContactType.MAIN) {
            Contact mainContact = getUserMainContact(user.getId());
            mainContact.setType(ContactType.SUB);
            saveContact(mainContact);
        }
        else if (userContactRequest.getType() != ContactType.SUB)
            throw new IllegalArgumentException("Invalid contact type");
        contact.setUserId(user.getId());
        return saveContact(contact);
    }

    @Transactional
    @Override
    public void deleteContact(int contactId) {
        AppUser user = userService.getUserFromSecurityContext();
        Contact contact = getContactById(contactId);

        if (!contact.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not allowed to delete this contact");
        }
        if (contact.getType() == ContactType.MAIN) {
            throw new IllegalArgumentException("You need to switch main contact before deleting this contact");
        }

        contact.setDeleted(true);
        saveContact(contact);
    }

    @Transactional
    @Override
    public void switchMainContact(int contactId) {
        AppUser user = userService.getUserFromSecurityContext();
        Contact contact = getContactById(contactId);

        if (!contact.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not allowed to switch this contact");
        }
        Contact mainContact = getUserMainContact(user.getId());
        mainContact.setType(ContactType.SUB);
        contact.setType(ContactType.MAIN);

        saveContact(mainContact);
        saveContact(contact);
    }

    @Override
    public void updateContact(UserContactRequest modifyContactRequest) {
        AppUser user = userService.getUserFromSecurityContext();
        Contact contact = getContactById(modifyContactRequest.getContactId());
        if(contact.getUserId() != user.getId())
            throw new IllegalArgumentException("You are not allowed to update this contact");

        if (modifyContactRequest.getType() == ContactType.MAIN) {
            Contact mainContact = getUserMainContact(user.getId());
            mainContact.setType(ContactType.SUB);
            saveContact(mainContact);
        }
        else if (modifyContactRequest.getType() != ContactType.SUB)
            throw new IllegalArgumentException("Invalid contact type");
        modelMapper.map(modifyContactRequest, contact);
        saveContact(contact);
    }

    @Override
    public LocationsResponse getAllLocations() {
        return contactRepository.getAllLocations();
    }

}
