package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dto.*;

import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> register(RegisterRequest request);

    ResponseEntity<JwtResponse> login(LoginRequest request);
}
