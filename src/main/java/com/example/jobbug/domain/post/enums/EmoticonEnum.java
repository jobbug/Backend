package com.example.jobbug.domain.post.enums;

import static com.example.jobbug.global.exception.enums.ErrorCode.INVALID_INPUT_VALUE;

public enum EmoticonEnum {
    HAPPY(1, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_1.png"),
    LOVE(2, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_2.png"),
    SCARY(3, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_3.png"),
    BUTTERFLY(4, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_4.png"),
    LADYBUG(5, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_5.png"),
    MOTH(6, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_6.png"),
    LOGO(7, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_7.png"),
    CAMERA(8, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_8.png"),
    MONEY(9, "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/emoticon/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A9%E1%84%90%E1%85%B5%E1%84%8F%E1%85%A9%E1%86%AB_9.png");

    private final int number;
    private final String url;

    EmoticonEnum(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public static String getUrlByNumber(int number) {
        for (EmoticonEnum emoticon : EmoticonEnum.values()) {
            if (emoticon.number == number) {
                return emoticon.url;
            }
        }
       throw new IllegalArgumentException(INVALID_INPUT_VALUE.getMessage());
    }
}
