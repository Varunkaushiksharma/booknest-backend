package com.example.demo.controller;

// import com.example.demo.repositories.UserRepository;
import com.example.demo.service.AuthService;
// import com.example.demo.service.UserService;
import com.example.demo.DTO.JwtResponseDTO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.SignUpDTO;
// import com.example.demo.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/users") // base API path
public class UserController {

  // @Autowired
  // private UserRepository userRepository;

  // @Autowired
  // private PasswordEncoder passwordEncoder;

  // @Autowired
  // private UserService userService;
  @Autowired
  private AuthService authService;

  // POST /api/users/signup - receive JSON user data
  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody SignUpDTO user) {
    // You can add checks here like user existence, validation etc.
    authService.signUp(user);
    
    return ResponseEntity.ok("User registered successfully");
  }
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO user) {
    // You can add checks here like user existence, validation etc.
    String token = authService.login(user);
    
    return ResponseEntity.ok(new JwtResponseDTO(token));
  }

  @GetMapping("/me")
public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(user);
}

  
}
