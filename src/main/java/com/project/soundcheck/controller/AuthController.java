package com.project.soundcheck.controller;

import com.project.soundcheck.dto.LoginRequest;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.User;
import com.project.soundcheck.service.UserService;
import com.project.soundcheck.service.VerificationService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    private final VerificationService verificationService;

    public record SendCodeRequest(String email) {}

    public record ConfirmCodeRequest(String email, String code) {}

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/verify-email/send")
    public ResponseEntity<Response> sendCode(@RequestBody SendCodeRequest req) {
        Response response = verificationService.sendVerificationCode(req.email());
        return ResponseEntity.status(response.getStatusCode())
            .body(response);
    }

    @PostMapping("/verify-email/confirm")
    public ResponseEntity<Response> confirm(@RequestBody ConfirmCodeRequest req) {
        Response response = verificationService.verifyCode(req.email(), req.code());
        return ResponseEntity.status(response.getStatusCode())
            .body(response);
    }
}
