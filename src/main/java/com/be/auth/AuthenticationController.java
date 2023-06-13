package com.be.auth;

import com.be.config.LogoutService;
import com.be.dto.request.CustomerRegisterRequest;
import com.be.dto.response.RefreshTokenResponse;
import com.be.user.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService service;
    private final LogoutService logoutService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerRegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createEmployee(@RequestBody UserEntity request) {
        service.createAdmin(request);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        RefreshTokenResponse refreshTokenResponse = service.refreshToken(request, response);
        return ResponseEntity.ok(refreshTokenResponse);
    }

}
