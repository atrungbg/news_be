package com.example.NewBackEnd.service.impl;

import com.example.NewBackEnd.constant.ConstError;
import com.example.NewBackEnd.constant.ConstStatus;
import com.example.NewBackEnd.dto.request.user.LoginRequest;
import com.example.NewBackEnd.dto.response.JwtAuthenticationResponse;
import com.example.NewBackEnd.entity.User;
import com.example.NewBackEnd.enums.ErrorCode;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.repository.UserRepository;
import com.example.NewBackEnd.service.IJWTService;
import com.example.NewBackEnd.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IJWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

//    @Override
//    public UserResponse findById(UUID id) throws BaseException {
//        try{
//        Optional<User> user = userRepository.findById(id);
//        boolean userIsExit = user.isPresent();
//
//            if (!userIsExit) {
//                logger.warn("Account with id {} is not found", id);
//                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
//            }
//
//            return UserConverter.toResponse( user.get());
//        }catch (Exception baseException) {
//            if (baseException instanceof BaseException) {
//                throw baseException;
//            }
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//
//    }
//
//    @Override
//    public PagingModel<UserResponse> getAll(Integer page, Integer limit) throws BaseException {
//        try{
//            if(page == null || limit == null){
//                page = 1;
//                limit = 10;
//            }
//            PagingModel<UserResponse> result = new PagingModel<>();
//            result.setPage(page);
//            Pageable pageable = PageRequest.of(page - 1, limit);
//
//
//            List<User> users = userRepository.findAllByOrderByCreatedDate(pageable);
//            List<UserResponse> userResponses = users.stream().map(UserConverter::toResponse).toList();
//            result.setListResult(userResponses);
//
//            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
//            result.setLimit(limit);
//
//            return result;
//        }catch (Exception baseException) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//    private int totalItem() {
//        return (int) userRepository.count();
//    }
//    private int totalItemWithStatusActive() {
//        return  userRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
//    }
//
//    @Override
//    public PagingModel<UserResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
//        try{
//            if(page == null || limit == null){
//                page = 1;
//                limit = 10;
//            }
//            PagingModel<UserResponse> result = new PagingModel<>();
//            result.setPage(page);
//            Pageable pageable = PageRequest.of(page - 1, limit);
//
//
//            List<User> users = userRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
//            List<UserResponse> userResponses = users.stream().map(UserConverter::toResponse).toList();
//            result.setListResult(userResponses);
//
//            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
//            result.setLimit(limit);
//
//            return result;
//        }catch (Exception baseException) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException {
        User user = findByEmailOrUserName(loginRequest.getEmailOrUsername());
        // Check if the account is disabled
        if (user.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "User is disabled", ErrorCode.ERROR_500.getMessage());
        }
//        if (!user.getRole().getName().equals(loginRequest.getLoginWithRole())) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), "Role is not match", ErrorCode.ERROR_500.getMessage());
//        }

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return MappingjwtAuthenticationRespone(user);
    }

//    @Override
//    public JwtAuthenticationResponse create(CreateUserRequest createUserRequest) throws BaseException {
//        try{
//
//            if (userRepository.findByUsername(createUserRequest.getUsername()).isPresent()) {
//                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USERNAME_EXISTED, ErrorCode.ERROR_500.getMessage());
//            }
//            // check dupplicate email
//            if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
//                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.EMAIL_EXISTED, ErrorCode.ERROR_500.getMessage());
//            }
//
//            User user = new User();
//            user.setUsername(createUserRequest.getUsername());
//            user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
//            user.setStatus(ConstStatus.ACTIVE_STATUS);
//            user.setEmail(createUserRequest.getEmail());
//            user.setPhoneNumber(createUserRequest.getPhoneNumber());
//
//
//            Role role = RoleConverter.responseToEntity(roleService.findByName(createUserRequest.getRoleName()));
//
//            user.setRole(role);
//
//            userRepository.save(user);
//            return MappingjwtAuthenticationRespone(user);
//        }catch (Exception baseException) {
//            if (baseException instanceof BaseException) {
//                throw baseException;
//            }
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//
//    }
//
    private JwtAuthenticationResponse MappingjwtAuthenticationRespone(User user) throws BaseException {
        JwtAuthenticationResponse jwtAuthenticationRespone = new JwtAuthenticationResponse();

        jwtAuthenticationRespone.setId(user.getId());



        jwtAuthenticationRespone.setUsername(user.getUsername());

        jwtAuthenticationRespone.setStatus(user.getStatus());
        jwtAuthenticationRespone.setEmail(user.getEmail());
        jwtAuthenticationRespone.setRoleName(user.getRole().name());
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

            logger.info("Find user by email or username {}", emailOrUserName);

            Optional<User> userById;
            userById = userRepository.findByEmail(emailOrUserName);
            if (userById.isEmpty()) {
                userById = userRepository.findByUsername(emailOrUserName);
            }

            boolean isAccountExist = userById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with email or username {} is not found", emailOrUserName);
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
//    @Override
//    public UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest) throws BaseException {
//        try {
//            // Tìm người dùng theo ID
//            Optional<User> userOptional = userRepository.findById(userId);
//            if (userOptional.isEmpty()) {
//                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
//            }
//            User user = userOptional.get();
//
//            // Cập nhật các trường không null từ UpdateUserRequest
//            if (updateUserRequest.getUsername() != null) {
//                user.setUsername(updateUserRequest.getUsername());
//            }
//
//            if (updateUserRequest.getPassword() != null) {
//                user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword())); // Mã hóa mật khẩu
//            }
//            if (updateUserRequest.getEmail() != null) {
//                user.setEmail(updateUserRequest.getEmail());
//            }
//            if (updateUserRequest.getPhoneNumber() != null) {
//                user.setPhoneNumber(updateUserRequest.getPhoneNumber());
//            }
//            if (updateUserRequest.getRoleName() != null) {
//                Role role = RoleConverter.responseToEntity(roleService.findByName(updateUserRequest.getRoleName()));
//                user.setRole(role);
//            }
//            if (updateUserRequest.getFullName() != null) {
//                user.setFullName(updateUserRequest.getFullName());
//            }
//            if (updateUserRequest.getAddress() != null) {
//                user.setAddress(updateUserRequest.getAddress());
//            }
//
//            // Lưu lại thông tin người dùng đã cập nhật
//            userRepository.save(user);
//            return UserConverter.toResponse(user);
//        } catch (Exception baseException) {
//            if (baseException instanceof BaseException) {
//                throw baseException;
//            }
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }

        }

