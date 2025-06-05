package bt.edu.gcit.usermicroservice.service.impl;

import bt.edu.gcit.usermicroservice.dto.ChangePasswordRequest;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.repository.UserRepository;
import bt.edu.gcit.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public User findById(String id) {
        Long userId = Long.parseLong(id);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(String id) {
        Long userId = Long.parseLong(id);
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUserRole(String id, Role newRole) {
        Long userId = Long.parseLong(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(String id) {
        Long userId = Long.parseLong(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public void updateUserProfile(String email, User updatedUser) {
        User existingUser = findByEmail(email);
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setBio(updatedUser.getBio());
        existingUser.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
        existingUser.setOrganizationName(updatedUser.getOrganizationName());
        userRepository.save(existingUser);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateProfilePicture(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads", filename);
            Files.createDirectories(uploadPath.getParent());
            Files.write(uploadPath, file.getBytes(), StandardOpenOption.CREATE);

            user.setProfilePictureUrl("/uploads/" + filename);
            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
