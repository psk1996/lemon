package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * �������
 * �����ÿ��ʵ�����ڱ�ʾ�ͻ��˷��͹�����һ������
 * ����������
 * 
 * ������������:
 * �����У���Ϣͷ����Ϣ����
 * @author adminitartor
 *
 */
public class HttpRequest {
	/*
	 * �����������Ϣ����
	 */
	//����ķ�ʽ
	private String method;
	//������Դ�ĳ���·��
	private String url;
	//����ʹ�õ�HttpЭ��汾
	private String protocol;
	//url�е����󲿷�
	private String requestURI;
	//url�еĲ�������
	private String queryString;
	//����ÿһ������Ĳ���
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
	/*
	 * ��Ϣͷ�����Ϣ����
	 */
	//key:��Ϣͷ������      value:��Ϣͷ��Ӧ��ֵ
	private Map<String,String> headers = new HashMap<String,String>();
	
	/*
	 * ��Ϣ���������Ϣ����
	 */
	
	
	/*
	 * ��ͻ���������صĶ���
	 */
	private Socket socket;
	private InputStream in;
	
	/**
	 * ���췽����������ʼ���������
	 * ��ʼ���������Ĺ��̣����Ƕ�ȡ�������ͻ���
	 * ���͹���������Ĺ��̡�
	 * �Դ˹��췽��Ҫ��Socket���룬�Դ˻�ȡ����
	 * ����ȡ�ͻ��˷��͵��������ݡ�
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException{
		System.out.println("HttpRequest:��ʼ������...");
		/*
		 * ����һ�������Ϊ����:
		 * 1:����������
		 * 2:������Ϣͷ
		 * 3:������Ϣ����
		 */
		try {
			this.socket = socket;
			//ͨ��Socket��ȡ����������ȡ�ͻ��˷��͵���������
			this.in = socket.getInputStream();
			
			//1
			parseRequestLine();
			//2
			parseHeaders();
			//3
			parseContent();
			
		}catch(EmptyRequestException e){
			//�����󲻴���ֱ���׸�ClientHandler
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest:��ʼ���������!");
	}
	/**
	 * ����������
	 * @throws EmptyRequestException 
	 */
	private void parseRequestLine() throws EmptyRequestException{
		System.out.println("��ʼ����������...");
		//��ȡһ���ַ���
		String line = readLine();
		System.out.println("����������:"+line);
		if("".equals(line)){
			//������,�׳��������쳣
			throw new EmptyRequestException();
		}
		/*
		 * ���������е�������Ϣ�ֱ����������
		 * ���õ���Ӧ������������:
		 * method,url,protocol
		 */
		String[] data = line.split("\\s");
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);
		
		//��һ������url����
		parseUrl();
		
		System.out.println("�������������!");
	}
	/**
	 * ��һ������url����
	 */
	private void parseUrl(){
		/*
		 * ����url�п��ܺ����û����ݵĲ������֣�
		 * �Դ�����Ҫ��url���н�һ������������
		 * 
		 * ����Ҫ���жϵ�ǰurl�Ƿ��в������ж�
		 * ���ݿ��Ը���url���Ƿ���"?"��������
		 * ��ʾ��url���в������֣���û�����ʾ��ǰ
		 * url�����в�����
		 * 
		 * �������в�������ֱ�ӽ�url��ֵ��ֵ��
		 * ����:requestURI���ɡ�
		 * 
		 * �����в���:
		 * 1:�Ƚ�url����"?"���Ϊ�����֣���"?"
		 *   ������ݸ�ֵ������:requestURI
		 *   ��"?"�Ҳ����ݸ�ֵ������:queryString
		 * 
		 * 2:ϸ�ֲ������֣����������ְ���"&"��ֳ�
		 *   ÿһ��������Ȼ��ÿһ�������ٰ���"="���
		 *   Ϊ�����֣�����"="���Ϊ���������Ҳ�Ϊ
		 *   ����ֵ����ÿ�������Ĳ�������Ϊkey��
		 *   ����ֵ��Ϊvalue��ӵ�����:parameters
		 *   ���Map�У�����ɲ����Ľ�����
		 * 
		 */
		
		System.out.println("��һ������url����������");

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
		System.out.println("url�������!");
		
	}
	
	
	/**
	 * ������Ϣͷ
	 */
	private void parseHeaders(){
		System.out.println("��ʼ������Ϣͷ...");
		/*
		 * ѭ������readLine��������ȡ���ɵ���Ϣͷ
		 * ��readLine��������ֵΪһ�����ַ���ʱ��
		 * ��Ӧ���ǵ�����ȡ����CRLF�����ǾͿ���ֹͣ
		 * ��ȡ�ˡ�
		 * 
		 * ����ȡ��һ����Ϣͷ�����ǿ��Խ��䰴��": "
		 * (ð�ſո�)���в�֣��������Բ�ֳ�����
		 * ��һ�������Ϣͷ�����֣��ڶ���Ϊ��Ϣͷ��ֵ
		 * ���ǽ�������Ϊkey����ֵ��Ϊvalue���浽
		 * ����headers���map�У�������ɽ�����Ϣͷ
		 * �Ĺ�����
		 */
		String line = null;
		while(!(line = readLine()).equals("")){
			System.out.println(line);
			String[] data = line.split(": ");
			headers.put(data[0], data[1]);
		}
		System.out.println("headrs:"+headers);
		System.out.println("��Ϣͷ�������!");
	}
	/**
	 * ������Ϣ����
	 */
	private void parseContent(){
		System.out.println("��ʼ������Ϣ����...");
		
		System.out.println("������Ϣ�������!");
	}
	
	/**
	 * ͨ���ͻ��˶�Ӧ���������ж�ȡ�ͻ��˷��͹�����
	 * һ���ַ���,��CRLF��Ϊһ�н����ı�־�����ص�
	 * �����ַ����в����к����CRLF��
	 * @return
	 */
	private String readLine(){
		try {
			StringBuilder builder = new StringBuilder();
			int d = -1;
			//c1��ʾ�ϴζ�ȡ�����ַ���c2��ʾ���ζ�ȡ�����ַ�
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
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
		public String getParameters(String name) {
			return this.parameters.get(name);
		}
	
	
	
	
	
	
}






