package com.chinobot.common.websocket.listener;

import com.chinobot.common.websocket.server.WebSocketServer;
import com.chinobot.framework.web.event.KafkaMsgEvent;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * kafka消息中间件推送到webSocket
 */
@Component
@Slf4j
public class KafkaPushWebSocket {

    @EventListener
    public void pushWebSocket(KafkaMsgEvent kafkaMsgEvent) {
        try {
            JSONObject jsonObject = new JSONObject ();
            jsonObject.put ("key", kafkaMsgEvent.getRecord ().key ());
            jsonObject.put ("msg", kafkaMsgEvent.getRecord ().value ());
            WebSocketServer.sendInfo (
                    kafkaMsgEvent.getRecord ().key (), jsonObject.toString ());
            //log.debug ("推送成功！" + kafkaMsgEvent.getRecord ());
        } catch (IOException e) {
            log.error ("消息推送失败！" + kafkaMsgEvent.getRecord ());
            e.printStackTrace ();
        }
    }
}
