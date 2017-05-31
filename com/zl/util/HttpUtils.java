package com.zl.util;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpUtils
{
	public static CloseableHttpClient client;
	static {
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(60000).setSocketTimeout(150000).build();
		PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = createSSLConnection();
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
		//设置默认连接池
		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
		//长连接策略 可以针对某个网站,设置保持连接的时间
		ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy()
		{
			
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context)
			{
				BasicHeaderElementIterator headerElement = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while(headerElement.hasNext()) {
					HeaderElement header = headerElement.nextElement();
					String name = header.getName();
					String value = header.getValue();
					if (value != null && name.equalsIgnoreCase("timeout")) {
						try
						{
							return Long.parseLong(value) * 1000;
						} catch (NumberFormatException e)
						{
						}
					}
				}
				HttpHost host = (HttpHost)context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
				if ("www.tomtop.com.cn".equalsIgnoreCase(host.getHostName())) {
					return 120 * 1000;
				} else {
					return 60 * 1000;
				}
			}
		};
		//转发策略
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		client = HttpClients.custom().setDefaultRequestConfig(config).setConnectionManager(cm)
							.setKeepAliveStrategy(keepAliveStrategy).setRedirectStrategy(redirectStrategy).build();
	}
	
	static  SSLConnectionSocketFactory createSSLConnection() {
		SSLConnectionSocketFactory sslf = null;
		try{
			SSLContext sslc = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				
				public boolean isTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
					return true;
				}
			}).build();
			sslf = new SSLConnectionSocketFactory(sslc, new X509HostnameVerifier() {
				
				public boolean verify(String s, SSLSession sslsession) {
					return true;
				}
				
				@Override
				public void verify(String s, String[] as, String[] as1) throws SSLException {
					
				}
				
				@Override
				public void verify(String s, X509Certificate x509certificate) throws SSLException {
					
				}
				
				@Override
				public void verify(String s, SSLSocket sslsocket) throws IOException {
					
				}
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sslf;
	}
	
	
	public static String post(String requestBody, String url) {
		String responString = "";
		HttpPost post = new HttpPost(url);
		try {
			StringEntity entity = new StringEntity(requestBody, ContentType.create("application/json", Consts.UTF_8));
			// 发送http请求
			post.setEntity(entity);
			post.setHeader("content-type", "application/json;charset=utf-8");
			CloseableHttpResponse response = client.execute(post);
			// 打印返回的信息
			responString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			post.releaseConnection();
		}
		return responString;
	}
	public static String get(Map<String, String> params, String url) {
		String responString = "";
		HttpGet get = new HttpGet(url);
		List<BasicNameValuePair> nvps = new ArrayList<>();
		Iterator<String> paramsIterator = params.keySet().iterator();
		while (paramsIterator.hasNext())
		{
			String key = paramsIterator.next();
			nvps.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
		}
		try {
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
			
			CloseableHttpResponse response = client.execute(get);
			// 打印返回的信息
			responString = EntityUtils.toString(response.getEntity(),Consts.UTF_8);
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			get.releaseConnection();
		}
		return responString;
	}
	
	/**用httpclient 来实现 上传文件和图片*/
    public static  String postFile(String url,Map<String,String> params,Map<String,File> fileParams) {
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    	String res = "";
    	try {
    		if (params != null && !params.isEmpty()) {
    			for (String key : params.keySet())
				{
    				entityBuilder.addPart(key,new StringBody(params.get(key), ContentType.create("text/plain",Consts.UTF_8)));
				}
    		}
    		entityBuilder.setBoundary("----------------------------file");
    		if (fileParams != null && !fileParams.isEmpty()) {
    			for (String name : fileParams.keySet())
				{
    				entityBuilder.addPart(name,new FileBody(fileParams.get(name),ContentType.create("image/jpeg",Consts.UTF_8),name));
				}
    		}
    		post.setEntity(entityBuilder.build());
    		CloseableHttpResponse response = client.execute(post);
    		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    			//成功
    			res = EntityUtils.toString(response.getEntity());
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}finally {
    		post.abort();
    		try
			{
				client.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
    	}
    	return res;
    }
}
