package com.be.auth;

import com.be.dto.response.RefreshTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.be.address.AddressEntity;
import com.be.address.AddressRepository;
import com.be.config.JwtService;
import com.be.dto.request.CustomerRegisterRequest;
import com.be.dto.request.EmployeeRegisterRequest;
import com.be.dto.response.CustomerLoginResponse;
import com.be.dto.response.LoginResponse;
import com.be.token.Token;
import com.be.token.TokenRepository;
import com.be.token.TokenType;
import com.be.user.Role;
import com.be.user.UserEntity;
import com.be.user.UserRepository;
import com.be.work.WorkEntity;
import com.be.work.WorkRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AddressRepository addressRepository;
    private final TokenRepository tokenRepository;

    private final WorkRepository workRepository;
    public AuthenticationResponse register(CustomerRegisterRequest request) {
        String email = request.getEmail();
        Optional<UserEntity> userExist = repository.findByEmail(email);
        String jwtToken = "";
        String refreshToken = "";
        if (userExist.isEmpty()) {
            var user = UserEntity.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.CUSTOMER)
                    .build();

            var savedUser = repository.save(user);
            jwtToken = jwtService.generateToken(user);
            refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            UserEntity userOptional = repository.findByEmail(user.getEmail()).orElseThrow();
            AddressEntity address = new AddressEntity(userOptional, request.getDepartmentNumber(), request.getRoomNumber());
            addressRepository.save(address);
        }

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public void createEmployee(EmployeeRegisterRequest request) {
        Optional<UserEntity> userExist = repository.findByEmail(request.getEmail());
        if (userExist.isEmpty()) {
            var user = UserEntity.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.EMPLOYEE)
                    .build();
            var savedUser = repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
            UserEntity userSaved = repository.findByEmail(request.getEmail()).orElseThrow();
            WorkEntity workEntity = new WorkEntity();
            workEntity.setEmployeeInfo(userSaved);
            workEntity.setWorkType(request.getWorkType());
            workRepository.save(workEntity);
        }
    }
    public void createAdmin(UserEntity request) {
        Optional<UserEntity> userExist = repository.findByEmail(request.getEmail());
        if (userExist.isEmpty()) {
            var user = UserEntity.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            var savedUser = repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
        }
    }


    public LoginResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        AuthenticationResponse jwt = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        LoginResponse loginResponse = new LoginResponse();
        CustomerLoginResponse response = new CustomerLoginResponse(user.getId(), user.getName(), user.getRole().toString(), user.getPhone());
        loginResponse.setUser(response);
        loginResponse.setJwtToken(jwt.getAccessToken());
        loginResponse.setRefreshToken(jwt.getRefreshToken());
        return loginResponse;
    }
    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public RefreshTokenResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                refreshTokenResponse.setRefreshToken(authResponse.getRefreshToken());
                refreshTokenResponse.setJwtToken(authResponse.getAccessToken());
            }
        }
        return refreshTokenResponse;
    }

}