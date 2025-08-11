package com.project.soundcheck.service.impl;

import com.project.soundcheck.dto.LoginRequest;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.UserService;
import com.project.soundcheck.utils.JWTUtils;
import com.project.soundcheck.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    @Override
    public Response register(User user) {
        Response response = new Response();

        try {
            if (user.getRole() == null || user.getRole().isBlank())
                user.setRole("USER");

            if (userRepository.existsByEmail(user.getEmail()))
                throw new CustomException(String.format("%s already exists", user.getEmail()));

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserToUserDTO(savedUser);

            response.setUserDTO(userDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error registering user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new CustomException("Email not found"));

            String token = jwtUtils.generateToken(user);

            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationDate("7 Days");
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error logging in " + e.getMessage());
        }
        return response;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public Response update(Long id, UserDTO userDTO) {
        Response response = new Response();

        try {
            User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found."));

            if (userDTO.getEmail() != null)
                user.setEmail(userDTO.getEmail());

            if (userDTO.getUsername() != null)
                user.setUsername(userDTO.getUsername());

            if (userDTO.getPassword() != null)
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            if (userDTO.getRole() != null)
                user.setRole(userDTO.getRole());

            userRepository.save(user);
            UserDTO userDTO1 = Utils.mapUserToUserDTO(user);

            response.setUserDTO(userDTO1);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> users = userRepository.findAll();

            List<UserDTO> userDTOS = Utils.mapUserListToUserDTOList(users);

            response.setUserList(userDTOS);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new CustomException("User not found"));

            userRepository.delete(user);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserId(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new CustomException("User not found"));

            UserDTO userDTO = Utils.mapUserToUserDTO(user);

            response.setUserDTO(userDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting user by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException("User not found"));

            UserDTO userDTO = Utils.mapUserToUserDTO(user);

            response.setUserDTO(userDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting info " + e.getMessage());
        }
        return response;
    }
}
