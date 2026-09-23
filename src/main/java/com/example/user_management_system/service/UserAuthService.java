package com.example.user_management_system.service;

import com.example.user_management_system.dto.request.RegisterRequest;
import com.example.user_management_system.dto.request.UserRequest;
import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.dto.response.UserProfileResponse;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.exception.InvalidLoginException;
import com.example.user_management_system.exception.UserNotFoundException;
import com.example.user_management_system.exception.UsernameAlreadyExistsException;
import com.example.user_management_system.repository.UserRepository;
import com.example.user_management_system.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserProfileResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDob(request.getDob());
        user.setStatus(User.Status.ACTIVE);

        userRepository.save(user);

        return toProfileResponse(user);
    }

    public LoginResponse login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidLoginException();
        }

        String token = jwtUtil.generateToken(user.getUsername(), "USER");
        return new LoginResponse(token, user.getUsername(), "USER");
    }

    public UserProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return toProfileResponse(user);
    }

    public UserProfileResponse updateProfile(String username, UserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        userRepository.save(user);
        return toProfileResponse(user);
    }

    private UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getDob(),
                user.getStatus(),
                user.getCreatedAt());
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        User user = userOptional.get();

        boolean isOldPasswordCorrect = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!isOldPasswordCorrect) {
            throw new InvalidLoginException();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
