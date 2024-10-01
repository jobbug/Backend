package com.example.jobbug.global.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListWrapperResponse {
    private List<Object> list = new ArrayList<>();

    public ListWrapperResponse(Object list) {
        if(list instanceof List) {
            this.list = (List<Object>) list;
        } else {
            this.list.add(list);
        }
    }
}
