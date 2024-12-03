package tain.task.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tain.task.dto.video.VideoContentDto;
import tain.task.dto.video.VideoDto;
import tain.task.dto.video.VideoRequestDto;
import tain.task.model.Video;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideoControllerTest {
    protected static MockMvc mockMvc;
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final String TITLE_FIRST = "video1";
    private static final String TITLE_SECOND = "video2";
    private static final String SYNOPSIS_FIRST = "test1";
    private static final String SYNOPSIS_SECOND = "test2";
    private static final String DIRECTOR_FIRST = "director1";
    private static final String DIRECTOR_SECOND = "director2";
    private static final String CAST_FIRST = "test1";
    private static final String CAST_SECOND = "test2";
    private static final int YEAR_OF_RELEASE = 2001;
    private static final Video.Genre GENRE = Video.Genre.COMEDY;
    private static final int RUNNING_TIME = 60;
    private static final String PATH = "111xxx22";
    private static final int IMPRESSIONS = 0;
    private static final int VIEWS = 0;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/video/add-default-videos.sql"
                    )
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/video/delete-from-videos.sql"
                    )
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/video/delete-test-video.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new video")
    void createVideo_ValidRequestDto_Success() throws Exception {
        VideoRequestDto request = publishVideoRequest(
                TITLE_FIRST,
                SYNOPSIS_FIRST,
                DIRECTOR_FIRST,
                CAST_FIRST,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                PATH
        );

        VideoDto expected = createExpectedVideoDto(
                DEFAULT_ID,
                TITLE_FIRST,
                SYNOPSIS_FIRST,
                DIRECTOR_FIRST,
                CAST_FIRST,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                IMPRESSIONS,
                VIEWS,
                PATH
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/videos")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), VideoDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get list videos")
    void getAll_GivenVideosInCatalog() throws Exception {
        VideoDto firstVideo = createExpectedVideoDto(
                DEFAULT_ID,
                TITLE_FIRST,
                SYNOPSIS_FIRST,
                DIRECTOR_FIRST,
                CAST_FIRST,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                IMPRESSIONS,
                VIEWS,
                PATH
        );

        VideoDto secondVideo = createExpectedVideoDto(
                SECOND_ID,
                TITLE_SECOND,
                SYNOPSIS_SECOND,
                DIRECTOR_SECOND,
                CAST_SECOND,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                IMPRESSIONS,
                VIEWS,
                PATH
        );

        List<VideoDto> expected = new ArrayList<>();
        expected.add(firstVideo);
        expected.add(secondVideo);

        MvcResult result = mockMvc.perform(get("/videos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), VideoDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get video by id")
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getVideoById() throws Exception {
        VideoDto expected = createExpectedVideoDto(
                SECOND_ID,
                TITLE_SECOND,
                SYNOPSIS_SECOND,
                DIRECTOR_SECOND,
                CAST_SECOND,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                IMPRESSIONS,
                VIEWS,
                PATH
        );
        MvcResult result = mockMvc.perform(get("/videos/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), VideoDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Soft-Delete video by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/videos/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update video by id")
    void updateVideoById() throws Exception {
        VideoRequestDto request = publishVideoRequest(
                TITLE_SECOND,
                SYNOPSIS_SECOND,
                DIRECTOR_SECOND,
                CAST_SECOND,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                PATH);

        VideoDto expected = createExpectedVideoDto(
                SECOND_ID,
                TITLE_SECOND,
                SYNOPSIS_SECOND,
                DIRECTOR_SECOND,
                CAST_SECOND,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                IMPRESSIONS,
                VIEWS,
                PATH
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/videos/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), VideoDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get video by id")
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void playVideoById() throws Exception {
        VideoContentDto expected = videoContent(PATH);

        MvcResult result = mockMvc.perform(get("/videos/{id}/play", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoContentDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), VideoContentDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getVideoRelativePath());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Search videos by parameters")
    void searchVideos_GivenSearchParameters() throws Exception {
        // Очікувані результати
        VideoDto firstVideo = createExpectedVideoDto(
                DEFAULT_ID,
                TITLE_FIRST,
                SYNOPSIS_FIRST,
                DIRECTOR_FIRST,
                CAST_FIRST,
                YEAR_OF_RELEASE,
                GENRE,
                RUNNING_TIME,
                IMPRESSIONS,
                VIEWS,
                PATH
        );

        List<VideoDto> expected = List.of(firstVideo);

        String[] titles = {TITLE_FIRST};
        String[] directors = {DIRECTOR_FIRST};

        MvcResult result = mockMvc.perform(get("/videos/search")
                        .param("titles", TITLE_FIRST)
                        .param("directors", DIRECTOR_FIRST)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), VideoDto[].class);

        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    private VideoDto createExpectedVideoDto(
            Long id,
            String title,
            String synopsis,
            String director,
            String cast,
            int yearOfRelease,
            Video.Genre genre,
            int runningTime,
            int impressions,
            int views,
            String videoRelativePath) {
        return new VideoDto()
                .setId(id)
                .setTitle(title)
                .setSynopsis(synopsis)
                .setDirector(director)
                .setCast(cast)
                .setYearOfRelease(yearOfRelease)
                .setGenre(genre)
                .setRunningTime(runningTime)
                .setImpressions(impressions)
                .setViews(views)
                .setVideoRelativePath(videoRelativePath);
    }

    private VideoRequestDto publishVideoRequest(
            String title,
            String synopsis,
            String director,
            String cast,
            int yearOfRelease,
            Video.Genre genre,
            int runningTime,
            String videoRelativePath) {
        return new VideoRequestDto()
                .setTitle(title)
                .setSynopsis(synopsis)
                .setDirector(director)
                .setCast(cast)
                .setYearOfRelease(yearOfRelease)
                .setGenre(genre)
                .setRunningTime(runningTime)
                .setVideoRelativePath(videoRelativePath);
    }

    private VideoContentDto videoContent(
            String videoRelativePath) {
        return new VideoContentDto()
                .setVideoRelativePath(videoRelativePath);
    }
}
