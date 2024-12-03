package tain.task.service.video;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tain.task.dto.video.VideoContentDto;
import tain.task.dto.video.VideoDto;
import tain.task.dto.video.VideoRequestDto;
import tain.task.dto.video.VideoSearchParametersDto;
import tain.task.exception.EntityNotFoundException;
import tain.task.mapper.VideoMapper;
import tain.task.model.Video;
import tain.task.repository.video.VideoRepository;
import tain.task.repository.video.VideoSpecificationBuilder;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final VideoSpecificationBuilder videoSpecificationBuilder;

    @Override
    public VideoDto publishVideo(VideoRequestDto videoRequestDto) {
        Video video = videoMapper.toModel(videoRequestDto);
        return videoMapper.toDto(videoRepository.save(video));
    }

    @Override
    public List<VideoDto> getAll(Pageable pageable) {
        return videoRepository.findAll(pageable)
                .stream()
                .map(videoMapper::toDto)
                .toList();
    }

    @Override
    public VideoDto findById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find video by id " + id));
        video.setImpressions(video.getImpressions() + 1);
        videoRepository.save(video);
        return videoMapper.toDto(video);
    }

    @Override
    public void deleteById(Long id) {
        videoRepository.deleteById(id);
    }

    @Override
    public VideoDto update(Long id, VideoRequestDto videoRequestDto) {
        Video existedVideo = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find video by id " + id));
        existedVideo.setTitle(videoRequestDto.getTitle());
        existedVideo.setDirector(videoRequestDto.getDirector());
        existedVideo.setGenre(videoRequestDto.getGenre());
        return videoMapper.toDto(videoRepository.save(existedVideo));
    }

    @Override
    public List<VideoDto> search(VideoSearchParametersDto params) {
        Specification<Video> videoSpecification = videoSpecificationBuilder.build(params);
        return videoRepository.findAll(videoSpecification)
                .stream()
                .map(videoMapper::toDto)
                .toList();
    }

    @Override
    public VideoContentDto playContentVideoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find video by id " + id));
        video.setViews(video.getViews() + 1);
        videoRepository.save(video);
        return videoMapper.toVideoContent(video);
    }
}
