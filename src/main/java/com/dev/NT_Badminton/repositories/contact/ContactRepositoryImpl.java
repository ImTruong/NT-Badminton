package com.dev.NT_Badminton.repositories.contact;

import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.QContact;
import com.dev.NT_Badminton.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public class ContactRepositoryImpl extends BaseRepository implements ContactRepositoryCustom {

    @Override
    public List<Contact> getContactsByUserId(int userId) {
        QContact qContact = QContact.contact;
        return query()
                .selectFrom(qContact)
                .where(
                        qContact.userId.eq(userId)
                )
                .fetch();
    }

    @Override
    public boolean existsByPhoneAndType(String phoneNumber, int type) {
        return query()
                .selectOne()
                .from(QContact.contact)
                .where(
                        QContact.contact.phone.eq(phoneNumber),
                        QContact.contact.type.eq(type)
                )
                .fetchFirst() != null;
    }

    @Override
    public Optional<Contact> findByUserIdAndType(int userId, int type) {
        Contact contact = query()
                .selectFrom(QContact.contact)
                .where(
                        QContact.contact.userId.eq(userId),
                        QContact.contact.type.eq(type)
                )
                .fetchOne();
        return Optional.ofNullable(contact);
    }


}
