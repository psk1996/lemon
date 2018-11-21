package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 处理注册业务
 * @author Administrator
 *
 */
public class RegServlet {
public void service(HttpRequest request,HttpResponse response) {
	/*
	 * 1：获取用户的注册信息
	 * 2：将注册信息写入文件
	 * 3：响应注册结果给用户
	 */
	System.out.println("RegServlet:开始处理注册业务>>>>>>>");
	String username = request.getParameters("username");
	String password = request.getParameters("password");
	String nickname = request.getParameters("nickname");
	int age = Integer.parseInt(request.getParameters("age"));
	System.out.println("username:"+username);
	System.out.println("password:"+password);
	System.out.println("nickname:"+nickname);
	System.out.println("age:"+age);
	/*
	 * 将用户信息写入文件user.dat中
	 * 每个用户占100字节，其中用户名，密码昵称
	 * 为字符串。各占32字节。年龄为int占固定的4字节。
	 */
	try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");)  {
		//先将指针移到末尾
		raf.seek(raf.length());
		//写用户名
		byte[]data = username.getBytes("UTF-8");
		//将数组扩容到32
		data = Arrays.copyOf(data, 32);
		//一次性写出32个字节
		raf.write(data);
		//写密码
		data = password.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		//写昵称
		data = nickname.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		//写年龄
		raf.writeInt(age);
		//注册完毕
		//给用户响应注册成功页面
		File file  = new File("webapps/myweb/reg_success.html");
		response.setEntity(file);
		
	}catch (Exception e) {
e.printStackTrace();
	}
	
	System.out.println("RegServlet:处理注册业务完毕>>>>>>>");
	
}
}

class service{
	
}