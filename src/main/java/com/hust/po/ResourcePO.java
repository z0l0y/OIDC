package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "resource_info")
public class ResourcePO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

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

    @Column(name = "code")
    private String code;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "scope")
    private String scope;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
