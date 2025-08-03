package com.project.soundcheck.service;

import com.project.soundcheck.dto.LoginRequest;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.model.User;

public interface UserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response update(Long id, UserDTO userDTO);

    Response getAllUsers();

    Response deleteUser(String userId);

    Response getUserId(String userId);

    Response getMyInfo(String email);
}
