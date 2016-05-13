package com.eversec.lte.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.eversec.lte.vo.CommandGetVo;

public class HttpUtil {

	/**
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 */
	public static boolean isPortBind(String host, int port) {
		boolean flag = false;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(host);
			try {
				new Socket(addr, port);
				flag = true;
			} catch (IOException e) {

			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		return flag;
	}

	public static String getUrlResp(String url) throws Exception {
		StringBuffer buffer = new StringBuffer();
		byte dst[] = new byte[1024 * 50];
		int byteread = 0;
		HttpURLConnection httpConn = null;
		URL sUrl = new URL(url);
		HttpURLConnection.setFollowRedirects(false);
		httpConn = (HttpURLConnection) sUrl.openConnection();
		httpConn.setRequestMethod("GET");
		httpConn.setConnectTimeout(10000);
		httpConn.setReadTimeout(10000);
		httpConn.connect();
		InputStream in = httpConn.getInputStream();
		while ((byteread = in.read(dst)) != -1) {
			buffer.append(new String(dst, 0, byteread, "UTF-8"));
		}
		httpConn.disconnect();
		return buffer.toString();
	}

	public static CommandGetVo getCommandResp(String host, int port,
			List<NameValuePair> nvps) {
		StringBuffer sbf = new StringBuffer("http://");
		sbf.append(host).append(":").append(port).append("/_command");
		URIBuilder ub = new URIBuilder();
		for (NameValuePair pair : nvps) {
			ub.addParameter(pair.getName(), pair.getValue());
		}
		String jsonString = null;
		CommandGetVo getVo = null;
		try {
			System.out.println(sbf.toString() + ub.toString());
			jsonString = getUrlResp(sbf.toString() + ub.toString());
			getVo = JSON.parseObject(jsonString, CommandGetVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getVo;
	}

	public static void main(String[] args) throws Exception {
		List<NameValuePair> pairs = new ArrayList<>();
		pairs.add(new BasicNameValuePair("size", "1"));
		pairs.add(new BasicNameValuePair("q",
				"INDEX=imsi_version SOURCETYPE=version version=1.09.88.01|TABLE imsi"));
		CommandGetVo getVo = getCommandResp("192.168.200.127", 9200, pairs);
		System.out.println(getVo);
		System.out.println(isPortBind("192.168.200.134", 2182));
	}

}