package tain.task.repository.video;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tain.task.model.Video;
import tain.task.repository.SpecificationProvider;
import tain.task.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class VideoSpecificationProviderManager implements SpecificationProviderManager<Video> {
    private final List<SpecificationProvider<Video>> videoSpecificationProviders;

    @Override
    public SpecificationProvider<Video> getSpecificationProvider(String key) {
        return videoSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Can't find correct specification provider for key " + key));
    }
}
