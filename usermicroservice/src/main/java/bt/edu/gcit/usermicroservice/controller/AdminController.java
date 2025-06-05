package bt.edu.gcit.usermicroservice.controller;

import bt.edu.gcit.usermicroservice.dto.RoleUpdateRequest;
import bt.edu.gcit.usermicroservice.dto.UserResponse;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // --- Generic Utilities ---

    private List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(u -> new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());
    }

    // --- STUDENT CRUD ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students")
    public ResponseEntity<List<UserResponse>> getAllStudents() {
        return ResponseEntity.ok(toResponseList(userService.findByRole(Role.ROLE_STUDENT)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/{id}")
    public ResponseEntity<UserResponse> getStudentById(@PathVariable String id) {
        User user = userService.findById(id);
        if (!user.getRole().equals(Role.ROLE_STUDENT)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Student deleted.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/students/{id}/deactivate")
    public ResponseEntity<String> deactivateStudent(@PathVariable String id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok("Student deactivated.");
    }

    // --- ALUMNI CRUD ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alumni")
    public ResponseEntity<List<UserResponse>> getAllAlumni() {
        return ResponseEntity.ok(toResponseList(userService.findByRole(Role.ROLE_ALUMNI)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alumni/{id}")
    public ResponseEntity<UserResponse> getAlumniById(@PathVariable String id) {
        User user = userService.findById(id);
        if (!user.getRole().equals(Role.ROLE_ALUMNI)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/alumni/{id}")
    public ResponseEntity<String> deleteAlumni(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Alumni deleted.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/alumni/{id}/deactivate")
    public ResponseEntity<String> deactivateAlumni(@PathVariable String id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok("Alumni deactivated.");
    }

    // --- RECRUITER CRUD ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recruiters")
    public ResponseEntity<List<UserResponse>> getAllRecruiters() {
        return ResponseEntity.ok(toResponseList(userService.findByRole(Role.ROLE_RECRUITER)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recruiters/{id}")
    public ResponseEntity<UserResponse> getRecruiterById(@PathVariable String id) {
        User user = userService.findById(id);
        if (!user.getRole().equals(Role.ROLE_RECRUITER)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/recruiters/{id}")
    public ResponseEntity<String> deleteRecruiter(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Recruiter deleted.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/recruiters/{id}/deactivate")
    public ResponseEntity<String> deactivateRecruiter(@PathVariable String id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok("Recruiter deactivated.");
    }
}
