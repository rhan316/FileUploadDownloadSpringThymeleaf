package org.example.fileuploadthymeleaf.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository  extends JpaRepository<FileInfo, Long> {

    void deleteByName(String filename);
}
