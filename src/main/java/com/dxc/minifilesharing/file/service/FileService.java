package com.dxc.minifilesharing.file.service;

import com.dxc.minifilesharing.file.common.FileServiceError;
import com.dxc.minifilesharing.file.config.StorageProperties;
import com.dxc.minifilesharing.file.exception.FileServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    private final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private final Path rootLocation;

    @Autowired
    public FileService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public UUID upFile(MultipartFile file, UUID userId) {
        if (null == file || file.isEmpty()) {
            throw new FileServiceException(FileServiceError.INPUT_FILE_INVALID, userId);
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            // This is a security check
            throw new FileServiceException(FileServiceError.FILENAME_INVALID, filename);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileServiceException(FileServiceError.UPLOAD_FAILED, filename);
        }
        return null;
    }
}
