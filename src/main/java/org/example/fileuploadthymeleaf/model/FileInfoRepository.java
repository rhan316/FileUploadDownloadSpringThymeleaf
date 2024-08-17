package org.example.fileuploadthymeleaf.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository  extends ListCrudRepository<FileInfo, Long> {

    void deleteByName(String fileName);
}
