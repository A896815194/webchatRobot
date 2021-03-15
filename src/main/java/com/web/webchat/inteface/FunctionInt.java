package com.web.webchat.inteface;


import com.web.webchat.dto.RequestDto;

/**
 * 所有功能接口
 */
public interface FunctionInt {

     boolean openFunction(RequestDto request);

     boolean closeFunction(RequestDto request);
}
