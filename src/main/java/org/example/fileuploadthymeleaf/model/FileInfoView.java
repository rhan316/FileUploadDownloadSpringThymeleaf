package org.example.fileuploadthymeleaf.model;

import java.time.LocalDateTime;

public class FileInfoView {
// TODO: Add type of file by ENUM -> Type
    private Long id;
    private String name;
    private String url;
    private String dateTime;
    private String fileSize;
    private String fileType;

    public FileInfoView(String name, String url, String dateTime, String fileSize, String fileType) {
        this.name = name;
        this.url = url;
        this.dateTime = dateTime;
        this.fileSize = fileSize;
        this.fileType = fileType;
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
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
