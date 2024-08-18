package org.example.fileuploadthymeleaf.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.fileuploadthymeleaf.controller.FileController;
import org.example.fileuploadthymeleaf.model.FileInfo;
import org.example.fileuploadthymeleaf.model.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService{

    private final Path rootLocation = Paths.get("./uploads");
    private static final Logger logger = Logger.getLogger(FilesStorageServiceImpl.class.getName());
    private final FileInfoRepository fileInfoRepository;

    @Autowired
    public FilesStorageServiceImpl(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }

    @PostConstruct
    @Override
    public void init() {

        try {

            Files.createDirectories(rootLocation);

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize files storage", e);
        }
    }

    @Override
    @Transactional
    public void save(MultipartFile file) {

        try {

            Files.copy(
                    file.getInputStream(),
                    rootLocation.resolve(Objects.requireNonNull(file.getOriginalFilename()))
            );

            String fileName = file.getOriginalFilename();

            FileInfo fileInfo = new FileInfo(
                    fileName,
                    MvcUriComponentsBuilder
                            .fromMethodName(FileController.class, "getFile", fileName)
                            .build()
                            .toString(),
                    this.dateTimeFormatter(LocalDateTime.now()),
                    this.fileSize(fileName)
            );

            logger.info("File saved!");

            fileInfoRepository.save(fileInfo);

        } catch (IOException e) {
            logger.info("Cannot save file! " + e.getMessage());
            System.out.println(e.getMessage());
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
    @Transactional
    public void deleteAll() {

        try {
            FileSystemUtils.deleteRecursively(rootLocation.toFile());
            fileInfoRepository.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete files", e);
        }
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
    @Transactional
    public void delete(String filename) {

        try {

            Files.deleteIfExists(rootLocation.resolve(filename));
            fileInfoRepository.deleteByName(filename);

        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filename, e);
        }
    }

    @Override
    public boolean exists(String filename) {
        return Files.exists(rootLocation.resolve(filename));
    }

    @Override
    public List<FileInfo> getFiles() {

        return new ArrayList<>(fileInfoRepository.findAll());
    }

    private String fileSize(String filename) {
        String result;

        try {

            Path file = rootLocation.resolve(filename);
            long fileSize = Files.size(file);

            if (fileSize < 1024) {
                result = fileSize + "B";
            }
            else if (fileSize < (1024 * 1024) && fileSize > 1024) {
                double sizeKB = (double) fileSize / 1024;
                result = String.format("%.2fKB", sizeKB);
            }
            else {
                double sizeMB = (double) fileSize / (1024 * 1024);
                result = String.format("%.2fMB", sizeMB);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return result;
    }

    private String dateTimeFormatter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return localDateTime.format(formatter);
    }
}
