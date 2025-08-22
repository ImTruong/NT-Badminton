package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.contact.UserContactRequest;
import com.dev.NT_Badminton.dto.request.user.*;
import com.dev.NT_Badminton.dto.response.user.UserContactResponse;
import com.dev.NT_Badminton.dto.response.user.UserDetailResponse;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.ContactType;
import com.dev.NT_Badminton.entities.role.constant.PermissionType;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.entities.users.constant.Gender;
import com.dev.NT_Badminton.exception.AuthenticationFailedException;
import com.dev.NT_Badminton.exception.PasswordMismatchException;
import com.dev.NT_Badminton.exception.ResourceAlreadyExistsException;
import com.dev.NT_Badminton.exception.UserNotAuthenticatedException;
import com.dev.NT_Badminton.repositories.user.UserRepository;
import com.dev.NT_Badminton.security.CustomUserDetails;
import com.dev.NT_Badminton.services.cloudinary.CloudinaryService;
import com.dev.NT_Badminton.services.contact.ContactService;
import com.dev.NT_Badminton.services.uploadFile.UploadFileService;
import com.dev.NT_Badminton.util.JwtUtil;
import com.dev.NT_Badminton.util.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactService contactService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Utils utils;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UploadFileService uploadFileService;

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

    @Transactional
    @Override
    public AppUser register(RegisterRequest registerRequest) throws Exception {
        if (userRepository.existsByEmailAndDeleted(registerRequest.getEmail(), false))
            throw new ResourceAlreadyExistsException("Email is already taken");
        if (contactService.checkPhoneNumberExistence(registerRequest.getPhone()))
            throw new ResourceAlreadyExistsException("Phone number is already taken");
        String userPassword = passwordEncoder.encode(registerRequest.getPassword());
        UploadFile avatar = uploadFileService.uploadFile(registerRequest.getAvatar(), "avatars");
        AppUser appUser = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(userPassword)
                .name(registerRequest.getFirstName() + " " + registerRequest.getLastName())
                .gender(Gender.fromValue(registerRequest.getGender()))
                .status(ActiveStatus.ACTIVE)
                .roleId(PermissionType.USER.getRoleId())
                .avatarId(avatar != null ? avatar.getId() : null)
                .build();
        appUser.setCode("USER-"+utils.randomString(8));
        appUser = userRepository.save(appUser);
        Contact contact = modelMapper.map(registerRequest, Contact.class);
        contact.setUserId(appUser.getId());
        contact.setType(ContactType.MAIN);
        contactService.saveContact(contact);
        return appUser;
    }

    @Transactional
    @Override
    public boolean updatePassword(UpdateUserPasswordRequest updateUserPasswordRequest) {
        AppUser user = getUserFromSecurityContext();
        if(!passwordEncoder.matches(updateUserPasswordRequest.getOldPassword(), user.getPassword()))
            throw new PasswordMismatchException("Current password is incorrect");
        if(!updateUserPasswordRequest.getNewPassword().equals(updateUserPasswordRequest.getConfirmPassword()))
            throw new PasswordMismatchException("New password and confirm password do not match");
        user.setPassword(passwordEncoder.encode(updateUserPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public boolean updateProfile(UpdateUserProfileRequest updateUserProfileRequest) throws Exception {
        AppUser user = getUserFromSecurityContext();
        if (updateUserProfileRequest.getAvatar() != null && !updateUserProfileRequest.getAvatar().isEmpty()){
            UploadFile avatar = null;
            List<String> validImageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg");
            if (!validImageTypes.contains(updateUserProfileRequest.getAvatar().getContentType())) {
                throw new IllegalArgumentException("Please send a valid image file (png, jpg, jpeg)");
            }
            Optional<UploadFile> oldAvatarFile = user.getAvatarId() == null ? Optional.empty() : uploadFileService.getUserAvatar(user.getAvatarId());
            if (oldAvatarFile.isPresent()){
                UploadFile oldAvatar = oldAvatarFile.get();
                uploadFileService.updateFile(updateUserProfileRequest.getAvatar(), oldAvatar);
            }
            else{
                avatar = uploadFileService.uploadFile(updateUserProfileRequest.getAvatar(), "avatars");
                user.setAvatarId(avatar.getId());
            }
        }
        modelMapper.map(updateUserProfileRequest, user);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetailResponse getUserDetail() {
        AppUser user = getUserFromSecurityContext();
        UserDetailResponse userDetail = modelMapper.map(user, UserDetailResponse.class);
        Optional<UploadFile> avatarFile = user.getAvatarId() == null ? null : uploadFileService.getUserAvatar(user.getAvatarId());
        userDetail.setAvatarUrl(avatarFile!=null&&avatarFile.isPresent() ? avatarFile.get().getOriginUrl() : null);
        return userDetail;
    }

    @Override
    public List<Map<String, Object>> getAllGenders() {
        return Arrays.stream(Gender.values())
                .map(gender -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", gender.toValue());
                    map.put("name", gender.name());
                    return map;
                })
                .collect(Collectors.toList());
    }

}
