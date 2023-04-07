package com.web.webchat.api;

import com.google.gson.Gson;
import com.web.webchat.dto.websocket.Message;
import com.web.webchat.dto.websocket.ServerEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint(value = "/websocket/", encoders = {ServerEncoder.class})
public class WebSocket {

    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static ConcurrentHashMap<String, WebSocket> serverMap = new ConcurrentHashMap<>();

    private Session session;

    private String ipAddr;

    private static AtomicInteger num = new AtomicInteger(0);

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Map<String, Object> userProperties = session.getUserProperties();
        this.ipAddr = "游客" + num.incrementAndGet();
        //this.ipAddr = (String) userProperties.get(WebSocketConfigurator.IP_ADDR);
        if (serverMap.containsKey(this.ipAddr)) {
            serverMap.remove(this.ipAddr);
            serverMap.put(this.ipAddr, this);
        } else {
            serverMap.put(this.ipAddr, this);
            addOnlineCount();
            log.info(this.ipAddr + "，已上线！");
        }
        Message message = new Message();
        message.setMsg("欢迎" + ipAddr + "加入游戏");
        message.setUserName(ipAddr);
        message.setOnlineCount(String.valueOf(getOnlineCount()));
        message.setType("2");
        message.setAction("welcome");
        groupSending(message, session);
    }

    /**
     * 服务器接收客户端发来的消息
     *
     * @param message 消息
     * @param session 会话session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务器收到了用户" + ipAddr + "发来的消息：" + message);
        Message reciver = new Gson().fromJson(message, Message.class);
        if(Objects.equals("heartBeat",reciver.getAction())) {
            try {
                reciver.setOnlineCount(String.valueOf(getOnlineCount()));
                session.getBasicRemote().sendObject(reciver);
            } catch (Exception e) {
                log.error("心跳检测发送异常");
            }
            return;
        }
        //方便前端测试
        Message msg = new Message();
        msg.setMsg(reciver.getMsg());
        msg.setUserName(ipAddr);
        msg.setOnlineCount(String.valueOf(getOnlineCount()));
        msg.setType("2");
        msg.setAction("broadcast");
        groupSendingWithOutSelf(msg, session);
    }

    /**
     * 给ip地址为ipAddr的客户端发送消息
     *
     * @param ipAddr  ip地址
     * @param message 消息
     */
    public static void sendMessage(String ipAddr, String message) {
        if (serverMap.containsKey(ipAddr)) {
            WebSocket webSocketServer = serverMap.get(ipAddr);
            Message msg = Message.builder().userName(ipAddr).action("private").msg(message).type("1").build();
            webSocketServer.sendMessage(msg);
        } else {
            log.error("发送失败，客户端未连接： " + ipAddr);
        }
    }

    //群发广播
    public void groupSending(Message message, Session exIncludeSession) {
        for (String name : serverMap.keySet()) {
            try {
                serverMap.get(name).session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                log.error("群发失败发送失败，客户端未连接： ", e);
            }
        }
    }
   // 群发不发自己
    public void groupSendingWithOutSelf(Message message, Session exIncludeSession) {
        for (String name : serverMap.keySet()) {
            try {
                if (exIncludeSession == serverMap.get(name).session)
                    continue;
                serverMap.get(name).session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                log.error("群发失败发送失败，客户端未连接： ", e);
            }
        }
    }


    /**
     * 服务器主动发送消息
     *
     * @param message 消息
     */
    public void sendMessage(Message message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获取在线人数
     *
     * @return 在线人数
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    @OnClose
    public void onClose() {
        if (serverMap.containsKey(ipAddr)) {
            serverMap.remove(ipAddr);
            subOnlineCount();
            log.info(ipAddr + "，已下线！");
            Message message = new Message();
            message.setMsg( ipAddr + "离开了游戏");
            message.setUserName(ipAddr);
            message.setOnlineCount(String.valueOf(getOnlineCount()));
            message.setType("2");
            message.setAction("baby");
            groupSending(message,null);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("用户" + ipAddr + "发生了错误，具体如下：" + throwable.getMessage());
        Message message = new Message();
        message.setMsg( ipAddr + "离开了游戏");
        message.setUserName(ipAddr);
        message.setOnlineCount(String.valueOf(getOnlineCount()));
        message.setType("2");
        message.setAction("baby");
        groupSending(message,null);
    }

    private static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

    public static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public static WebSocket get(String ipAddr) {
        return serverMap.get(ipAddr);
    }

    public static ConcurrentHashMap<String, WebSocket> getMap() {
        return serverMap;
    }

    public static boolean isOnline(String ipAddr) {
        return serverMap.containsKey(ipAddr);
    }

}
