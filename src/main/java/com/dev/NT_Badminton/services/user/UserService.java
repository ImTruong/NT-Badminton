package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.request.*;
import com.dev.NT_Badminton.dto.response.UserContactResponse;
import com.dev.NT_Badminton.dto.response.UserDetailResponse;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.users.AppUser;

import java.util.List;
import java.util.Map;

public interface UserService {

    String login(LoginRequest loginRequest);

    AppUser getUserFromSecurityContext();

    AppUser register(RegisterRequest registerRequest) throws Exception;

    boolean updatePassword(UpdateUserPasswordRequest updateUserPasswordRequest);

    boolean updateProfile(UpdateUserProfileRequest updateUserProfileRequest) throws Exception;

    UserDetailResponse getUserDetail();

    List<Map<String, Object>> getAllGenders();

    List<Map<String, Object>> getAllCitiesAndDistricts();

    List<UserContactResponse> getUserContacts();

    Contact addContact(UserContactRequest userContactRequest);

    void deleteContact(int contactId);

    void switchMainContact(int contactId);

}
