package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每个实例用于表示客户端发送过来的一个具体
 * 的请求内容
 * 
 * 请求有三部分:
 * 请求行，消息头，消息正文
 * @author adminitartor
 *
 */
public class HttpRequest {
	/*
	 * 请求行相关信息定义
	 */
	//请求的方式
	private String method;
	//请求资源的抽象路径
	private String url;
	//请求使用的Http协议版本
	private String protocol;
	//url中的请求部分
	private String requestURI;
	//url中的参数部分
	private String queryString;
	//保存每一个具体的参数
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
	/*
	 * 消息头相关信息定义
	 */
	//key:消息头的名字      value:消息头对应的值
	private Map<String,String> headers = new HashMap<String,String>();
	
	/*
	 * 消息正文相关信息定义
	 */
	
	
	/*
	 * 与客户端连接相关的定义
	 */
	private Socket socket;
	private InputStream in;
	
	/**
	 * 构造方法，用来初始化请求对象
	 * 初始化请求对象的过程，就是读取并解析客户端
	 * 发送过来的请求的过程。
	 * 对此构造方法要求将Socket传入，以此获取输入
	 * 流读取客户端发送的请求内容。
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException{
		System.out.println("HttpRequest:初始化请求...");
		/*
		 * 解析一个请求分为三步:
		 * 1:解析请求行
		 * 2:解析消息头
		 * 3:解析消息正文
		 */
		try {
			this.socket = socket;
			//通过Socket获取输入流，读取客户端发送的请求内容
			this.in = socket.getInputStream();
			
			//1
			parseRequestLine();
			//2
			parseHeaders();
			//3
			parseContent();
			
		}catch(EmptyRequestException e){
			//空请求不处理，直接抛给ClientHandler
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest:初始化请求完毕!");
	}
	/**
	 * 解析请求行
	 * @throws EmptyRequestException 
	 */
	private void parseRequestLine() throws EmptyRequestException{
		System.out.println("开始解析请求行...");
		//读取一行字符串
		String line = readLine();
		System.out.println("请求行内容:"+line);
		if("".equals(line)){
			//空请求,抛出空请求异常
			throw new EmptyRequestException();
		}
		/*
		 * 将请求行中的三个信息分别解析出来并
		 * 设置到对应的三个属性上:
		 * method,url,protocol
		 */
		String[] data = line.split("\\s");
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);
		
		//进一步解析url部分
		parseUrl();
		
		System.out.println("解析请求行完毕!");
	}
	/**
	 * 进一步解析url部分
	 */
	private void parseUrl(){
		/*
		 * 由于url中可能含有用户传递的参数部分，
		 * 对此我们要对url进行进一步解析操作。
		 * 
		 * 首先要先判断当前url是否含有参数，判断
		 * 依据可以根据url中是否含有"?"。若有则
		 * 表示该url含有参数部分，若没有则表示当前
		 * url不含有参数。
		 * 
		 * 若不含有参数，则直接将url的值赋值给
		 * 属性:requestURI即可。
		 * 
		 * 若含有参数:
		 * 1:先将url按照"?"拆分为两部分，将"?"
		 *   左侧内容赋值给属性:requestURI
		 *   将"?"右侧内容赋值给属性:queryString
		 * 
		 * 2:细分参数部分，将参数部分按照"&"拆分出
		 *   每一个参数，然后每一个参数再按照"="拆分
		 *   为两部分，其中"="左侧为参数名，右侧为
		 *   参数值。将每个参数的参数名作为key，
		 *   参数值作为value添加到属性:parameters
		 *   这个Map中，以完成参数的解析。
		 * 
		 */
		
		System.out.println("进一步解析url。。。。。");

		if (url.indexOf("?") != -1) {
			String[] date = url.split("\\?");
			this.requestURI = date[0];
			this.queryString = date[1];
			String[] arr = queryString.split("\\&");
			for (String i : arr) {
				String[] mdzz = i.split("\\=");
				if (mdzz.length > 1) {
					this.parameters.put(mdzz[0], mdzz[1]);
				} else {
					this.parameters.put(mdzz[0], null);
				}
			}
		} else {
			requestURI = url;
		}
	
		
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		System.out.println("url解析完毕!");
		
	}
	
	
	/**
	 * 解析消息头
	 */
	private void parseHeaders(){
		System.out.println("开始解析消息头...");
		/*
		 * 循环调用readLine方法，读取若干的消息头
		 * 当readLine方法返回值为一个空字符串时，
		 * 那应当是单独读取到了CRLF，这是就可以停止
		 * 读取了。
		 * 
		 * 当读取到一个消息头后，我们可以将其按照": "
		 * (冒号空格)进行拆分，这样可以拆分出两项
		 * 第一项就是消息头的名字，第二项为消息头的值
		 * 我们将名字作为key，将值作为value保存到
		 * 属性headers这个map中，最终完成解析消息头
		 * 的工作。
		 */
		String line = null;
		while(!(line = readLine()).equals("")){
			System.out.println(line);
			String[] data = line.split(": ");
			headers.put(data[0], data[1]);
		}
		System.out.println("headrs:"+headers);
		System.out.println("消息头解析完毕!");
	}
	/**
	 * 解析消息正文
	 */
	private void parseContent(){
		System.out.println("开始解析消息正文...");
		
		System.out.println("解析消息正文完毕!");
	}
	
	/**
	 * 通过客户端对应的输入流中读取客户端发送过来的
	 * 一行字符串,以CRLF作为一行结束的标志。返回的
	 * 这行字符串中不含有后面的CRLF。
	 * @return
	 */
	private String readLine(){
		try {
			StringBuilder builder = new StringBuilder();
			int d = -1;
			//c1表示上次读取到的字符，c2表示本次读取到的字符
			char c1='a',c2='a';
			while((d = in.read())!=-1){
				c2 = (char)d;
				if(c1==13&&c2==10){
					break;
				}
				builder.append(c2);
				c1 = c2;
			}
			return builder.toString().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getHeader(String name) {
		return headers.get(name);
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * 根据给定的参数名获取对应的参数值
	 * @param name
	 * @return
	 */
		public String getParameters(String name) {
			return this.parameters.get(name);
		}
	
	
	
	
	
	
}






