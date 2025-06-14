package com.sideline.chat.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse<T> {
    private String message; // error 일때
    private T data; // 정상응답일때

    public BaseResponse(String message) {
        this.message = message;
    }
}
