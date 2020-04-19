package com.chinobot.common.websocket.server;

import com.chinobot.common.utils.ResultFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @date: 2019/4/10
 */
@Component
@Slf4j
@ServerEndpoint("/api/websocket")
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。把它设计成线程安全的。
    private static AtomicInteger atomicInteger = new AtomicInteger (0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet ();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private Set<String> sid = new HashSet<> ();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add (this);                       //加入set中
        atomicInteger.incrementAndGet ();           //在线数加1
        log.info ("有新窗口开始监听:" + sid + ",当前在线人数为" + atomicInteger.get ());
        try {
            sendMessage (ResultFactory.success ().toString ());
        } catch (IOException e) {
            log.error ("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove (this);               //从set中删除
        atomicInteger.decrementAndGet ();           //在线数减1
        log.info ("有一连接关闭！当前在线人数为" + atomicInteger.get ());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     *                格式为逗号隔开人频道，例如：robot_trail,robot_flight,robot_gather_data
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug ("收到来自窗口" + sid + "的信息:" + message);
        if("ping".equals(message)) return;
        // 订阅消息
        for (WebSocketServer item : webSocketSet) {
            if(item.session.getId ().equals (session.getId ())){
                item.sid.addAll (Arrays.asList (message.split (",")));
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error ("发生错误");
        error.printStackTrace ();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote ().sendText (message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo( String sid, String message) throws IOException {
        //log.info ("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage (message);
                } else if (item.sid.contains (sid)) {
                    item.sendMessage (message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }
}

