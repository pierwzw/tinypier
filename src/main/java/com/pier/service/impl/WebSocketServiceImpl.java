package com.pier.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint("/ws")
@Service
@Slf4j
public class WebSocketServiceImpl {

    // id记录是哪一个订单，应该设计成线程安全的
    private static final ThreadLocal<String> id = new ThreadLocal<>();
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServiceImpl对象。
    private static CopyOnWriteArraySet<WebSocketServiceImpl> webSocketSet = new CopyOnWriteArraySet<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     // 加入set中
        addOnlineCount();           // 在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        /*try {
            sendMessage("系统通知: 有新的连接加入了！！");
        } catch (IOException e) {
            log.error("IO异常", e);
        }*/
    }

    /**
     * 连接关闭调用的方法
     * 不要使用session发送消息给用户
     * 不要手动调用close方法
     * 不能有任何异常抛出
     * 不然会报：Message will not be sent because the WebSocket session has been closed
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  // 从set中删除
        subOnlineCount();           // 在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendInfo(String message, String orderId) {
        // orderid匹配才发送消息
        for (WebSocketServiceImpl item : webSocketSet) {
            String getedId = item.getId();
            // get()方法初始会返回null,必须要做检验
            if (getedId == null){
                continue;
            }
            if (getedId.equals(orderId)) {
                log.info("begin to send msg");
                item.sendMessage(message);
                break;
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误", error);
    }

    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    private void sendMessage(String message) {
        // 有同步与异步两种方式， 推荐使用异步的方式
        this.session.getAsyncRemote().sendText(message);
}
    /**
     * 返回在线数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 当连接人数增加时
     */
    private static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    /**
     * 当连接人数减少时
     */
    private static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

    public String getId(){
        return id.get();
    }

    public void setId(String id1){
        id.set(id1);
    }
}
