package com.bigdata.yls_apply.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
	/***
     * 检测{@link javax.websocket.server.ServerEndpointConfig}和{@link ServerEndpoint} 类型的 bean，
     * 并在运行时使用标准 Java WebSocket 时注册。
     * 我们在{@link com.bigdata.yls_apply.websocket.WebSocketConfig}中就是使用@ServerEndpoint 去声明 websocket 服务
     */
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
