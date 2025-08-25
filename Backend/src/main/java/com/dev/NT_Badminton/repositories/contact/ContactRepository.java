package com.dev.NT_Badminton.repositories.contact;

import com.dev.NT_Badminton.dto.response.contact.locationsResponse.LocationsResponse;
import com.dev.NT_Badminton.dto.response.contact.userContactResponse.UserContactResponse;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, ContactRepositoryCustom {

    boolean existsByPhoneAndType(String phoneNumber, ContactType type);

    Optional<Contact> findByUserIdAndType(int userId, ContactType type);

    Optional<Contact> findById(int id);

    @Query("""
        SELECT new com.dev.NT_Badminton.dto.response.contact.userContactResponse.UserContactResponse(
            c.id, c.firstName, c.lastName,
            c.phone, c.email, c.streetAddress, c.note,
            c.type, c.userId,
            new com.dev.NT_Badminton.dto.response.contact.userContactResponse.CityResponse(ci.id, ci.name),
            new com.dev.NT_Badminton.dto.response.contact.userContactResponse.DistrictResponse(d.id, d.name),
            new com.dev.NT_Badminton.dto.response.contact.userContactResponse.WardResponse(w.id, w.name)
        )
        FROM Contact c
        LEFT JOIN c.city ci
        LEFT JOIN c.district d
        LEFT JOIN c.ward w
        WHERE c.userId = ?1
    """)
    List<UserContactResponse> findAllByUserId(int userId);

}
