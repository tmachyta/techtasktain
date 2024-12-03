package tain.task.service.user;

import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tain.task.dto.user.UserRegistrationRequest;
import tain.task.dto.user.UserResponseDto;
import tain.task.exception.RegistrationException;
import tain.task.mapper.UserMapper;
import tain.task.model.Role;
import tain.task.model.Role.RoleName;
import tain.task.model.User;
import tain.task.repository.user.UserRepository;
import tain.task.service.role.RoleService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password";
    private static final String REPEAT_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String INVALID_PASSWORD = "another_password";
    private static final RoleName ROLE = RoleName.USER;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSuccessfulRegistration() throws RegistrationException {

        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);
        request.setRepeatPassword(REPEAT_PASSWORD);

        UserResponseDto expectedResult = new UserResponseDto();
        expectedResult.setEmail(VALID_EMAIL);

        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(ENCODED_PASSWORD);

        Role userRole = new Role();
        userRole.setRoleName(ROLE);

        when(userMapper.toModel(request)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedResult);
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(roleService.getRoleByRoleName(ROLE)).thenReturn(userRole);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto result = userService.register(request);

        Assertions.assertEquals(expectedResult.getEmail(), result.getEmail());
    }

    @Test
    public void testRegistrationWithDuplicateEmail() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(VALID_EMAIL);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(RegistrationException.class, () -> userService.register(request));
    }

    @Test
    public void testRegistrationWithMismatchedPasswords() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);
        request.setRepeatPassword(INVALID_PASSWORD);

        Assertions.assertThrows(RegistrationException.class, () -> userService.register(request));
    }
}
