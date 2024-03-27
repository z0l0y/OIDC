package com.hust.dto;

import lombok.Data;

/**
 * 注意一下，这里的逻辑是user1向user2请求添加好友，在user2的好友申请栏里面会出现user1，点击同意，status变为1，
 * 表示互为好友了，而status为0（默认值的话，表示还不是好友）
 */
@Data
public class FriendDTO {
    private String user1;
    private String user2;
}
