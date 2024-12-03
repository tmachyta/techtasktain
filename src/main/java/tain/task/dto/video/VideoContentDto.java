package tain.task.dto.video;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VideoContentDto {
    private String videoRelativePath;
}
