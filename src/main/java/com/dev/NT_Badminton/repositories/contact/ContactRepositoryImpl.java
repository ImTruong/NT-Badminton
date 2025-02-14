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
                        qContact.userId.eq(userId),
                        qContact.deleted.eq(false)
                )
                .fetch();
    }

    @Override
    public boolean existsByPhoneAndType(String phoneNumber, int type) {
        QContact qcontact = QContact.contact;
        return query()
                .selectOne()
                .from(qcontact)
                .where(
                        qcontact.phone.eq(phoneNumber),
                        qcontact.type.eq(type),
                        qcontact.deleted.eq(false)
                )
                .fetchFirst() != null;
    }

    @Override
    public Optional<Contact> findByUserIdAndType(int userId, int type) {
        Contact contact = query()
                .selectFrom(QContact.contact)
                .where(
                        QContact.contact.userId.eq(userId),
                        QContact.contact.type.eq(type),
                        QContact.contact.deleted.eq(false)
                )
                .fetchOne();
        return Optional.ofNullable(contact);
    }

    @Override
    public Optional<Contact> findById(int id) {
        Contact contact = query()
                .selectFrom(QContact.contact)
                .where(
                        QContact.contact.id.eq(id),
                        QContact.contact.deleted.eq(false)
                )
                .fetchOne();
        return Optional.ofNullable(contact);
    }

}
