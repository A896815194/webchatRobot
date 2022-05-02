package com.web.webchat.inteface;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public interface Handler extends InitializingBean {

    List<String> createMessage(RequestDto request, PropertiesEntity propertiesEntity);

}
