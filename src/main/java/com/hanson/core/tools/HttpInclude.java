package com.hanson.core.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取response的输出流，使velocity能够在前台使用类似<jsp:include page="xxx.jsp">的功能
 * @author hanson
 *
 */
public class HttpInclude {

	private static final Logger log = LoggerFactory.getLogger(HttpInclude.class);
	
	private HttpServletResponse response;
	
	private HttpServletRequest request;
	
	public HttpInclude (HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	/**
	 * 将response的输出流  注入到StringWriter当中，输出到页面
	 * @param path 请求的路径
	 * @return
	 */
	public String include(String path){
		StringWriter sw = new StringWriter(8*1024);
		include(path, sw);
		return sw.toString();
		
	}
	
	public void include(String path, Writer writer){
		// 请求的是本地的服务器
		if(path!=null && !path.toLowerCase().startsWith("http://") && !path.toLowerCase().startsWith("https://")){ 
			try {
				getLocalContent(path, writer);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getLocalContent(String path, Writer writer) throws ServletException, IOException{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8*1024);
		WrapperResponse wrapperResponse = new WrapperResponse(this.response, writer, outputStream);
		String url_path = path.indexOf("?") > 0 ? path.substring(0, path.indexOf("?")) : path;
		String url_query = path.indexOf("?") > 0 ? path.substring(path.indexOf("?") + 1) : "";
		String[] params = url_query.split("&");
		for (String param : params) {//解析请求参数，封装到Request域中
		    if ((param != null) && (!param.equals(""))) {
			String[] list = param.split("=");
			if (list.length == 2)
			    this.request.setAttribute(list[0], list[1]);
		    }
		}
		this.request.getRequestDispatcher(url_path).include(this.request, wrapperResponse);
		wrapperResponse.flushBuffer();
		if (wrapperResponse.useOutputStream) {
		    writer.write(outputStream.toString(this.response.getCharacterEncoding()));
		}
		writer.flush();
	}
	
}

/**
 * 自定义的Response 包裹类     装饰着模式 
 * @author hanson
 *
 */
class WrapperResponse extends HttpServletResponseWrapper{

	public boolean useWriter = false;// 是否为响应字符流
	public boolean useOutputStream = false;// 是否为响应字节流
	private PrintWriter printWriter;// 响应字符流
	private ServletOutputStream servletOutputStream;// 响应字节流
	
	public WrapperResponse(HttpServletResponse response, Writer customWriter, final OutputStream customOutputStream) {
		super(response);
		this.printWriter = new PrintWriter(customWriter);
		this.servletOutputStream = new ServletOutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				customOutputStream.write(b);
			}
	
			public void write(byte[] b) throws IOException {
			    customOutputStream.write(b);
			}
	
			public void write(byte[] b, int off, int len) throws IOException {
			    customOutputStream.write(b, off, len);
			}
			
			@Override
			public void setWriteListener(WriteListener writeListener) {
				
			}
			
			@Override
			public boolean isReady() {
				return false;
			}
		};
	}
	
	/**
	 * 获得响应的字符输出流
	 */
	public PrintWriter getWriter() throws IOException {
	    if (this.useOutputStream)
		throw new IllegalStateException(
			"getOutputStream() has already been called for this response 有了OutputStream就不要用Writer了1");
	    this.useWriter = true;
	    return this.printWriter;
	}

	/**
	 * 获得响应的字节输出流
	 */
	public ServletOutputStream getOutputStream() throws IOException {
	    if (this.useWriter)
		throw new IllegalStateException("getWriter() has already been called for this response；有了Writer就不要用OutputStream");
	    this.useOutputStream = true;
	    return this.servletOutputStream;
	}

	/**
	 * 清除响应的输出流的缓存（Writer或者OutputStream）； 即强制向客户端输出
	 */
	public void flushBuffer() throws IOException {
	    if (this.useWriter)
		this.printWriter.flush();
	    if (this.useOutputStream)
		this.servletOutputStream.flush();
	}
	
}
