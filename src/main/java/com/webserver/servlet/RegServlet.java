package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ����ע��ҵ��
 * @author Administrator
 *
 */
public class RegServlet {
public void service(HttpRequest request,HttpResponse response) {
	/*
	 * 1����ȡ�û���ע����Ϣ
	 * 2����ע����Ϣд���ļ�
	 * 3����Ӧע�������û�
	 */
	System.out.println("RegServlet:��ʼ����ע��ҵ��>>>>>>>");
	String username = request.getParameters("username");
	String password = request.getParameters("password");
	String nickname = request.getParameters("nickname");
	int age = Integer.parseInt(request.getParameters("age"));
	System.out.println("username:"+username);
	System.out.println("password:"+password);
	System.out.println("nickname:"+nickname);
	System.out.println("age:"+age);
	/*
	 * ���û���Ϣд���ļ�user.dat��
	 * ÿ���û�ռ100�ֽڣ������û����������ǳ�
	 * Ϊ�ַ�������ռ32�ֽڡ�����Ϊintռ�̶���4�ֽڡ�
	 */
	try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");)  {
		//�Ƚ�ָ���Ƶ�ĩβ
		raf.seek(raf.length());
		//д�û���
		byte[]data = username.getBytes("UTF-8");
		//���������ݵ�32
		data = Arrays.copyOf(data, 32);
		//һ����д��32���ֽ�
		raf.write(data);
		//д����
		data = password.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		//д�ǳ�
		data = nickname.getBytes("UTF-8");
		data = Arrays.copyOf(data, 32);
		raf.write(data);
		//д����
		raf.writeInt(age);
		//ע�����
		//���û���Ӧע��ɹ�ҳ��
		File file  = new File("webapps/myweb/reg_success.html");
		response.setEntity(file);
		
	}catch (Exception e) {
e.printStackTrace();
	}
	
	System.out.println("RegServlet:����ע��ҵ�����>>>>>>>");
	
}
}

class service{
	
}