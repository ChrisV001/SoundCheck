package com.project.soundcheck.controller;

import com.project.soundcheck.dto.LoginRequest;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.model.User;
import com.project.soundcheck.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

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

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable String id) {
        Response response = userService.getUserId(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUserById(@PathVariable String id) {
        Response response = userService.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/get-my-info")
    public ResponseEntity<Response> getInfoAboutUser(@RequestParam String email) {
        Response response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }
}
