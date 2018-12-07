package com.dxc.minifilesharing.file.repository;

import com.dxc.minifilesharing.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, String> {
}
