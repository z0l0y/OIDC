package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "rating")
public class RatingPO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "rating_value")
    private Integer ratingValue;

    @Column(name = "anime_name")
    private String animeName;

    @Column(name = "commentary")
    private String commentary;

    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

/*    @Column(name = "status")
    private Integer status;*/
}
