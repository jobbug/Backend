package com.example.jobbug.global.config.web.socket;

import lombok.Data;

@Data
public class SocketRequest {
    private String type;
    private Object data;
}
