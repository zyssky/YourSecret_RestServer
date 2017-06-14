package Util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UrlContentDecoder {
	
	public static Map<String, String> getEntityMap(String urlencoded) {
		Map<String, String> map = new HashMap<String, String>();
		String decodedString = null ;
		try {
			decodedString = URLDecoder.decode(urlencoded,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(decodedString!=null){
			String[] pairs = decodedString.split("&");
			for(int i=0;i<pairs.length;i++){
				String[] content = pairs[i].split("=");
				if(content.length<2){
					map.put(content[0], null);
				}
				else{
					map.put(content[0], content[1]);
				}
			}
		}
		return map;
	}

}
