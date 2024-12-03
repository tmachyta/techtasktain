package tain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tain.task.dto.video.VideoContentDto;
import tain.task.dto.video.VideoDto;
import tain.task.dto.video.VideoRequestDto;
import tain.task.dto.video.VideoSearchParametersDto;
import tain.task.service.video.VideoService;

@Tag(name = "Video management", description = "Endpoints for managing videos")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/videos")
public class VideoController {
    private final VideoService videoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "Get all videos", description = "Get a list of all available videos")
    public List<VideoDto> findAll(@ParameterObject Pageable pageable) {
        return videoService.getAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get video by id", description = "Get available video by id")
    public VideoDto getVideoById(@PathVariable Long id) {
        return videoService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Save video to repository", description = "Save valid video to repository")
    public VideoDto publishVideo(@RequestBody @Valid VideoRequestDto requestDto) {
        return videoService.publishVideo(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete video by id", description = "Soft delete of available video by id")
    public void deleteById(@PathVariable Long id) {
        videoService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update video by id", description = "Update available video by id")
    public VideoDto update(@PathVariable Long id,
                           @RequestBody @Valid VideoRequestDto requestDto) {
        return videoService.update(id, requestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search")
    @Operation(summary = "Search videos", description = "Search videos by available parameters")
    public List<VideoDto> searchVideos(VideoSearchParametersDto searchParameters) {
        return videoService.search(searchParameters);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}/play")
    @Operation(summary = "Get content video by id",
            description = "Get available content video by id")
    public VideoContentDto playContentVideoById(@PathVariable Long id) {
        return videoService.playContentVideoById(id);
    }
}
