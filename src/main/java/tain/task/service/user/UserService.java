package tain.task.service.user;

import tain.task.dto.user.UserRegistrationRequest;
import tain.task.dto.user.UserResponseDto;
import tain.task.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequest request) throws RegistrationException;
}
