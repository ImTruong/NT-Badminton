package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.request.LoginRequest;
import com.dev.NT_Badminton.dto.request.RegisterRequest;
import com.dev.NT_Badminton.dto.request.UpdateUserPasswordRequest;
import com.dev.NT_Badminton.dto.request.UpdateUserProfileRequest;
import com.dev.NT_Badminton.entities.users.AppUser;

public interface UserService {

    String login(LoginRequest loginRequest);

    AppUser getUserFromSecurityContext();

    AppUser register(RegisterRequest registerRequest);

    boolean updatePassword(UpdateUserPasswordRequest updateUserPasswordRequest);

    boolean updateProfile(UpdateUserProfileRequest updateUserProfileRequest);

}
