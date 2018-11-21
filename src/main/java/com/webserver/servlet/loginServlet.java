package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class loginServlet {
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("正在登陆中，请稍等");
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		System.out.println("登录成功！欢迎回家");

		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "r");) {
			int a = 0;
			int b = 0;
			int index = 0;
			int index1 =0;
			
			for (int i = 0; i < raf.length() / 100; i++) {
				raf.seek(i*100);
				byte[]data = new byte[32];
				raf.read(data);
				String name = new String(data, "UTF-8").trim();
				if(username.equals(name)) {
					a++;
					break;
				}
				index++;
				}
			for (int i = 0; i < raf.length() / 100; i++) {
				raf.seek(i*100);
				byte[]data = new byte[32];
				raf.read(data);
				String name = new String(data, "UTF-8").trim();
				if(username.equals(name)) {
					b++;
					break;
				}
			index1++;
		}
			if(a>b) {
				File file = new File("webapps/myweb/passworddont.html");
				response.setEntity(file);
			}else if(b>a) {
				File file = new File("webapps/myweb/namedont.html");
				response.setEntity(file);
			}else if(a==b&&index==index1&&index!=raf.length()/100) {
				File file = new File("webapps/myweb/login_fail.html");
				response.setEntity(file);
			}
			
			
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
