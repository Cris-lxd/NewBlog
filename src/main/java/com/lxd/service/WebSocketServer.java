package com.lxd.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.Session;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: Cris_liuxd
 * @Date: 2021/05/18/16:07
 * @Description:
 **/

@Slf4j
@Service
@ServerEndpoint("/api/websocket/{sid}")
@Component
public class WebSocketServer {
    //当前在线数
    private static int onlineCount = 0;
    //存放每个客户端对应的mywebsocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private Session session;
    //接受sid
    private String sid = "";

    /**
     * 连接成功调用的方法
     *
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(@PathVariable("sid") String sid) {
        //this.session = session;
        webSocketSet.add(this);
        this.sid = sid;
        addOnlineCount();   //请求一次在线数加1
        try {
            sendMessage("conn_success");
            log.info("有新窗口开始监听：" + sid + ",当前在线人数为：" + getOnlineCount());
        } catch (IOException e) {
            log.error("websocket IOException");
        }
    }

    /**
     * 关闭连接后调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();    //在线数减1
        log.info("释放的id为" + sid);
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息调用的方法
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息：" + message);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     *
     * @param message
     * @param sid
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) {
        log.info("推送消息到窗口" + sid + ",推送内容：" + message);

        for (WebSocketServer item : webSocketSet) {
            try {
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }


}