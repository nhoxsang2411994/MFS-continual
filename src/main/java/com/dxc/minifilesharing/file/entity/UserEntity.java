package com.dxc.minifilesharing.file.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER")
public class UserEntity {
    // TODO create account level entity
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Size(min = 1, max = 36)
    @Column(name = "USER_ID", nullable = false, length = 36)
    private String userId;

    // TODO validate username if possible (must not contain special symbols)
    @Size(min = 1, max = 20)
    @Column(name = "USERNAME", nullable = false, unique = true, length = 20)
    private String username;

    @Size(min = 1, max = 100)
    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Size(min = 1, max = 100)
    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @Size(min = 1, max = 50)
    @Column(name = "EMAIL", length = 50, nullable = false, unique = true)
    private String email;

    @Size(min = 7, max = 10)
    @Column(name = "PHONE", length = 10)
    private String phone;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file")
//    private List<CommentEntity> userComments;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;

    @Column(name = "DELETED_DATE", nullable = false)
    private Date deletedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userComments=LAZY" +
                ", deleted=" + deleted +
                ", createDate=" + createDate +
                ", deletedDate=" + deletedDate +
                '}';
    }
}
