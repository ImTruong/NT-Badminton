package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.request.user.*;
import com.dev.NT_Badminton.dto.response.user.UserDetailResponse;
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



}
