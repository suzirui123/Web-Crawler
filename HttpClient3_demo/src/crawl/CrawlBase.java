package crawl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public abstract class CrawlBase {
	private static Logger log = Logger.getLogger(CrawlBase.class);
	private String pageSourceCode = "";
	private Header[] responseHeaders = null;
	private static int connectTimeout = 3500;
	private static int readTimeout = 3500;
	private static int maxConnectTimes = 3;
	private static String charsetName = "iso-8859-1";
	private static HttpClient httpClient = new HttpClient();
	
	static {
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
	}
	
	public boolean readPage(String urlStr, String charsetName, String method, HashMap<String, String>params) throws HttpException, IOException{
		if ("post".equals(method) || "POST".equals(method)) return readPageByPost(urlStr, charsetName, params);
		else return readPageByGet(urlStr, charsetName, params);
	}

	private boolean readPageByPost(String urlStr, String charsetName2, HashMap<String, String> params) throws HttpException, IOException{
		PostMethod postMethod = createPostMethod(urlStr, params);
		return readPage(postMethod, charsetName, urlStr);
	}

	private PostMethod createPostMethod(String urlStr, HashMap<String, String> params){
		PostMethod postMethod = new PostMethod(urlStr);
		if (params == null) return postMethod;
		Iterator<Entry<String, String>> iter = params.entrySet().iterator();
		while (iter.hasNext()){
			Map.Entry<String, String> entry = iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			postMethod.setParameter(key, val);
		}
		return postMethod;
	}

	private boolean readPageByGet(String urlStr, String charsetName, HashMap<String, String> params) throws HttpException, IOException{
		GetMethod getMethod = createGetMethod(urlStr, params);
		return readPage(getMethod, charsetName, urlStr);
	}

	private GetMethod createGetMethod(String urlStr, HashMap<String, String> params){
		GetMethod getMethod = new GetMethod(urlStr);
		if (params == null) return getMethod;
		Iterator iter = params.entrySet().iterator();
		while (iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			getMethod.setRequestHeader(key, val);
		}
		return getMethod;
	}
	
	private boolean readPage(HttpMethod method, String defaultCharset, String urlStr) throws HttpException, IOException{
		int n = maxConnectTimes;
		while (n>0){
			try{
				if (httpClient.executeMethod(method) != HttpStatus.SC_OK){
					log.error("can't connect"+urlStr+"\t"+(maxConnectTimes-n+1)+"\t"+httpClient.executeMethod(method));
					n--;
				}else{
					responseHeaders = method.getResponseHeaders();
					InputStream inputStream = method.getResponseBodyAsStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
					StringBuffer stringBuffer = new StringBuffer();
					String lineString = null;
					while ((lineString = bufferedReader.readLine())!=null){
						stringBuffer.append(lineString);
						stringBuffer.append("\n");
					}
					pageSourceCode = stringBuffer.toString();
					InputStream in = new ByteArrayInputStream(pageSourceCode.getBytes(charsetName));
					String charset = CharsetUtil.getStreamCharset(in, defaultCharset);
					if ("Big5".equals(charset)) charset = "gbk";
					if (!charsetName.toLowerCase().equals(charset.toLowerCase())) pageSourceCode = new String(pageSourceCode.getBytes(charsetName), charset);
					return true;
				}
			}catch (Exception e){
				e.printStackTrace();
				System.out.println(urlStr+"--can't connect"+(maxConnectTimes-n+1));
				n--;
			}
		}
		return false;
	}
	
	public boolean readPageByGet(String urlStr, String charsetName) throws IOException{
		return this.readPageByGet(urlStr, charsetName, null);
	}
	
	public String getPageSourceCode(){
		return pageSourceCode;
	}
	
	public Header[] getHeader(){
		return responseHeaders;
	}
	
	public void setConnectTimeout(int timeout){
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
	}
	
	public void setReadTimeout(int timeout){
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
	}
	
	public static void setMaxConnectTimes(int maxConnectTimes){
		CrawlBase.maxConnectTimes = maxConnectTimes;
	}
	
	public void setTimeout(int connectTimeout, int readTimeout){
		setConnectTimeout(connectTimeout);
		setReadTimeout(readTimeout);
	}
	
	public static void setCharsetName(String charsetName){
		CrawlBase.charsetName = charsetName;
	}
}
