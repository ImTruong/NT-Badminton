package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.request.LoginRequest;
import com.dev.NT_Badminton.entities.users.AppUser;

public interface UserService {

    String login(LoginRequest loginRequest);

    AppUser getUserFromSecurityContext();

}
