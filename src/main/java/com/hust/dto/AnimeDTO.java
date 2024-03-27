package com.hust.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class AnimeDTO {
    private String name;
    private Integer episodes;
    private String director;
    private String avatar;
    private String introduction;
}
