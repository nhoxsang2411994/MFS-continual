package com.dxc.minifilesharing.file;

import com.dxc.minifilesharing.file.common.CommonFileCategory;
import com.dxc.minifilesharing.file.config.StorageProperties;
import com.dxc.minifilesharing.file.delegate.V1ApiDelegateImp;
import com.dxc.minifilesharing.file.entity.Comment;
import com.dxc.minifilesharing.file.entity.CommentEntity;
import com.dxc.minifilesharing.file.entity.FileEntity;
import com.dxc.minifilesharing.file.repository.FileRepository;
import com.dxc.minifilesharing.file.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.dxc.minifilesharing.file.common.CommonFileCategory.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class FileServiceTester {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceTester.class);

    private final static UUID FILE_ID = UUID.fromString("593e61e8-5308-4655-9343-12cf7f242af8");
    private final static UUID USER_ID = UUID.fromString("c66b8f6e-6c84-4296-8c49-a93dd90a286c");
    private final static String COMMENT_CONTENT = "This is a very long comment.";
    private final static String UPLOAD_PATH = "D:\\Projects\\Maven_projects\\minifilesharing\\src\\main\\webapp\\upload";
    private MockMultipartFile textFile = new MockMultipartFile("data",
            "text.txt", "text/plain", "Some text".getBytes());

    private Comment comment = new Comment();
    private CommentEntity commentEntity = new CommentEntity();

    private FileEntity fileEntity;

    @Mock
    private FileRepository fileRepository;

    private Path rootLocation;

//    @InjectMocks
    private FileService fileService;
    @Before
    public void setUploadPath(){
        StorageProperties storageProperties = new StorageProperties();
        storageProperties.setLocation("src/main/webapp/upload");
        fileService = new FileService(storageProperties, fileRepository);
    }


    @Test
    public void testUpFileHappyCase() {

//        LOGGER.info(fileService.toString());

//        assertEquals(36, fileService.upFile(textFile, USER_ID, 5).toString().length());
//        when(rootLocation.toAbsolutePath()).thenReturn("D://")

//        when(rootLocation.toAbsolutePath().toString()).thenReturn(UPLOAD_PATH);
        comment.setComment("This is a very long comment.");
        comment.setCommentatorId(USER_ID);
        comment.setComment(COMMENT_CONTENT);
        commentEntity = comment2CommentEntity(comment, USER_ID);

        when(fileRepository.findByFileId(FILE_ID.toString())).thenReturn(
                initializeFileEntity(0, FILE_ID, USER_ID, "file-5MB", unclassified,
                        10485760L, commentEntity) );

        assertEquals(36, fileService.postComment(USER_ID, FILE_ID, "vu-medicine", comment).toString().length());
    }

    private CommentEntity comment2CommentEntity(Comment comment, UUID commentatorId) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUsername(comment.getUsername());
        commentEntity.setCommentContent(comment.getComment());
        commentEntity.setCommentatorId(commentatorId.toString());
        commentEntity.setCreateDate(new Date());
        commentEntity.setModifiedDate(new Date());
        commentEntity.setDeleted(false);
        // No file is set yet
        return commentEntity;
    }

    private FileEntity initializeFileEntity(long id, UUID fileId, UUID userId, String fileName, CommonFileCategory commonFileCategory,
                                            long size, CommentEntity... commentEntities) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUserComments(Arrays.stream(commentEntities).collect(Collectors.toList()));
        fileEntity.setFileId(fileId.toString());
        fileEntity.setUploaderId(userId.toString());
        fileEntity.setFileName(fileName);
        fileEntity.setCategory(commonFileCategory);
        fileEntity.setSize(size);
        fileEntity.setCreateDate(new Date());
        fileEntity.setModifiedDate(new Date());
        fileEntity.setDeleted(false);
        fileEntity.setId(id);
        return fileEntity;
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        public FileService fileService() {
            return mock(FileService.class);
        }

        @Bean
        public V1ApiDelegateImp v1ApiDelegateImp() {
            return mock(V1ApiDelegateImp.class);
        }


        @Bean
        public FileRepository fileRepository() {
            return mock(FileRepository.class);
        }
    }
}
