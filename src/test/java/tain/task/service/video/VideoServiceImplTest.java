package tain.task.service.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tain.task.dto.video.VideoContentDto;
import tain.task.dto.video.VideoDto;
import tain.task.dto.video.VideoRequestDto;
import tain.task.dto.video.VideoSearchParametersDto;
import tain.task.exception.EntityNotFoundException;
import tain.task.mapper.VideoMapper;
import tain.task.model.Video;
import tain.task.repository.video.VideoRepository;
import tain.task.repository.video.VideoSpecificationBuilder;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {
    private static final Long VIDEO_ID = 1L;
    private static final Long NON_EXISTED_VIDEO_ID = 50L;
    private static final String OLD_NAME = "Old";
    private static final String NEW_NAME = "Updated";
    private static final int IMPRESSIONS = 100;
    private static final int VIEWS = 100;
    private static final String PATH = "1125ZZXLLLJ";
    private static final String DIRECTOR = "Director";

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoMapper videoMapper;

    @Mock
    private VideoSpecificationBuilder videoSpecificationBuilder;

    @InjectMocks
    private VideoServiceImpl videoService;

    @Test
    public void testSuccessfullyPublishVideo() {
        VideoRequestDto request = new VideoRequestDto();
        VideoDto videoDto = new VideoDto();
        Video videoToSave = new Video();

        when(videoMapper.toModel(request)).thenReturn(videoToSave);

        when(videoRepository.save(videoToSave))
                .thenReturn(videoToSave);

        when(videoMapper.toDto(videoToSave)).thenReturn(videoDto);

        VideoDto result = videoService.publishVideo(request);

        assertNotNull(result);
        assertEquals(videoDto, result);
    }

    @Test
    public void testGetAllVideos() {
        Video video = new Video();
        Pageable pageable = PageRequest.of(0, 10);
        List<Video> videos = List.of(new Video());
        List<VideoDto> expectedVideos = List.of(new VideoDto());
        Page<Video> page =
                new PageImpl<>(videos, pageable, videos.size());

        when(videoRepository.findAll(pageable)).thenReturn(page);

        when(videoMapper.toDto(video)).thenReturn(new VideoDto());

        List<VideoDto> result = videoService.getAll(pageable);

        Assertions.assertEquals(expectedVideos.size(), result.size());
    }

    @Test
    public void testLoadVideoById() {
        Video video = new Video();
        video.setId(VIDEO_ID);
        video.setImpressions(IMPRESSIONS);
        VideoDto videoDto = new VideoDto();
        videoDto.setId(VIDEO_ID);
        videoDto.setImpressions(IMPRESSIONS);

        when(videoRepository.findById(video.getId()))
                .thenReturn(Optional.of(video));

        when(videoMapper.toDto(video)).thenReturn(videoDto);

        VideoDto result = videoService.findById(VIDEO_ID);

        Assertions.assertEquals(video.getId(), result.getId());
    }

    @Test
    public void testFindVideoWithNonExistedId() {
        when(videoRepository.findById(NON_EXISTED_VIDEO_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> videoService.findById(NON_EXISTED_VIDEO_ID));
    }

    @Test
    public void testPlayVideoById() {
        Video video = new Video();
        video.setId(VIDEO_ID);
        video.setViews(VIEWS);
        video.setVideoRelativePath(PATH);
        VideoContentDto videoContentDto = new VideoContentDto();
        videoContentDto.setVideoRelativePath(PATH);

        when(videoRepository.findById(video.getId()))
                .thenReturn(Optional.of(video));

        when(videoMapper.toVideoContent(video)).thenReturn(videoContentDto);

        VideoContentDto result = videoService.playContentVideoById(VIDEO_ID);

        Assertions.assertEquals(video.getVideoRelativePath(), result.getVideoRelativePath());
    }

    @Test
    public void testDeleteVideoById() {
        videoService.deleteById(VIDEO_ID);

        when(videoRepository.findById(VIDEO_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> videoService.findById(VIDEO_ID));
    }

    @Test
    public void testUpdateTrainingProgramSuccessfully() {
        VideoRequestDto request = new VideoRequestDto();
        request.setTitle(NEW_NAME);

        VideoDto expectedResult = new VideoDto();
        expectedResult.setTitle(NEW_NAME);

        Video video = new Video();
        video.setTitle(OLD_NAME);

        when(videoRepository.findById(VIDEO_ID))
                .thenReturn(Optional.of(video));

        when(videoRepository.save(video)).thenReturn(video);

        when(videoMapper.toDto(video)).thenReturn(expectedResult);

        VideoDto updatedVideo =
                videoService.update(VIDEO_ID, request);

        Assertions.assertEquals(updatedVideo.getTitle(), expectedResult.getTitle());
    }

    @Test
    public void testSearchVideo() {
        String[] titles = {OLD_NAME};
        String[] directors = {DIRECTOR};
        VideoSearchParametersDto searchParams = new VideoSearchParametersDto(titles, directors);

        Specification<Video> specification = mock(Specification.class);
        when(videoSpecificationBuilder.build(searchParams)).thenReturn(specification);

        Video video = new Video();
        VideoDto videoDto = new VideoDto();
        List<Video> videos = List.of(video);
        List<VideoDto> expectedVideos = List.of(videoDto);

        when(videoRepository.findAll(specification)).thenReturn(videos);
        when(videoMapper.toDto(video)).thenReturn(videoDto);

        List<VideoDto> result = videoService.search(searchParams);

        Assertions.assertEquals(expectedVideos.size(), result.size());
        Assertions.assertEquals(expectedVideos, result);
    }
}
