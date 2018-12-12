package com.dxc.minifilesharing.file;

import com.dxc.minifilesharing.file.api.V1ApiDelegate;
import com.dxc.minifilesharing.file.config.StorageProperties;
import com.dxc.minifilesharing.file.delegate.V1ApiDelegateImp;
import com.dxc.minifilesharing.file.entity.Comment;
import com.dxc.minifilesharing.file.repository.FileRepository;
import com.dxc.minifilesharing.file.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.mail.Multipart;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class FileServiceTester {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceTester.class);

    private final static UUID FILE_ID = UUID.fromString("593e61e8-5308-4655-9343-12cf7f242af8");
    private final static UUID USER_ID = UUID.fromString("c66b8f6e-6c84-4296-8c49-a93dd90a286c");

    private MockMultipartFile textFile = new MockMultipartFile("data",
            "text.txt", "text/plain", "Some text".getBytes());

    private Comment comment = new Comment();

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private StorageProperties storageProperties;

    @Autowired
    private FileService fileService;



    @Test
    public void testUpFileHappyCase() {
        List<String> list = new ArrayList<>();
        List<String> spyList = Mockito.spy(list);

        spyList.add("one");
        spyList.add("two");

        Mockito.verify(spyList).add("one");
        Mockito.verify(spyList).add("two");

        assertEquals(2, spyList.size());

//        LOGGER.info(fileService.toString());

//        assertEquals(36, fileService.upFile(textFile, USER_ID, 5).toString().length());
        when(storageProperties.getLocation()).thenReturn("src//main//webapp//upload");
        comment.setComment("This is a very long comment.");
        assertEquals(36, fileService.postComment(USER_ID, FILE_ID, "vu-medicine", comment));
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
