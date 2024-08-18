package org.example.fileuploadthymeleaf.model;

import jakarta.persistence.*;

import java.nio.file.Files;
import java.nio.file.Path;
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
    private String date_time;
    private String file_size;

    public FileInfo(String name, String url, String date_time, String file_size) {
        this.name = name;
        this.url = url;
        this.date_time = date_time;
        this.file_size = file_size;
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

    public String getDateTime() {
        return date_time;
    }

    public void setDateTime(String dateTime) {
        this.date_time = dateTime;
    }

    public void setFileSize(String fileSize) {
        this.file_size = fileSize;
    }

    public String getFileSize() {
        return file_size;
    }

}
