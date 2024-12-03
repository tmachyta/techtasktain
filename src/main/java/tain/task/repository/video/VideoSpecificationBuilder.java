package tain.task.repository.video;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tain.task.dto.video.VideoSearchParametersDto;
import tain.task.model.Video;
import tain.task.repository.SpecificationBuilder;
import tain.task.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class VideoSpecificationBuilder implements SpecificationBuilder<Video> {
    private final SpecificationProviderManager<Video> videoSpecificationProviderManager;

    @Override
    public Specification<Video> build(VideoSearchParametersDto searchParameters) {
        Specification<Video> spec = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(videoSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.directors() != null && searchParameters.directors().length > 0) {
            spec = spec.and(videoSpecificationProviderManager.getSpecificationProvider("director")
                    .getSpecification(searchParameters.directors()));
        }
        return spec;
    }
}
