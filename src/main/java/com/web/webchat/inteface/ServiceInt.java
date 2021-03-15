package com.web.webchat.inteface;


import com.web.webchat.dto.RequestDto;

public abstract class ServiceInt {

     public abstract void sendMessageToWechat(RequestDto request);

     public abstract boolean beforeSendMessageToWechat(RequestDto request);

     public abstract void afterSendMessageToWechat(RequestDto request);

     public boolean sendToWechat(RequestDto request){
          boolean isSendMsg = beforeSendMessageToWechat(request);
          boolean isSendFinish = false;
          if(isSendMsg) {
               sendMessageToWechat(request);
               isSendFinish = true;
          }
          afterSendMessageToWechat(request);
          return isSendFinish;
     }
}
