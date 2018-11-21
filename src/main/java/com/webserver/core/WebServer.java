package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer����
 * @author adminitartor
 *
 */
public class WebServer {
	private ServerSocket server;
	
	public WebServer(){
		try {
			server = new ServerSocket(8088);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		try {
			/*
			 * ��ʱ���ṩ�ͻ��˵Ķ�����ӡ��Ƚ�һ��
			 * �����������Ϻ���֧�ָù��ܡ�
			 */
			while(true){
				System.out.println("等待客户端连接。。。");
				Socket socket = server.accept();
				System.out.println("一个客户端连接了！");
				//�����̴߳���ͻ�������
				ClientHandler handler 
					= new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}
















