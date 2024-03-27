package com.hust.utils;

public enum CollectStatus {
    WANT_TO_WATCH("想看"),
    WATCHED("看过"),
    CURRENTLY_WATCHING("在看"),
    ON_HOLD("搁置"),
    ABANDONED("抛弃");

    private final String status;

    CollectStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
