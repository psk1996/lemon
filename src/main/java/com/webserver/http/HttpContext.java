package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTP协议相关内容定义
 * @author adminitartor
 *
 */
public class HttpContext {
	/*
	 * 状态代码与状态描述的对应
	 * key:状态代码
	 * value:状态描述
	 */
	private static Map<Integer,String> statusReasonMapping = new HashMap<Integer,String>();
	/*
	 * 资源文件后缀与对应的介质类型定义的对应关系
	 * key:资源文件的后缀名
	 * value:Content-Type对应的值
	 */
	private static Map<String,String> mimeMapping = new HashMap<String,String>();
	
	
	
	static{
		//初始化
		initStatusMapping();
		initMimeMapping();
	}
	/**
	 * 初始化介质类型映射
	 */
	private static void initMimeMapping(){
		/*
		 * 解析项目目录中的conf目录里的web.xml文件
		 * 将根元素中的所有子元素<mime-mapping>
		 * 解析出来，并将其中的子元素:
		 * <extension>中的文本作为key
		 * <mime-type>中的文本作为value
		 * 存入到mimeMapping这个Map中，完成
		 * 初始化操作。
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
	 * 初始化状态代码与对应的状态描述
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
	 * 根据给定的状态代码获取对应的状态描述
	 * @param code
	 * @return
	 */
	public static String getStatusReason(int code){
		return statusReasonMapping.get(code);
	}
	/**
	 * 根据给定的资源后缀名获取对应的Content-Type值
	 * @param ext 资源后缀名
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






