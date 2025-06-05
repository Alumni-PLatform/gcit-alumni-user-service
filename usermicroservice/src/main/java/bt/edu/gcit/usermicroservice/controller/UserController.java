package bt.edu.gcit.usermicroservice.controller;

import bt.edu.gcit.usermicroservice.dto.ChangePasswordRequest;
import bt.edu.gcit.usermicroservice.dto.UserResponse;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 1. Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(new UserResponse(
                user.getId(), user.getFullName(), user.getEmail(), user.getRole()));
    }

    // ✅ 2. Update User Profile
    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody User updatedUser) {

        userService.updateUserProfile(userDetails.getUsername(), updatedUser);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    // ✅ 3. Change Password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request) {

        userService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok("Password changed successfully.");
    }
}
