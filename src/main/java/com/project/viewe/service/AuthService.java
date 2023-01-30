package com.project.viewe.service;

import com.project.viewe.dto.AuthenticationDto;
import com.project.viewe.dto.LoginDto;
import com.project.viewe.dto.RefreshTokenRequest;
import com.project.viewe.dto.UserRegistrationDto;
import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.Email;
import com.project.viewe.model.User;
import com.project.viewe.model.VerificationToken;
import com.project.viewe.repo.UserRepo;
import com.project.viewe.repo.VerificationRepo;
import com.project.viewe.security.JwtService;
import com.project.viewe.security.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final UserRepo userRepo;
    private final MailService mailService;
    private final VerificationRepo verificationRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public String signup(UserRegistrationDto userRegistrationDto) {
        if(userRepo.existsByUsername(userRegistrationDto.getUsername())){
            return "User has existed already";
        }else{
            User user = new User();
            user.setUsername(userRegistrationDto.getUsername());
            user.setEmail(userRegistrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
            user.setCreated(Instant.now());
            user.setEnable(false);
            userRepo.save(user);
            String token = generateVerificationToken(user);
            mailService.sendMail(new Email("Please activate your account ", user.getEmail(),
                    "Thank you Thank you for signing up to WeWatch, " +
                            "please click on the below url to activate your account : " +
                            "http://localhost:8080/api/auth/accountVerification/" + token));
            return "User Registration Successful";
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationRepo.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationRepo.findByToken(token);
        enableUser(verificationToken.orElseThrow(() -> new ProjectException("Cannot find Token")));
    }

    private void enableUser(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepo.findByUsername(username).
                orElseThrow(() -> new ProjectException("Cannot found user by username: " + username));
        user.setEnable(true);
        userRepo.save(user);
    }

    public AuthenticationDto login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtService.generateToken(authenticate);
        return AuthenticationDto.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                .username(loginDto.getUsername())
                .build();
    }

    public AuthenticationDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtService.generateTokenWithUsername(refreshTokenRequest.getUsername());

        return AuthenticationDto.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

}
