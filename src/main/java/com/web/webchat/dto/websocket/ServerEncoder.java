package com.web.webchat.dto.websocket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

@Slf4j
public class ServerEncoder implements Encoder.Text<Message> {

    @Override
    public void destroy() {
        // 这里不重要
    }

    @Override
    public void init(EndpointConfig arg0) {
        // 这里也不重要

    }

    /*
     *  encode()方法里的参数和Text<T>里的T一致，如果你是Student，这里就是encode（Student student）
     */
    @Override
    public String encode(Message message) throws EncodeException {
        try {
            /*
             * 这里是重点，只需要返回Object序列化后的json字符串就行
             * 你也可以使用gosn，fastJson来序列化。
             */
            Gson gson = new Gson();
            return gson.toJson(message);

        } catch (Exception e) {
            log.error("转码异常", e);
            return null;
        }
    }

}
