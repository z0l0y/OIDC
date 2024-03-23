package com.hust.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "user_info")
public class UserPO {
    @javax.persistence.Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "bio")
    private String bio;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
