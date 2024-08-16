package org.example.fileuploadthymeleaf.controller;

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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

@Controller
public class FileController {

    private final FilesStorageService<FileInfo> filesStorageService;

    @Autowired
    public FileController(FilesStorageService<FileInfo> filesStorageService) {
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

        return "upload_form";
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {

        try {

            model.addAttribute("files", filesStorageService.getFiles(filesStorageService));

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
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {

        try {

            if (!filesStorageService.exists(filename)) {
                return ResponseEntity.status(404).body("File not found");
            }

            filesStorageService.delete(filename);
            return ResponseEntity.ok("File deleted");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Could not delete file: " + e.getMessage());
        }

    }

}
