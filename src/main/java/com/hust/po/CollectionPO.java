package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "collection")
public class CollectionPO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "anime_name")
    private String animeName;

    @Column(name = "type")
    private String type;
}
