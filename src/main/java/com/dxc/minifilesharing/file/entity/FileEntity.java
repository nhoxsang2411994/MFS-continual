package com.dxc.minifilesharing.file.entity;

import com.dxc.minifilesharing.file.common.CommonFileCategory;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILE", indexes = {
        @Index(name = "IDX_FILE_NAME", columnList = "FILE_NAME"),
        @Index(name = "IDX_SIZE", columnList = "SIZE"),
        @Index(name = "IDX_FILE_CATEGORY", columnList = "FILE_CATEGORY"),
        @Index(name = "IDX_UPLOADER_ID", columnList = "UPLOADER_ID")
})
public class FileEntity {

    //TODO Indexing category, uploader, fileName, size
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Size(min = 1, max = 36)
    @Column(name = "FILE_ID", nullable = false, unique = true, length = 36)
    private String fileId;

    @Size(min = 1, max = 36)
    @Column(name = "UPLOADER_ID", nullable = false, length = 36)
    private String uploaderId;

    //TODO validate file name, exclude special symbols
    @Size(min = 1, max = 100)
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    //TODO validate file's size depending on account level
    @Column(name = "SIZE", nullable = false)
    private long size;

    @Column(name = "FILE_CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommonFileCategory category;

    // TODO (if possible) store userComments as HTMLs or something
    // Micro-service approach: cut the links between loosely coupled entities
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file", cascade = CascadeType.ALL)
    private List<CommentEntity> userComments;

    // TODO add user's id or user as an object?

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
        modifiedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public CommonFileCategory getCategory() {
        return category;
    }

    public void setCategory(CommonFileCategory category) {
        this.category = category;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public List<CommentEntity> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<CommentEntity> userComments) {
        this.userComments = userComments;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
                ", uploaderId='" + uploaderId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", category=" + category +
                ", userComments=LAZY" +
                ", deleted=" + deleted +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
