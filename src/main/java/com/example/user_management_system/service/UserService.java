package com.example.user_management_system.service;

import com.example.user_management_system.dto.UserRequest;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.exception.UserNotFoundException;
import com.example.user_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setDob(request.getDob());
        user.setStatus(request.getStatus() != null ? request.getStatus() : User.Status.ACTIVE);
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest request) {
        User user = getUserById(id);
        user.setName(request.getName());
        user.setDob(request.getDob());
        user.setStatus(request.getStatus() != null ? request.getStatus() : user.getStatus());
        return userRepository.save(user);
    }

    public User patchUser(Long id, UserRequest request) {
        User user = getUserById(id);
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}