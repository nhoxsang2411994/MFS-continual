package com.dxc.minifilesharing.file.service;

import com.dxc.minifilesharing.file.common.FileCategory;
import com.dxc.minifilesharing.file.common.FileServiceError;
import com.dxc.minifilesharing.file.config.StorageProperties;
import com.dxc.minifilesharing.file.entity.Comment;
import com.dxc.minifilesharing.file.entity.CommentEntity;
import com.dxc.minifilesharing.file.entity.FileEntity;
import com.dxc.minifilesharing.file.exception.FileServiceException;
import com.dxc.minifilesharing.file.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import static com.dxc.minifilesharing.file.common.FileServiceError.*;

@Service
public class FileService {
    private final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private final Path rootLocation;

    private static final String UNCLASSIFIED = "null";
    private static final String APPLICATION = "application";
    private static final String IMAGE = "image";
    private static final String VIDEO = "video";
    private static final String AUDIO = "audio";
    private static final String TEXT = "text";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    public FileService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public UUID upFile(MultipartFile file, UUID userId, int maxUpSize) {
        LOGGER.info(rootLocation.toAbsolutePath().toString());
        FileEntity fileEntity = new FileEntity();
        // validate parameters
        if (null == userId) {
            throw new FileServiceException(INVALID_UPLOADER, "User id is null");
        }
        // set uploader id
        fileEntity.setUploaderId(userId.toString());

        if (null == file || file.isEmpty()) {
            throw new FileServiceException(FileServiceError.INPUT_FILE_INVALID, userId);
        }
        if (maxUpSize <= 0) {
            throw new FileServiceException(FileServiceError.UPLOAD_FAILED,
                    "The upload size is invalid: " + maxUpSize);
        }

        // create new user folder if necessary
        Path userDir = Paths.get(rootLocation.toString() + "//" + userId.toString());
        if (!Files.exists(userDir)) {
            try {
                Files.createDirectory(userDir);
            } catch (IOException e) {
                throw new FileServiceException(FileServiceError.UPLOAD_FAILED,
                        userDir.toAbsolutePath().toString());
            }
        }

        // Check file size
        boolean limitExceeded = maxUpSize*1024*1024 < file.getSize();
        if (limitExceeded) {
            throw new FileServiceException(FileServiceError.UPLOAD_FAILED, "The file is too big." +
                    " Maximum upload size is: " + maxUpSize);
        }
        fileEntity.setSize(file.getSize());

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            // This is a security check
            throw new FileServiceException(FileServiceError.FILENAME_INVALID, filename);
        }
        // set file name
        fileEntity.setFileName(filename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, userDir.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);

        // Files are classified based on its content type
        fileEntity.setCategory(classifyContentType(userDir, filename));

        } catch (IOException e) {
            throw new FileServiceException(FileServiceError.UPLOAD_FAILED, e, filename);
        } catch (RuntimeException e) {
            throw new FileServiceException(FileServiceError.UNEXPECTED, e, filename);
        }

        fileEntity.setFileId(UUID.randomUUID().toString());
        fileEntity.setUserComments(new ArrayList<>());
        fileRepository.saveAndFlush(fileEntity);
        return UUID.fromString(fileEntity.getFileId());
    }

    private FileCategory classifyContentType(Path userDir, String filename) throws IOException {
        FileCategory fileCategory;
        Path uploadedFile = Paths.get(userDir.toString() + filename);
        String contentType = Files.probeContentType(uploadedFile);
        String[] rs;

        if (contentType != null) {
            rs = contentType.split("/");
            if (rs.length <= 0) {
                throw new FileServiceException(FileServiceError.UNEXPECTED, "Could not determine the content-type");
            }
            LOGGER.info(rs[0]);
        } else {
            rs = new String[1];
            rs[0] = UNCLASSIFIED;
        }

        switch (rs[0]) {
            case APPLICATION: {
                fileCategory = FileCategory.application;
                break;
            }
            case AUDIO: {
                fileCategory = FileCategory.audio;
                break;
            }
            case VIDEO: {
                fileCategory = FileCategory.video;
                break;
            }
            case IMAGE: {
                fileCategory = FileCategory.image;
                break;
            }
            case TEXT: {
                fileCategory = FileCategory.text;
                break;
            }
            default: {
                fileCategory = FileCategory.unclassified;
                break;
            }
        }//end switch
        return  fileCategory;
    }

    public UUID postComment(UUID userId, UUID fileId, String username, Comment comment) {
        if (userId == null) {
            throw new FileServiceException(INVALID_UPLOADER, "userId is null");
        }
        if (fileId == null) {
            throw new FileServiceException(INVALID_FILE_ID, "fileId is null");
        }
        if (comment == null || null == comment.getComment()) {
            throw new FileServiceException(INVALID_COMMENT, "comment is null");
        }
        if (username == null) {
            throw new FileServiceException(INVALID_COMMENTATOR, "username is null");
        }
        FileEntity fileEntity = fileRepository.findByFileId(fileId.toString());
        if (fileEntity == null) {
            throw new FileServiceException(INVALID_FILE_ID, "File could not be found: " + fileId);
        }
        fileRepository.save(fileEntity);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentatorId(userId.toString());
        commentEntity.setFile(fileEntity);
        commentEntity.setUsername(username);
        commentEntity.setCommentContent(comment.getComment());
        commentEntity.setCommentId(UUID.randomUUID().toString());
        commentEntity.setFile(fileEntity);
        fileEntity.getUserComments().add(commentEntity);
        fileRepository.flush();
        return UUID.fromString(commentEntity.getCommentId());
    }
}
