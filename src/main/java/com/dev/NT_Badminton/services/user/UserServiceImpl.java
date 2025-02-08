package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.request.LoginRequest;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.AuthenticationFailedException;
import com.dev.NT_Badminton.exception.UserNotAuthenticatedException;
import com.dev.NT_Badminton.repositories.user.UserRepository;
import com.dev.NT_Badminton.security.CustomUserDetails;
import com.dev.NT_Badminton.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            String token = jwtUtil.generateToken(authentication);
            return token;
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid username or password. Please try again.");
        }
    }

    @Override
    public AppUser getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (authentication == null || !authentication.isAuthenticated() || !(principal instanceof CustomUserDetails)) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }

        return ((CustomUserDetails) principal).getAppUser();
    }
}
