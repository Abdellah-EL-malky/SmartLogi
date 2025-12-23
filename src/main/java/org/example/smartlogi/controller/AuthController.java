package org.example.smartlogi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.AuthResponse;
import org.example.smartlogi.dto.LoginRequest;
import org.example.smartlogi.dto.RegisterRequest;
import org.example.smartlogi.entity.User;
import org.example.smartlogi.enums.UserRole;
import org.example.smartlogi.security.jwt.JwtUtil;
import org.example.smartlogi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(userDetails);

            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .username(userDetails.getUsername())
                    .role(userDetails.getAuthorities().iterator().next().getAuthority())
                    .expiresIn(jwtExpiration / 1000)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .token(null)
                            .username(null)
                            .role(null)
                            .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            Role role = roleRepository.findByName(registerRequest.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + registerRequest.getRole()));

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setEmail(registerRequest.getEmail());
            user.setNom(registerRequest.getNom());
            user.setPrenom(registerRequest.getPrenom());
            user.setTelephone(registerRequest.getTelephone());
            user.setAdresse(registerRequest.getAdresse());
            user.setRole(role);
            user.setEnabled(true);

            User createdUser = userService.createUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully with username: " + createdUser.getUsername());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Authentication is working!");
    }
}