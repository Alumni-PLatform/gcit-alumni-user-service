package bt.edu.gcit.usermicroservice.dto;

public class JwtResponse {

    private String token;
    private String role;
    private String message;

    public JwtResponse(String token, String role, String message) {
        this.token = token;
        this.role = role;
        this.message = message;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}
