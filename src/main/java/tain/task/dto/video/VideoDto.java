package tain.task.dto.video;

import lombok.Data;
import lombok.experimental.Accessors;
import tain.task.model.Video.Genre;

@Data
@Accessors(chain = true)
public class VideoDto {
    private Long id;
    private String title;
    private String synopsis;
    private String director;
    private String cast;
    private int yearOfRelease;
    private Genre genre;
    private int runningTime;
    private int impressions;
    private int views;
    private String videoRelativePath;
}
