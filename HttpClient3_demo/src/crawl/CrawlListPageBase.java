package crawl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import util.DoRegex;

public abstract class CrawlListPageBase extends CrawlBase {
	private String pageurl;
	
	public CrawlListPageBase(String urlStr, String charsetName) throws IOException{
		readPageByGet(urlStr, charsetName);
		pageurl = urlStr;
	}
	
	public CrawlListPageBase(String urlStr, String charsetName, String method, HashMap<String, String> params) throws IOException{
		readPage(urlStr, charsetName, method, params);
		pageurl = urlStr;
	}
	
	public List<String> getPageUrls(){
		List<String> pageUrls = new ArrayList<String>();
		pageUrls = DoRegex.getArrayList(getPageSourceCode(), getUrlRegexString(), pageurl, getUrlRegexStringNum());
		return pageUrls;
	}
	
	public abstract String getUrlRegexString();
	
	public abstract int getUrlRegexStringNum();
}
