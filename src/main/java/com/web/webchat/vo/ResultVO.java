package com.web.webchat.vo;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO<T> {

    private Integer code;

    private String message;

    private T data;

    public ResultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}