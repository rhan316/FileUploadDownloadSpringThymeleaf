package org.example.fileuploadthymeleaf.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesStorageService {

    void init();

    void save(MultipartFile file);

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();

    void delete(String filename);

    boolean exists(String filename);

    List<?> getFiles();
}
