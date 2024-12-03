package tain.task.repository;

import org.springframework.data.jpa.domain.Specification;
import tain.task.dto.video.VideoSearchParametersDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(VideoSearchParametersDto searchParameters);
}
