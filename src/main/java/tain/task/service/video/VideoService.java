package tain.task.service.video;

import java.util.List;
import org.springframework.data.domain.Pageable;
import tain.task.dto.video.VideoContentDto;
import tain.task.dto.video.VideoDto;
import tain.task.dto.video.VideoRequestDto;
import tain.task.dto.video.VideoSearchParametersDto;

public interface VideoService {
    VideoDto publishVideo(VideoRequestDto videoRequestDto);

    List<VideoDto> getAll(Pageable pageable);

    VideoDto findById(Long id);

    void deleteById(Long id);

    VideoDto update(Long id, VideoRequestDto videoRequestDto);

    List<VideoDto> search(VideoSearchParametersDto params);

    VideoContentDto playContentVideoById(Long id);
}
