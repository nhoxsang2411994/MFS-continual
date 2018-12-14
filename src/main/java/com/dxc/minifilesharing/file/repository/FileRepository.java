package com.dxc.minifilesharing.file.repository;

import com.dxc.minifilesharing.file.common.CommonFileCategory;
import com.dxc.minifilesharing.file.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileId(String toString);

    @Query("SELECT f FROM FileEntity f " +
            "WHERE f.category = :commonFileCategory AND f.deleted = false")
    Page<FileEntity> findByFileCategory(@Param("commonFileCategory") CommonFileCategory commonFileCategory, Pageable pageable);
}
