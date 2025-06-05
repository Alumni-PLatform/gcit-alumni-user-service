package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dto.ChangePasswordRequest;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User findByEmail(String email);

    List<User> findAllUsers(); // for admin controller

    void deleteUserById(String id);

    void updateUserRole(String id, Role newRole);

    void deactivateUser(String id);

    User findById(String id);

    void updateUserProfile(String email, User updatedUser);

    void changePassword(String email, ChangePasswordRequest request);

    void updateProfilePicture(String email, MultipartFile file);

    List<User> findByRole(Role role);
}
