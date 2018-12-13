package com.dxc.minifilesharing.file.delegate;

import com.dxc.minifilesharing.file.api.V1ApiDelegate;
import com.dxc.minifilesharing.file.entity.Category;
import com.dxc.minifilesharing.file.entity.Comment;
import com.dxc.minifilesharing.file.entity.UserFile;
import com.dxc.minifilesharing.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
public class V1ApiDelegateImp implements V1ApiDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(V1ApiDelegateImp.class);

    @Autowired
    private FileService fileService;


    @Override
    public ResponseEntity<byte[]> downloadFile(Integer maxDownloadSize, UUID fileId) {
        return null;
    }

    @Override
    public ResponseEntity<UUID> postComment(UUID userId, UUID fileId, String username, Comment comment) {
        return ResponseEntity.ok(fileService.postComment(userId, fileId, username, comment));
    }

    @Override
    public ResponseEntity<UserFile> readFileInfo(UUID fileId) {
        return null;
    }


    @Override
    public ResponseEntity<List<UserFile>> readFilesByCategory(String category) {
        return ResponseEntity.ok(fileService.readFilesByCategory(category));
    }


    @Override
    public ResponseEntity<List<byte[]>> readFilesByFileNames(String fileName) {
        return null;
    }

    @Override
    public ResponseEntity<List<byte[]>> readFilesByMultiCategories(List<Category> categories) {
        return null;
    }


    @Override
    public ResponseEntity<List<byte[]>> readFilesBySize(Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<List<byte[]>> readFilesByUsername(String username) {
        return null;
    }

    @Override
    public ResponseEntity<UUID> upFile(MultipartFile upfile, Integer maxUpSize, UUID userId) {
        return ResponseEntity.ok(fileService.upFile(upfile, userId, maxUpSize));
    }


}
