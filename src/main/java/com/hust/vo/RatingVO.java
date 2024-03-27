package com.hust.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingVO {
    private String animeName;
    private Integer ratingValue;
    private String commentary;
    private LocalDateTime gmtModified;
}
