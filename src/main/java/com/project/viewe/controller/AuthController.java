package com.project.viewe.controller;

import com.project.viewe.dto.AuthenticationDto;
import com.project.viewe.dto.LoginDto;
import com.project.viewe.dto.RefreshTokenRequest;
import com.project.viewe.dto.UserRegistrationDto;
import com.project.viewe.service.AuthService;
import com.project.viewe.security.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> userRegistration(@RequestBody UserRegistrationDto userRegistrationDto) {
        String signUpMassage = authService.signup(userRegistrationDto);
        return new ResponseEntity<>(signUpMassage, OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token) {
        authService.verifyAccount(token);
        return ResponseEntity.status(OK).body("Account activated");
    }

    @PostMapping("/login")
    @ResponseStatus(OK)
    public AuthenticationDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/refresh/token")
    @ResponseStatus(OK)
    public AuthenticationDto refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deletedRefreshToken(refreshTokenRequest);
        return ResponseEntity.status(OK).body("Token deleted, logout successfully");
    }

}
