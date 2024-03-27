package com.hust.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "client_state")
public class StatePO {
    @javax.persistence.Id
    @Column(name = "id")
    private Long id;

    @Column(name = "state")
    private String state;
}
