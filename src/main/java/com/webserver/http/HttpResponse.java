package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示要给客户端发送的实际响应
 * 内容。
 * @author adminitartor
 *
 */
public class HttpResponse {
	/*
	 * 状态行相关信息定义
	 */
	//状态代码
	private int statusCode = 200;
	
	
	/*
	 * 响应头相关信息定义
	 */
	//key:响应头名字     value:响应头的值
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	
	/*
	 * 响应正文相关信息定义
	 */
	//响应的实体文件
	private File entity;
	
	//和客户端连接相关的内容
	private Socket socket;
	private OutputStream out;
	
	
	public HttpResponse(Socket socket){
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将当前响应对象的内容以一个HTTP标准响应格式
	 * 发送给客户端
	 */
	public void flush(){
		/*
		 * 1:发送状态行
		 * 2:发送响应头
		 * 3:发送响应正文
		 */
		System.out.println("HttpResponse:开始发送响应...");
		sendStatusLine();
		sendHeaders();
		sendContent();
		System.out.println("HttpResponse:发送响应完毕!");
	}
	/**
	 * 发送状态行
	 */
	private void sendStatusLine(){
		try {
			System.out.println("开始发送状态行...");
			//发送状态行 
			String line = "HTTP/1.1"+" "+statusCode+" "+HttpContext.getStatusReason(statusCode);
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);//written CR
			out.write(10);//written LF			
			System.out.println("状态行发送完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送响应头
	 */
	private void sendHeaders(){
		try {
			System.out.println("开始发送响应头...");
			//发送响应头
			Set<Entry<String,String>> entrySet 
									= headers.entrySet();
			for(Entry<String,String> header : entrySet){
				String line = 
				  header.getKey()+": "+header.getValue();
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);//written CR
				out.write(10);//written LF
			}			
			//单独发送CRLF表示响应头部分发送完毕
			out.write(13);//written CR
			out.write(10);//written LF
			System.out.println("响应头发送完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送响应正文
	 */
	private void sendContent(){
		try (
			FileInputStream fis
				= new FileInputStream(entity);	
		){
			System.out.println("开始发送响应正文...");
			//发送响应正文
			int len = -1;
			byte[] data = new byte[1024*10];
			while((len = fis.read(data))!=-1){
				out.write(data, 0, len);
			}
			System.out.println("发送响应正文完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public File getEntity() {
		return entity;
	}
	/**
	 * 设置响应正文的实体文件，在设置的同时会自动
	 * 添加对应正文的两个响应头:
	 * Content-Type与Content-Length
	 * @param entity
	 */
	public void setEntity(File entity) {
		this.entity = entity;
		//根据该实体文件设置响应头
		//根据实体文件的后缀获取对应的介质类型
		String fileName = entity.getName();
		int index = fileName.lastIndexOf(".");
		String ext = fileName.substring(index+1);
		String type = HttpContext.getMimeType(ext);
		this.headers.put("Content-Type", type);
		this.headers.put("Content-Length", String.valueOf(entity.length()));
		
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * 设置响应头
	 * @param name  响应头的名字
	 * @param value 响应头对应的值
	 */
	public void putHeader(String name,String value){
		headers.put(name, value);
	}
	
	public String getHeader(String name){
		return headers.get(name);
	}
	
	
	
	
}








