package tain.task.repository.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tain.task.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long>,
        JpaSpecificationExecutor<Video> {
}
