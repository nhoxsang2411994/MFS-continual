package com.dxc.minifilesharing.file.service;

import com.dxc.minifilesharing.file.common.CommonFileCategory;
import com.dxc.minifilesharing.file.common.FileServiceError;
import com.dxc.minifilesharing.file.config.StorageProperties;
import com.dxc.minifilesharing.file.entity.*;
import com.dxc.minifilesharing.file.exception.FileServiceException;
import com.dxc.minifilesharing.file.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
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

    private static final int NO_OF_RESULTS = 3;

    private FileRepository fileRepository;

    @Autowired
    public FileService(StorageProperties properties, FileRepository fileRepository) {
        this.fileRepository = fileRepository;
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
                    " Maximum upload size is: " + maxUpSize + "MB.");
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

    /**
     * Classifies the contentType of the uploaded file.
     * <p>
     * @param userDir
     * @param filename
     * @return CommonFileCategory
     * @throws IOException
     */
    private CommonFileCategory classifyContentType(Path userDir, String filename) throws IOException {
        CommonFileCategory commonFileCategory;
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

        return string2FileCategory(rs[0]);
    }//end classifyContentType

    private CommonFileCategory string2FileCategory(String r) {
        CommonFileCategory commonFileCategory;
        switch (r) {
            case APPLICATION: {
                commonFileCategory = CommonFileCategory.application;
                break;
            }
            case AUDIO: {
                commonFileCategory = CommonFileCategory.audio;
                break;
            }
            case VIDEO: {
                commonFileCategory = CommonFileCategory.video;
                break;
            }
            case IMAGE: {
                LOGGER.info("OKE");
                commonFileCategory = CommonFileCategory.image;
                break;
            }
            case TEXT: {
                commonFileCategory = CommonFileCategory.text;
                break;
            }
            default: {
                commonFileCategory = CommonFileCategory.unclassified;
                break;
            }
        }//end switch
        return commonFileCategory;
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
    }//end postComment

    public List<UserFile> readFilesByCategory(String category) {
        if (null == category || category.isEmpty()) {
            throw new FileServiceException(INVALID_SEARCH_TERM, "The input category is invalid.");
        }

        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "fileName"));
        Pageable pageable = new PageRequest(0, NO_OF_RESULTS, sort);

        String[] rs = category.split("");
        category = "";
        for (int i = 1, l = rs.length; i < rs.length - 1; i++) {
            category = category.concat(rs[i]);
        }

        Page<FileEntity> page = fileRepository.findByFileCategory(string2FileCategory(category), pageable);

        if (null == page || 0 >= page.getSize()) {
            throw new FileServiceException(NOT_FOUND, "No result with the input category found: " + category);
        }

        List<UserFile> userFiles = new ArrayList<>();
        for (FileEntity fileEntity : page.getContent()) {
            userFiles.add(fileEntity2UserFile(fileEntity));
        }

        return userFiles;
    }//end readFilesByCategory

    private UserFile fileEntity2UserFile(FileEntity fileEntity) {
        UserFile userFile = new UserFile();
        userFile.setFileCategory(commonFileCategory2Category(fileEntity.getCategory()));
        userFile.setFileId(UUID.fromString(fileEntity.getFileId()));
        userFile.setFileName(fileEntity.getFileName());
        userFile.setSize(fileEntity.getSize());
        userFile.setUploaderId(UUID.fromString(fileEntity.getUploaderId()));
        return userFile;
    }

    private Category commonFileCategory2Category(CommonFileCategory category) {
        if (null == category) {
            throw new FileServiceException(UNEXPECTED, "The input category is invalid.");
        }
        switch (category) {
            case text:{
                return Category.TEXT;
            }
            case audio:{
                return Category.AUDIO;
            }
            case application:{
                return Category.APPLICATION;
            }
            case image: {
                return Category.IMAGE;
            }
            case video: {
                return Category.VIDEO;
            }
            default:{
                return Category.UNCLASSIFIED;
            }
        }
    }
}
