package com.project.soundcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.soundcheck.dto.LoginRequest;
import com.project.soundcheck.dto.Response;
import com.project.soundcheck.dto.UserDTO;
import com.project.soundcheck.model.User;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.impl.UserServiceImpl;
import com.project.soundcheck.utils.JWTUtils;
import com.project.soundcheck.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtils jwtUtils;

    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, authenticationManager, jwtUtils);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole("USER");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testUser");
        userDTO.setRole("USER");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");        
    }

    @Test
    void register_return200() {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setUsername("newUser");
        newUser.setPassword("password");
        newUser.setRole(null);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("new@example.com");
        savedUser.setUsername("newUser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole("USER");

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(1L);
        expectedUserDTO.setEmail("new@example.com");
        expectedUserDTO.setUsername("newUser");
        expectedUserDTO.setRole("USER");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapUserToUserDTO(savedUser))
                .thenReturn(expectedUserDTO);
            
            Response response = userService.register(newUser);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getUserDTO()).isNotNull();
            assertThat(response.getUserDTO().getEmail()).isEqualTo("new@example.com");
            assertThat(response.getUserDTO().getRole()).isEqualTo("USER");

            verify(userRepository).existsByEmail("new@example.com");
            verify(passwordEncoder).encode("password");
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void login_return200() {
        String token = "jwt-token-1234";

        UsernamePasswordAuthenticationToken mockAuth = 
            new UsernamePasswordAuthenticationToken("test@example.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuth);

        when(userRepository.findByEmail("test@example.com"))
            .thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn(token);

        Response response = userService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Successful");
        assertThat(response.getToken()).isEqualTo(token);
        assertThat(response.getRole()).isEqualTo("USER");
        assertThat(response.getExpirationDate()).isEqualTo("7 Days");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(jwtUtils).generateToken(user);
    }

    @Test
    void getAllUsers_return200() {
        List<User> users = List.of(user);
        List<UserDTO> userDTOs = List.of(userDTO);

        when(userRepository.findAll())
            .thenReturn(users);
        
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapUserListToUserDTOList(users))
                .thenReturn(userDTOs);

            Response response = userService.getAllUsers();

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");
            assertThat(response.getUserList()).isNotNull();
            assertThat(response.getUserList().get(0).getEmail()).isEqualTo(userDTOs.get(0).getEmail());
        }
    }

    @Test
    void deleteUser_return200() {
        User userToDelete = new User();
        userToDelete.setId(2L);
        userToDelete.setEmail("email");
        userToDelete.setPassword("password");

        when(userRepository.findById(2L)).thenReturn(Optional.of(userToDelete));
        doNothing().when(userRepository).delete(any(User.class));

        Response response = userService.deleteUser("2");

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Successful");
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getUserDTO()).isNull();

        verify(userRepository).findById(2L);
        verify(userRepository).delete(userToDelete);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserId_return200() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapUserToUserDTO(user))
                .thenReturn(userDTO);

            Response response = userService.getUserId("1");

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");

            UserDTO result = response.getUserDTO();
            assertThat(result.getId()).isEqualTo(user.getId());
            assertThat(result.getEmail()).isEqualTo(user.getEmail());
            assertThat(result.getRole()).isEqualTo(user.getRole());
            assertThat(result.getUsername()).isEqualTo(user.getUsername());

            verify(userRepository).findById(1L);
            mockedUtils.verify(() -> Utils.mapUserToUserDTO(user));
        }
    }

    @Test
    void getMyInfo_return200() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapUserToUserDTO(user))
                .thenReturn(userDTO);

            Response response = userService.getMyInfo(email);

            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getMessage()).isEqualTo("Successful");

            UserDTO result = response.getUserDTO();
            assertThat(result.getId()).isEqualTo(user.getId());
            assertThat(result.getEmail()).isEqualTo(user.getEmail());
            assertThat(result.getRole()).isEqualTo(user.getRole());
            assertThat(result.getUsername()).isEqualTo(user.getUsername());

            verify(userRepository).findByEmail(email);
            mockedUtils.verify(() -> Utils.mapUserToUserDTO(user));
        }
    }
}
