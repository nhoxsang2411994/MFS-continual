package com.dxc.minifilesharing.file.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "COMMENT")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Size(min = 1, max = 36)
    @Column(name = "COMMENT_ID", nullable = false, unique = true, length = 36)
    private String commentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private FileEntity file;

    @Size(min = 1, max = 36)
    @Column(name = "COMMENTATOR_ID", nullable = false, length = 36)
    private String commentatorId;

    @Size(min = 3, max = 15)
    @Column(name = "USERNAME", nullable = false, length = 15)
    private String username;

    @Size(min = 20, max = 200)
    @Column(name = "COMMENT_CONTENT", nullable = false, length = 200)
    private String commentContent;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;

    @Column(name = "MODIFIED_DATE", nullable = false)
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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    public String getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(String commentatorId) {
        this.commentatorId = commentatorId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }



    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", commentId='" + commentId + '\'' +
                ", file=LAZY" +
                ", commentatorId='" + commentatorId + '\'' +
                ", username='" + username + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", deleted=" + deleted +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
