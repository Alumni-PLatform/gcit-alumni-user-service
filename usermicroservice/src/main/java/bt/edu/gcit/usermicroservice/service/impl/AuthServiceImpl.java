package bt.edu.gcit.usermicroservice.service.impl;

import bt.edu.gcit.usermicroservice.config.JwtTokenProvider;
import bt.edu.gcit.usermicroservice.dto.*;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.repository.UserRepository;
import bt.edu.gcit.usermicroservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import bt.edu.gcit.usermicroservice.entity.Role;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<String> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already registered");
        }

        // Role determination based on email
        Role role = determineRole(request.getEmail());

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role) // Use the Role enum here
                .build();

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    private Role determineRole(String email) {
        String yearPart = email.substring(0, 4);
        int year = Integer.parseInt(yearPart);

        if (year >= 1222) {
            return Role.ROLE_STUDENT;
        } else {
            return Role.ROLE_ALUMNI;
        }
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return ResponseEntity.ok(new JwtResponse(token, user.getRole().name(), "Login successful"));
    }
}
