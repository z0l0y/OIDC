package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "rating")
public class RatingPO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

    @Column(name = "rating_value")
    private Integer ratingValue;

    @Column(name = "anime_name")
    private String animeName;

    @Column(name = "commentary")
    private String commentary;
}
