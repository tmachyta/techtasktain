package tain.task.repository.video.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tain.task.model.Video;
import tain.task.repository.SpecificationProvider;

@Component
public class DirectorSpecificationProvider implements SpecificationProvider<Video> {
    @Override
    public String getKey() {
        return "director";
    }

    @Override
    public Specification<Video> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("director").in(Arrays.stream(params).toArray());
    }
}
