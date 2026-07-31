package in.ankitboot.librarymanagment.controller;

import in.ankitboot.librarymanagment.dto.LoginRequest;
import in.ankitboot.librarymanagment.dto.LoginResponse;
import in.ankitboot.librarymanagment.dto.RegisterRequest;
import in.ankitboot.librarymanagment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid  @RequestBody  RegisterRequest request) {
        String result = authService.register(request);
        return ResponseEntity.ok(result); //ResponseEntity.ok(result);
        // ek hi step me status + body dono set
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody  LoginRequest request) {

        LoginResponse response = authService.login(request);
         return ResponseEntity.ok(response);
    }
}
