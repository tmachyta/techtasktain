package tain.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tain.task.config.MapperConfig;
import tain.task.dto.user.UserRegistrationRequest;
import tain.task.dto.user.UserResponseDto;
import tain.task.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toModel(UserRegistrationRequest request);

    UserResponseDto toUserResponse(User user);
}
