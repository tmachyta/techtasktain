package tain.task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE videos SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "videos")
public class Video {
    public enum Genre {
        ACTION,
        DRAMA,
        COMEDY,
        HORROR,
        DOCUMENTARY,
        THRILLER,
        ROMANCE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String synopsis;
    @Column(nullable = false)
    private String director;
    @Column(nullable = false)
    private String cast;
    @Column(name = "year_of_release")
    private int yearOfRelease;
    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    @Column(name = "running_time")
    private int runningTime;
    @Column(name = "video_relative_path")
    private String videoRelativePath;
    @Column(name = "impressions")
    private int impressions;
    @Column(name = "views")
    private int views;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
