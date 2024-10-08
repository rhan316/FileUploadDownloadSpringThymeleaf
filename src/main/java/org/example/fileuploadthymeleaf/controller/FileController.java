package org.example.fileuploadthymeleaf.controller;

import org.example.fileuploadthymeleaf.model.FileInfoRepository;
import org.example.fileuploadthymeleaf.model.FileInfoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.example.fileuploadthymeleaf.model.FileInfo;
import org.example.fileuploadthymeleaf.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


@Controller
public class FileController {

    private final FilesStorageService filesStorageService;
    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public FileController(FilesStorageService filesStorageService) {
        this.filesStorageService = filesStorageService;
    }

    @GetMapping("/")
    public String homepage() {
        return "redirect:/files";
    }

    @GetMapping("/files/new")
    public String newFile(Model model) {
        return "upload_form";
    }

    @PostMapping("/files/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {

        try {

            filesStorageService.save(file);

            model.addAttribute("message", "File successfully uploaded");

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return getListFiles(model);
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {

        try {
            
            List<FileInfoView> files = filesStorageService.getFiles();

            model.addAttribute("files", files);

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "files";
    }

    @GetMapping("/files/sortBy/{sortBy:.+}")
    public String getSortedFilesBy(@PathVariable String sortBy, Model model) {
        try {
            List<FileInfoView> files = filesStorageService.getFiles(sortBy);
            model.addAttribute("files", files);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "files";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        try {

            Resource file = filesStorageService.load(filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/files/delete/{filename:.+}")
    public String deleteFile(@PathVariable String filename, Model model) {


        logger.info("Attempting to delete file: {}", filename);

        try {

            if (!filesStorageService.exists(filename)) {
                logger.warn("File not found: {}", filename);
                throw new RuntimeException("File not found!");
            }

            filesStorageService.delete(filename);
            logger.info("File deleted successfully: {}", filename);
            return getListFiles(model);

        } catch (Exception e) {
            logger.error("Error deleting file: {}", filename);
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    @DeleteMapping("/files/delete")
    public String deleteFiles(Model model) {
        filesStorageService.deleteAllFiles();

        return getListFiles(model);
    }

}
