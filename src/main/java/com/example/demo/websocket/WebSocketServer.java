package com.example.demo.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收sid
     */
    private String sid="";
    /**
     * 静态变量，用来记录拨号
     */
    private static WebSocketServer bb   ;
    private static WebSocketServer bb1  ;

    /**
     * 连接建立成功调用的方法
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        //设置消息区大小
        session.setMaxTextMessageBufferSize(10888);
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        if(sid.equals("tongxin1")){

            bb=this;

        }else if(sid.equals("tongxin2")){
            bb1=this;
        }

        //在线数加1
        addOnlineCount();
        System.out.println("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.sid=sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            System.out.println("websocket IO异常");
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到来自窗口"+sid+"的信息:"+message);


        if (sid.equals("tongxin1") ) {
            String[]  data ;
            //如果是拨号服务器返回的数据
            data = message.split("//");
            //号码返回给发送者
            for (WebSocketServer item : webSocketSet) {
                try {
                    //这里可以设定只推送给这个sid的，为null则全部推送
                    if(sid==null) {
                        item.sendMessage(message);
                    }else if(item.sid.equals(data[0])){
                        //客户端返回来的数据发送给拨号服务器
//                        data date =new data();
                        item.sendMessage(data[1]);
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        }else if (sid.equals("tongxin2") ) {
            String[]  data ;
            //如果是拨号服务器返回的数据
            data = message.split("//");
            //号码返回给发送者
            for (WebSocketServer item : webSocketSet) {
                try {
                    //这里可以设定只推送给这个sid的，为null则全部推送
                    if(sid==null) {
                        item.sendMessage(message);
                    }else if(item.sid.equals(data[0])){
                        //客户端返回来的数据发送给拨号服务器
//                        data date =new data();
                        item.sendMessage(data[1]);
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        }else{
            //如果不是拨号服务返回数据
            for (WebSocketServer item : webSocketSet) {
                try {

                    //这里可以设定只推送给这个sid的，为null则全部推送
                    if(sid==null) {
                        item.sendMessage(message);
                    }else if(item.sid.equals(sid)){
                        //客户端返回来的数据发送给拨号服务器

                        if (ip.num == 1) {
                            bb.sendMessage(sid + "," + message);
                        } else if(ip.num == 2){
                            bb1.sendMessage(sid + "," + message);
                        }
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        }



//        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        System.out.println("推送消息到窗口"+sid+"，推送内容:"+message);
        this.session.getBasicRemote().sendText(message);
    }
    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        System.out.println("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
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

}
