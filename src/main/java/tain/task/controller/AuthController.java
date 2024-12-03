package tain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tain.task.dto.user.UserLoginRequestDto;
import tain.task.dto.user.UserLoginResponseDto;
import tain.task.dto.user.UserRegistrationRequest;
import tain.task.dto.user.UserResponseDto;
import tain.task.exception.RegistrationException;
import tain.task.security.AuthenticationService;
import tain.task.service.user.UserService;

@Tag(name = "Authentication management", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login method to authenticate users")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register method to register users")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequest request)
            throws RegistrationException {
        return userService.register(request);
    }
}
