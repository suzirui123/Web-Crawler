package crawl;

import java.io.IOException;
import java.util.HashMap;
import crawl.CrawlListPageBase;

public class BaiduNewList extends CrawlListPageBase {
	private static HashMap<String, String> params;
	
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "https://www.baidu.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	}
	
	public BaiduNewList(String urlStr) throws IOException{
		super(urlStr, "utf-8", "get", params);
	}
	@Override
	public String getUrlRegexString() {
		// TODO Auto-generated method stub
		return "• <a href=\"(.*?)\"";
	}

	@Override
	public int getUrlRegexStringNum() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public static void main(String[] args) throws IOException{
		BaiduNewList baidu = new BaiduNewList("http://news.baidu.com/n?cmd=4&class=sportnews&pn=1&from=tab");
		for (String s: baidu.getPageUrls()){
			System.out.println(s);
		}
	}

}
