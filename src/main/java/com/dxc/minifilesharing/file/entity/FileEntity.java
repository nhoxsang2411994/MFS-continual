package com.dxc.minifilesharing.file.entity;

import com.dxc.minifilesharing.file.common.FileCategory;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILE")
public class FileEntity {

    //TODO Indexing category, uploader, fileName, size
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Size(min = 1, max = 36)
    @Column(name = "FILE_ID", nullable = false, unique = true, length = 36)
    private String fileId;

    //TODO validate file name, exclude special symbols, prevent duplicate file names
    @Size(min = 1, max = 100)
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    //TODO validate file's size depending on account level
    @Column(name = "SIZE", nullable = false)
    private int size;

    @Column(name = "FILE_CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileCategory category;

    // TODO (if possible) store userComments as HTMLs or something
    // Micro-service approach: cut the links between loosely coupled entities
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file")
//    private List<CommentEntity> userComments;

    // TODO validate file's path
    @Column(name = "PATH")
    private String path;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
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

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
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
