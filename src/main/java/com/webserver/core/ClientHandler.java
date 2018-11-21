package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.function.LongToIntFunction;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.RegServlet;
import com.webserver.servlet.loginServlet2;

/**
 * 客户端处理类
 * 
 * @author adminitartor
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// 1 准备工作
			System.out.println("ClientHandler:开始解析请求...");
			HttpRequest request = new HttpRequest(socket);
			System.out.println("ClientHandler:解析请求完毕!");
			HttpResponse response = new HttpResponse(socket);

			// 2 处理请求
			System.out.println("ClientHandler:开始处理请求...");

			// 先获取用户请求的资源路径
			String url = request.getRequestURI();
			System.out.println(url);
			// 判断是否为请求注册业务
			if ("/myweb/reg".equals(url)) {
				RegServlet servlet = new RegServlet();
				servlet.service(request, response);
			} else if ("/myweb/longin".equals(url)) {
				loginServlet2 servlet = new loginServlet2();
				servlet.service(request, response);
			} else {
				File file = new File("webapps" + url);
				if (file.exists()) {
					System.out.println("资源已找到！");
					// 设置响应头
					response.putHeader("Content-Type", "text/html");
					response.putHeader("Content-Length", String.valueOf(file.length()));

					// 将资源设置到response中
					response.setEntity(file);
				} else {
					System.out.println("资源未找到！");
					// 响应404
					// 设置response中的状态代码为404
					response.setStatusCode(404);
					// 设置404页面
					File notFoundFile = new File("webapps/root/404.html");
					response.setEntity(notFoundFile);
				}
			}
			System.out.println("ClientHandler:处理请求完毕！");

			// 3发送响应
			System.out.println("ClientHandler:开始发送响应...");
			response.flush();
			System.out.println("ClientHandler:响应发送完毕！");

		} catch (

		EmptyRequestException e) {
			System.out.println("空请求");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 处理与客户端断开连接的操作
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
