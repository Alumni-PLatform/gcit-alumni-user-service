package bt.edu.gcit.usermicroservice.dto;

import lombok.Data;

@Data
public class RoleUpdateRequest {
    // Accept values like: STUDENT, ALUMNI, RECRUITER, ADMIN
    private String role;
}
