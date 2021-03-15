package com.web.webchat.strategy;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.baidutextreview.BaiduTextReviewResponseDto;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.inteface.Handler;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.StrategyFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class BaiduTextReview implements Handler {

    private PropertiesEntity properties;

    @Override
    public List<String> createMessage(String msg, PropertiesEntity propertiesEntity) {
        return createFankui(getResponse(msg,properties.getBaiduTextReviewUrl()));

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(FunctionType.TextReview.name(), this);
    }

    private BaiduTextReviewResponseDto getResponse(String msg, String url){
        return RestTemplateUtil.sendMsgToBaiduTextReview(msg,url);
    }

    private List<String> createFankui(BaiduTextReviewResponseDto responseDto){
        List<String> msg = new ArrayList<>();
        if(isNull(responseDto) || 1 == responseDto.getConclusionType() || isEmpty(responseDto.getData())){
            return null;
        }else if(2 == responseDto.getConclusionType() && 12 == responseDto.getData().get(0).getType()){
              msg.add(responseDto.getData().get(0).getMsg()+"!请注意你的言论哦");
        }
        return msg;
    }

}
