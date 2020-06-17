package com.bigdata.yls_apply.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.net.URLDecoder;


@ServerEndpoint(value = "/socket/{username}")
@Component
public class ChatSocket {
	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) {
		System.out.println(username+"打开websocket。");
		// 这个方法线程不安全
		Constant.nameAndSession.putIfAbsent(username, session);
	}

	@OnClose
	public void onClose(Session session, @PathParam("username") String username) {
		System.out.println(username+"关闭websocket。");
		Constant.nameAndSession.remove(username);
	}


	@OnMessage
	public void onMessage(Session session, String message, @PathParam("username") String username)  {
		System.out.println(username+"发送消息。");
		// 防止中文乱码
		String msg = "";
		try {
			msg = URLDecoder.decode(message, "UTF-8");
		}catch (Exception e){
			e.printStackTrace();
		}
		// 简单模拟群发消息
		String finalMsg = msg;
		Constant.nameAndSession.forEach((s, webSocketSession) -> {
			try {
				webSocketSession.getBasicRemote().sendText(username+":"+ finalMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
