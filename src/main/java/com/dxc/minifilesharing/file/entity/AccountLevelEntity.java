package com.dxc.minifilesharing.file.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ACCOUNT_LEVEL")
public class AccountLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Size(min = 1, max = 36)
    @Column(name = "ACCOUNT_LEVEL_ID", nullable = false, unique = true, length = 36)
    private String accountLevelId;


}
