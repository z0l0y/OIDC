package com.hust.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FriendListDTO {
    List<String> friendList = new ArrayList<>();
}
