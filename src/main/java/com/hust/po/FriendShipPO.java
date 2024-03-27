package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "friendship")
public class FriendShipPO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user1")
    private String user1;

    @Column(name = "user2")
    private String user2;

    @Column(name = "status")
    private String status;
}
