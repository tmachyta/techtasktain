package tain.task.dto.video;

import lombok.Data;
import lombok.experimental.Accessors;
import tain.task.model.Video;

@Data
@Accessors(chain = true)
public class VideoRequestDto {
    private String title;
    private String synopsis;
    private String director;
    private String cast;
    private int yearOfRelease;
    private Video.Genre genre;
    private int runningTime;
    private String videoRelativePath;
}
