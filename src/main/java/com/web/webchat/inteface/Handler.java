package com.web.webchat.inteface;

import com.web.webchat.config.PropertiesEntity;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public interface Handler extends InitializingBean {

    public List<String> createMessage(String msg, PropertiesEntity propertiesEntity);

}
