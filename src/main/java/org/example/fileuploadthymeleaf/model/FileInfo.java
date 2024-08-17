package org.example.fileuploadthymeleaf.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "files")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private LocalDateTime date_time;

    public FileInfo(String name, String url, LocalDateTime date_time) {
        this.name = name;
        this.url = url;
        this.date_time = date_time;
    }

    public FileInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDateTime() {
        return date_time;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.date_time = dateTime;
    }

}
