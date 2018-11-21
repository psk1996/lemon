package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTPЭ��������ݶ���
 * @author adminitartor
 *
 */
public class HttpContext {
	/*
	 * ״̬������״̬�����Ķ�Ӧ
	 * key:״̬����
	 * value:״̬����
	 */
	private static Map<Integer,String> statusReasonMapping = new HashMap<Integer,String>();
	/*
	 * ��Դ�ļ���׺���Ӧ�Ľ������Ͷ���Ķ�Ӧ��ϵ
	 * key:��Դ�ļ��ĺ�׺��
	 * value:Content-Type��Ӧ��ֵ
	 */
	private static Map<String,String> mimeMapping = new HashMap<String,String>();
	
	
	
	static{
		//��ʼ��
		initStatusMapping();
		initMimeMapping();
	}
	/**
	 * ��ʼ����������ӳ��
	 */
	private static void initMimeMapping(){
		/*
		 * ������ĿĿ¼�е�confĿ¼���web.xml�ļ�
		 * ����Ԫ���е�������Ԫ��<mime-mapping>
		 * �����������������е���Ԫ��:
		 * <extension>�е��ı���Ϊkey
		 * <mime-type>�е��ı���Ϊvalue
		 * ���뵽mimeMapping���Map�У����
		 * ��ʼ��������
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("mime-mapping");
			for(Element mime : list){
				String key = mime.elementText("extension");
				String value = mime.elementText("mime-type");
				mimeMapping.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("mimeMapping:"+mimeMapping.size());
//		mimeMapping.put("html", "text/html");
//		mimeMapping.put("png", "image/png");
//		mimeMapping.put("jpg", "image/jpeg");
//		mimeMapping.put("gif", "image/gif");
//		mimeMapping.put("css", "text/css");
//		mimeMapping.put("js", "application/javascript");
		
	}
	/**
	 * ��ʼ��״̬�������Ӧ��״̬����
	 */
	private static void initStatusMapping(){
		statusReasonMapping.put(200, "OK");
		statusReasonMapping.put(201, "Created");
		statusReasonMapping.put(202, "Accepted");
		statusReasonMapping.put(204, "No Content");
		statusReasonMapping.put(301, "Moved Permanently");
		statusReasonMapping.put(302, "Moved Temporarily");
		statusReasonMapping.put(304, "Not Modified");
		statusReasonMapping.put(400, "Bad Request");
		statusReasonMapping.put(401, "Unauthorized");
		statusReasonMapping.put(403, "Forbidden");
		statusReasonMapping.put(404, "Not Found");
		statusReasonMapping.put(500, "Internal Server Error");
		statusReasonMapping.put(501, "Not Implemented");
		statusReasonMapping.put(502, "Bad Gateway");
		statusReasonMapping.put(503, "Service Unavailable");
	}
	/**
	 * ���ݸ�����״̬�����ȡ��Ӧ��״̬����
	 * @param code
	 * @return
	 */
	public static String getStatusReason(int code){
		return statusReasonMapping.get(code);
	}
	/**
	 * ���ݸ�������Դ��׺����ȡ��Ӧ��Content-Typeֵ
	 * @param ext ��Դ��׺��
	 * @return
	 */
	public static String getMimeType(String ext){
		return mimeMapping.get(ext);
	}
	
	public static void main(String[] args) {
		// distpicker.data.js   header.css
		String fileName = "header.css";
		int index = fileName.lastIndexOf(".");
		String ext = fileName.substring(index+1);
		String type = getMimeType(ext);
		System.out.println(type);
		
	}
}






