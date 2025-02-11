package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.request.LoginRequest;
import com.dev.NT_Badminton.dto.request.RegisterRequest;
import com.dev.NT_Badminton.dto.request.UpdateUserPasswordRequest;
import com.dev.NT_Badminton.dto.request.UpdateUserProfileRequest;
import com.dev.NT_Badminton.dto.response.UserDetailResponse;
import com.dev.NT_Badminton.entities.users.AppUser;

import java.io.IOException;

public interface UserService {

    String login(LoginRequest loginRequest);

    AppUser getUserFromSecurityContext();

    AppUser register(RegisterRequest registerRequest) throws Exception;

    boolean updatePassword(UpdateUserPasswordRequest updateUserPasswordRequest);

    boolean updateProfile(UpdateUserProfileRequest updateUserProfileRequest) throws Exception;

    UserDetailResponse getUserDetail();

}
