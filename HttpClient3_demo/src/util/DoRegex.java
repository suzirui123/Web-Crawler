package util;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DoRegex {
	private static String rootUrlRegex = "(http://.*?/)";
	private static String currentUrlRegex = "(http://.*/)";
	private static String ChRegex = "([\u4e00-\u9fa5]+)";
	
	public static String getString(String dealStr, String regexStr, String splitStr, int n){
		String reStr = "";
		if (dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) return reStr;
		splitStr = (splitStr == null) ? "" : splitStr;
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		StringBuffer stringBuffer = new StringBuffer();
		while (matcher.find()){
			stringBuffer.append(matcher.group(n).trim());
			stringBuffer.append(splitStr);
		}
		reStr = stringBuffer.toString();
		if (splitStr == "" && reStr.endsWith(splitStr)) reStr = reStr.substring(0, reStr.length()-splitStr.length());
		return reStr;
	}
	
	public static String getString(String dealStr, String regexStr, int n){
		return getString(dealStr, regexStr, null, n);
	}
	
	public static String getFirstString(String dealStr, String regexStr, int n){
		if (dealStr == "" || regexStr == null || n < 1 || dealStr.isEmpty()) return "";
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) return matcher.group(n).trim();
		return "";
	}
	
	public static List<String> getList(String dealStr, String regexStr, int n){
		List<String> reArrayList = new ArrayList<String>();
		if (dealStr == "" || regexStr == "" || n < 1 || dealStr.isEmpty()) return reArrayList;
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) reArrayList.add(matcher.group(n).trim());
		return reArrayList;
	}
	
	private static String getHttpUrl(String url, String currentUrl){
		try{
			url = encodeUrlCh(url);
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		if (url.indexOf("http") == 0) return url;
		if (url.indexOf("/") == 0) return getFirstString(currentUrl, rootUrlRegex, 1) + url.substring(1);
		return getFirstString(currentUrl, currentUrlRegex, 1) + url;
	}
//	获取和正则匹配的绝对链接地址
	public static List<String> getArrayList(String dealStr, String regexStr, String currentUrl, int n){
		List<String> reArrayList = new ArrayList<String>();
		if (dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) return reArrayList;
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) reArrayList.add(getHttpUrl(matcher.group(n).trim(), currentUrl));
		return reArrayList;
	}
	
	public static String encodeUrlCh(String url) throws UnsupportedEncodingException{
		while (true){
			String s = getFirstString(url, ChRegex, 1);
			if ("".equals(s)) return url;
			url = url.replaceAll(s, URLEncoder.encode(s, "utf-8"));
		}
	}
	
	public static List<String[]> getListArray(String dealStr, String regexStr, int[] array){
		List<String[]> reArrayList = new ArrayList<String[]>();
		if (dealStr == null || regexStr == null || array == null) return reArrayList;
		for (int i=0; i<array.length; i++){
			if (array[i] < 1) return reArrayList;
		}
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()){
			String[] ss = new String[array.length];
			for (int i=0; i<array.length; i++){
				ss[i] = matcher.group(array[i]).trim();
			}
			reArrayList.add(ss);
		}
		return reArrayList;
	}
	
	public static List<String> getStringArray(String dealStr, String regexStr, int[] array){
		List<String> reStringList = new ArrayList<String>();
		if (dealStr == null || regexStr == null || array == null) return reStringList;
		for (int i=0; i<array.length; i++){
			if (array[i] < 1) return reStringList;
		}
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()){
			StringBuffer sb = new StringBuffer();
			for (int i=0; i<array.length; i++){
				sb.append(matcher.group(array[i]).trim());
			}
			reStringList.add(sb.toString());
		}
		return reStringList;
	}
	
	public static String[] getFirstArray(String dealStr, String regexStr, int[] array){
		if (dealStr == null || regexStr == null || array == null) return null;
		for (int i=0; i<array.length; i++){
			if (array[i] < 1) return null;
		}
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()){
			String[] ss = new String[array.length];
			for (int i=0; i<array.length; i++){
				ss[i] = matcher.group(array[i]).trim();
			}
			return ss;
		}
		return null;
	}
}
