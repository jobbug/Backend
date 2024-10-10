package com.example.jobbug.domain.badge.enums;

public enum BadgeType {
    KILLER("학살자"), EXPERT("고수"), SOS("살려주세요"), DISLIKE("싫어요");

    private final String name;

    BadgeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
