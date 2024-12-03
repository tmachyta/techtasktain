package tain.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tain.task.config.MapperConfig;
import tain.task.dto.video.VideoContentDto;
import tain.task.dto.video.VideoDto;
import tain.task.dto.video.VideoRequestDto;
import tain.task.model.Video;

@Mapper(config = MapperConfig.class)
public interface VideoMapper {
    VideoDto toDto(Video video);

    @Mapping(target = "id", ignore = true)
    Video toModel(VideoRequestDto requestDto);

    VideoContentDto toVideoContent(Video video);
}
