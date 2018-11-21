package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class loginServlet2 {
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("正在登陆中，请稍等");
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		System.out.println("登录成功！欢迎回家");

		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "r");) {
			int a = 0;
			int b = 0;
			File file = new File("webapps/myweb/login_fail.html");
			for (int i = 0; i < raf.length() / 100; i++) {
				// 每次读取100字节
				raf.seek(i * 100);
				//
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data, "UTF-8").trim();

				raf.read(data);
				// String nickname = new String(data, "UTF-8").trim();
				// int age = raf.readInt();
				System.out.println(username + "," + password);
				System.out.println("pos:" + raf.getFilePointer());
				if (username.equals(name)) {
					raf.read(data);
					String word = new String(data, "UTF-8").trim();
					a++;
					if (password.equals(word)) {
						file = new File("webapps/myweb/login_success.html");
						b++;
						return;
					}

				}

			}
			if (a > b) {
				file = new File("webapps/myweb/passworddont.html");
			} else if (b== a) {
				file = new File("webapps/myweb/namedont.html");
			}
			response.setEntity(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
