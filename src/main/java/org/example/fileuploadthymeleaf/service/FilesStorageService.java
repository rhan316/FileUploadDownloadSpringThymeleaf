package org.example.fileuploadthymeleaf.service;

import jakarta.annotation.PostConstruct;
import org.example.fileuploadthymeleaf.model.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;


public interface FilesStorageService {

    @PostConstruct
    void init();

    void save(MultipartFile file);

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();

    void delete(String filename);

    boolean exists(String filename);

    List<FileInfo> getFiles();

}
