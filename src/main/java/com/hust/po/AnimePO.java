package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "anime")
public class AnimePO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "episodes")
    private Integer episodes;

    @Column(name = "director")
    private String director;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "introduction")
    private String introduction;
}
