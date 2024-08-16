package org.example.fileuploadthymeleaf.service;

import jakarta.annotation.PostConstruct;
import org.example.fileuploadthymeleaf.controller.FileController;
import org.example.fileuploadthymeleaf.model.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService<FileInfo> {

    private final Path rootLocation = Paths.get("/src/main/resources/uploads/");
    private static final Logger logger = Logger.getLogger(FilesStorageServiceImpl.class.getName());

    @Override
    @PostConstruct
    public void init() {

        try {

            Files.createDirectories(rootLocation);

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize files storage", e);
        }
    }

    @Override
    public void save(MultipartFile file) {

        try {

            Files.copy(
                    file.getInputStream(),
                    rootLocation.resolve(Objects.requireNonNull(file.getOriginalFilename()))

            );

        } catch (IOException e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists", e);
            }

            logger.severe("Error saving file: " + e.getMessage());
            throw new RuntimeException("Could not save file", e);
        }
    }

    @Override
    public Resource load(String filename) {

        try {

            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error! Could not load file " + filename, e);
        }
    }

    @Override
    public void deleteAll() {

        FileSystemUtils.deleteRecursively(rootLocation.toFile());

    }

    @Override
    public Stream<Path> loadAll() {

        try (Stream<Path> paths = Files.walk(rootLocation, 1)) {

            return paths
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize)
                    .toList()
                    .stream();

        } catch (IOException e) {
            throw new RuntimeException("Could not read files", e);
        }
    }

    @Override
    public void delete(String filename) {

        try {

            Files.deleteIfExists(rootLocation.resolve(filename));

        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filename, e);
        }
    }

    @Override
    public boolean exists(String filename) {
        return Files.exists(rootLocation.resolve(filename));
    }

    @Override
    public List<FileInfo> getFiles(FilesStorageService<FileInfo> filesStorageService) {

        return filesStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile",
                            path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).toList();
    }
}
