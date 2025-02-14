package com.dev.NT_Badminton.services.user;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.*;
import com.dev.NT_Badminton.dto.response.UserContactResponse;
import com.dev.NT_Badminton.dto.response.UserDetailResponse;
import com.dev.NT_Badminton.entities.contacts.CityDistrictPair;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.contacts.ContactType;
import com.dev.NT_Badminton.entities.role.Role;
import com.dev.NT_Badminton.entities.role.constant.PermissionType;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.entities.upload_file.constant.UploadFileType;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
        UploadFile avatar = createAvatar(registerRequest.getAvatar());
        AppUser appUser = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(userPassword)
                .name(registerRequest.getFirstName() + " " + registerRequest.getLastName())
                .birthday(registerRequest.getBirthday())
                .gender(Gender.fromValue(registerRequest.getGender()))
                .status(ActiveStatus.ACTIVE)
                .roleId(PermissionType.USER.getRoleId())
                .avatarId(avatar != null ? avatar.getId() : null)
                .build();
        appUser.setCode("USER-"+utils.randomString(8));
        appUser = userRepository.save(appUser);
        Contact contact = modelMapper.map(registerRequest, Contact.class);
        contact.setUserId(appUser.getId());
        contact.setType(ContactType.MAIN.getTypeId());
        contactService.createContact(contact);
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
            Optional<UploadFile> oldAvatarFile = uploadFileService.getUserAvatar(user.getAvatarId());
            if (oldAvatarFile.isPresent()){
                UploadFile oldAvatar = oldAvatarFile.get();
                Map<String,String> newImage = cloudinaryService.updateFile(oldAvatar.getPublicId(), updateUserProfileRequest.getAvatar());
                Map details = cloudinaryService.getFileDetails(newImage.get("public_id"));
                oldAvatar.setOriginUrl(details.get("url").toString());
                oldAvatar.setWidth((Integer) details.get("width"));
                oldAvatar.setHeight((Integer) details.get("height"));
                oldAvatar.setSize((Integer) details.get("bytes"));
                uploadFileService.insertFile(oldAvatar);
            }
            else{
                avatar = createAvatar(updateUserProfileRequest.getAvatar());
                user.setAvatarId(avatar.getId());
            }
        }
        Contact contact = contactService.getUserMainContact(user.getId());
        modelMapper.map(updateUserProfileRequest, contact);
        contactService.saveContact(contact);
        modelMapper.map(updateUserProfileRequest, user);
        userRepository.save(user);
        return true;
    }

    private UploadFile createAvatar(MultipartFile avatar) throws Exception {
        if (avatar != null && !avatar.isEmpty()) {
            // Cho phép nhiều định dạng (png, jpg, jpeg)
            List<String> validImageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg");
            if (!validImageTypes.contains(avatar.getContentType())) {
                throw new IllegalArgumentException("Please send a valid image file (png, jpg, jpeg)");
            }
            Map<String, String> uploadResult = cloudinaryService.uploadFile(avatar, "avatars");

            Map details = cloudinaryService.getFileDetails(uploadResult.get("publicId"));

            UploadFile uploadFile =
                    UploadFile.builder()
                            .originUrl(uploadResult.get("url"))
                            .type(UploadFileType.IMAGE)
                            .width((Integer) details.get("width"))
                            .height((Integer) details.get("height"))
                            .size((Integer) details.get("bytes"))
                            .publicId( (String) details.get("publicId"))
                            .build();
            return uploadFileService.insertFile(uploadFile);
        }
        return null;
    }

    @Override
    public UserDetailResponse getUserDetail() {
        AppUser user = getUserFromSecurityContext();
        Contact contact = contactService.getUserMainContact(user.getId());
        UserDetailResponse userDetail = modelMapper.map(user, UserDetailResponse.class);
        modelMapper.map(contact, userDetail);
        Optional<UploadFile> avatarFile = uploadFileService.getUserAvatar(user.getAvatarId());
        userDetail.setAvatarUrl(avatarFile.isPresent() ? avatarFile.get().getOriginUrl() : null);
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

    @Override
    public List<Map<String, Object>> getAllCitiesAndDistricts() {
        return Arrays.stream(CityDistrictPair.values())
                .map(cityDistrictPair -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", cityDistrictPair.getId());
                    map.put("name", cityDistrictPair.name());
                    map.put("districts", cityDistrictPair.getDistricts());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserContactResponse> getUserContacts() {
        AppUser user = getUserFromSecurityContext();
        List<Contact> contacts = contactService.getUserContactsByUserId(user.getId());
        return contacts.stream()
                .map(contact -> {
                    UserContactResponse responseContact = modelMapper.map(contact, UserContactResponse.class);
                    responseContact.setType(ContactType.fromTypeId(contact.getType()).name());
                    return responseContact;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Contact addContact(UserContactRequest userContactRequest) {
        AppUser user = getUserFromSecurityContext();
        Contact contact = modelMapper.map(userContactRequest, Contact.class);
        contact.setType(ContactType.SUB.getTypeId());
        if(userContactRequest.getType().equals("MAIN")){
            Contact mainContact = contactService.getUserMainContact(user.getId());
            mainContact.setType(ContactType.SUB.getTypeId());
            contactService.saveContact(mainContact);
        }
        else if(!userContactRequest.getType().equals("SUB"))
            throw new IllegalArgumentException("Invalid contact type");
        contact.setType(ContactType.fromValue(userContactRequest.getType()).getTypeId());
        contact.setUserId(user.getId());
        return contactService.createContact(contact);
    }

    @Transactional
    @Override
    public void deleteContact(int contactId) {
        AppUser user = getUserFromSecurityContext();
        Contact contact = contactService.getContactById(contactId);
        if(contact.getUserId() != user.getId())
            throw new IllegalArgumentException("You are not allowed to delete this contact");
        if(contact.getType()==ContactType.MAIN.getTypeId())
            throw new IllegalArgumentException("You need to switch main contact before deleting this contact");
        contact.setDeleted(true);
        contactService.saveContact(contact);
    }

    @Transactional
    @Override
    public void switchMainContact(int contactId) {
        AppUser user = getUserFromSecurityContext();
        Contact contact = contactService.getContactById(contactId);
        if(contact.getUserId() != user.getId())
            throw new IllegalArgumentException("You are not allowed to switch this contact");
        Contact mainContact = contactService.getUserMainContact(user.getId());
        mainContact.setType(ContactType.SUB.getTypeId());
        contact.setType(ContactType.MAIN.getTypeId());
        contactService.saveContact(mainContact);
        contactService.saveContact(contact);
    }

}
