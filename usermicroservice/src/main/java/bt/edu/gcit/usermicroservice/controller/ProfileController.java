package bt.edu.gcit.usermicroservice.controller;

import bt.edu.gcit.usermicroservice.dto.UserResponse;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(new UserResponse(
                user.getId(), user.getFullName(), user.getEmail(), user.getRole()));
    }

    @PutMapping("/upload-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("image") MultipartFile file) {

        String email = userDetails.getUsername();
        userService.updateProfilePicture(email, file);
        return ResponseEntity.ok("Profile picture uploaded successfully.");
    }

    // You can add profile update endpoints here later if needed
}
