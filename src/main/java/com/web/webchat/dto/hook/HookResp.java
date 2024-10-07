package com.web.webchat.dto.hook;


import lombok.*;
import org.apache.poi.ss.formula.functions.T;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HookResp<T> {

    private String code;

    private boolean success;

    private T data;

    private String msg;

    public  HookResp success(T data){
          return HookResp.builder().success(true).data(data).build();
    }
}
