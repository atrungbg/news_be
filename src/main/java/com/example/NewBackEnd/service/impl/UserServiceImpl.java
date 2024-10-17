package com.example.NewBackEnd.service.impl;

import com.example.NewBackEnd.constant.ConstError;
import com.example.NewBackEnd.constant.ConstStatus;
import com.example.NewBackEnd.dto.request.user.CreateUserRequest;
import com.example.NewBackEnd.dto.request.user.LoginRequest;
import com.example.NewBackEnd.dto.request.user.UpdateUserRequest;
import com.example.NewBackEnd.dto.response.JwtAuthenticationResponse;
import com.example.NewBackEnd.dto.response.user.UserResponse;
import com.example.NewBackEnd.entity.User;
import com.example.NewBackEnd.enums.ErrorCode;
import com.example.NewBackEnd.enums.Role;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.repository.UserRepository;
import com.example.NewBackEnd.service.IJWTService;
import com.example.NewBackEnd.service.IUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IJWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${google.client.id}")
    private String googleClientId;

    @Override
    public UserResponse findById(UUID id) throws BaseException {
        try{
        Optional<User> user = userRepository.findById(id);
        boolean userIsExit = user.isPresent();
            if (!userIsExit) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }
            return modelMapper.map(user.get(), UserResponse.class);
        }catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    @Override
    public PagingModel<UserResponse> getAll(Integer page, Integer limit) throws BaseException {
        try{
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<UserResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<User> users = userRepository.findAllByOrderByCreatedDate(pageable);
            List<UserResponse> userResponses = users.stream().map(user
                    -> modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
            result.setListResult(userResponses);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);
            return result;
        }catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    private int totalItem() {
        return (int) userRepository.count();
    }

    private int totalItemWithStatusActive() {
        return  userRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel<UserResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try{
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<UserResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<User> users = userRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            List<UserResponse> userResponses = users.stream().map(user
                    -> modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
            result.setListResult(userResponses);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        }catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException {
        User user = findByEmailOrUserName(loginRequest.getEmailOrUsername());
        if (user.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "User is disabled", ErrorCode.ERROR_500.getMessage());
        }
//        if (!user.getRole().getName().equals(loginRequest.getLoginWithRole())) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), "Role is not match", ErrorCode.ERROR_500.getMessage());
//        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return MappingjwtAuthenticationRespone(user);
    }

    @Override
    public JwtAuthenticationResponse create(CreateUserRequest createUserRequest) throws BaseException {
        try{
            if (userRepository.findByUsername(createUserRequest.getUsername()).isPresent()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USERNAME_EXISTED, ErrorCode.ERROR_500.getMessage());
            }
            if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.EMAIL_EXISTED, ErrorCode.ERROR_500.getMessage());
            }
            User user = new User();
            user.setUsername(createUserRequest.getUsername());
            user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
            user.setStatus(ConstStatus.ACTIVE_STATUS);
            user.setEmail(createUserRequest.getEmail());
            user.setPhoneNumber(createUserRequest.getPhoneNumber());
            user.setRole(createUserRequest.getRoleName());
            userRepository.save(user);
            return MappingjwtAuthenticationRespone(user);
        }catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    private JwtAuthenticationResponse MappingjwtAuthenticationRespone(User user) throws BaseException {
        JwtAuthenticationResponse jwtAuthenticationRespone = new JwtAuthenticationResponse();

        jwtAuthenticationRespone.setId(user.getId());
        jwtAuthenticationRespone.setUsername(user.getUsername());
        jwtAuthenticationRespone.setStatus(user.getStatus());
        jwtAuthenticationRespone.setEmail(user.getEmail());
        jwtAuthenticationRespone.setRoleName(user.getRole());
        jwtAuthenticationRespone.setFullName(user.getFullName());
        jwtAuthenticationRespone.setAddress(user.getAddress());

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        jwtAuthenticationRespone.setToken(jwt);
        jwtAuthenticationRespone.setRefreshToken(refreshToken);
        return jwtAuthenticationRespone;
    }

    private User findByEmailOrUserName(String emailOrUserName) throws BaseException {
        try {
            Optional<User> userById;
            userById = userRepository.findByEmail(emailOrUserName);
            if (userById.isEmpty()) {
                userById = userRepository.findByUsername(emailOrUserName);
            }
            boolean isAccountExist = userById.isPresent();
            if (!isAccountExist) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }
            return userById.get();
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest) throws BaseException {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }
            User user = userOptional.get();

            if (updateUserRequest.getUsername() != null) {
                user.setUsername(updateUserRequest.getUsername());
            }

            if (updateUserRequest.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            }
            if (updateUserRequest.getEmail() != null) {
                user.setEmail(updateUserRequest.getEmail());
            }
            if (updateUserRequest.getPhoneNumber() != null) {
                user.setPhoneNumber(updateUserRequest.getPhoneNumber());
            }
            if (updateUserRequest.getRoleName() != null) {
                user.setRole(updateUserRequest.getRoleName());
            }
            if (updateUserRequest.getFullName() != null) {
                user.setFullName(updateUserRequest.getFullName());
            }
            if (updateUserRequest.getAddress() != null) {
                user.setAddress(updateUserRequest.getAddress());
            }

            userRepository.save(user);
            return modelMapper.map(user, UserResponse.class);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public UserResponse loginGoogle(String token) throws BaseException {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), "Invalid Google token", "Token không hợp lệ");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setFullName(name);
                        newUser.setRole(Role.USER.name());
                        return userRepository.save(newUser);
                    });

            return modelMapper.map(user, UserResponse.class);

        }catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

}

