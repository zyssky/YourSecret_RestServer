package Rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;
import org.junit.internal.runners.statements.Fail;

import Api.ArticalApi;
import Model.Artical;
import Util.FileManager;
import Util.UrlContentDecoder;
import antlr.collections.List;

@Path("/artical")
public class ArticalService {
	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;
	

		
	public static String FAIL = "fail";
	
    public static String ARTICAL_TYPE_SCENERY = "风景";
    public static String ARTICAL_TYPE_PERSON = "人物";
    public static String ARTICAL_TYPE_THING = "事物";
    public static String ARTICAL_TYPE_INTEREST = "值得";
    public static String ARTICAL_TYPE_ADS = "广告";
    
    public static String ARTICAL_CATOGORY_HOT = "附近热点";
    public static String ARTICAL_CATOGORY_GOOD= "附近简书";
    public static String ARTICAL_CATOGORY_PUSH = "附近推荐";
    public static String ARTICAL_CATOGORY_OUTSIDE = "外面的世界";
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ArrayList<Artical>> getArticals(){
		Map<String, ArrayList<Artical>> map = new HashMap<String, ArrayList<Artical>>();
		map.put(ARTICAL_CATOGORY_HOT, ArticalApi.getTodayArticals(ARTICAL_TYPE_THING));
		map.put(ARTICAL_CATOGORY_PUSH, ArticalApi.getTodayArticals(ARTICAL_TYPE_ADS));
		ArrayList<Artical> articals = ArticalApi.getTodayArticals(ARTICAL_TYPE_INTEREST);
//		articals.addAll(ArticalApi.getTodayArticals(ARTICAL_TYPE_INTEREST));
		articals.addAll(ArticalApi.getTodayArticals(ARTICAL_TYPE_PERSON));
		map.put(ARTICAL_CATOGORY_GOOD, articals);
		map.put(ARTICAL_CATOGORY_OUTSIDE, ArticalApi.getTodayArticals(ARTICAL_TYPE_SCENERY));
		
		return map;
		
		
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String putArtical(FormDataMultiPart multiPart){

		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		String toLocal = context.getRealPath("");
		
		System.out.println(toLocal);
		System.out.println(toRemote);

		String ans = null;
		
		if(multiPart!=null){
			String rawHtml = null;
			
			Artical artical = new Artical();
			
			
			Map<String, String> urlMap = new HashMap<String, String>();
			
			for (BodyPart item : multiPart.getBodyParts()) {
				String name = ((FormDataBodyPart) item).getName();
				System.out.println(name);
				
				if(name.startsWith("image")){
					InputStream inputStream = item.getEntityAs(InputStream.class);
					String filePath = FileManager.saveFile( toLocal, FileManager.ARTICAL_IMAGE_SUBPATH, FileManager.JPG, inputStream);
					urlMap.put(name.replace("image:", ""), toRemote+filePath);
					continue;
				}
				
				if(name.equals("html")){
					rawHtml = item.getEntityAs(String.class);
					continue;
				}
				
				if(name.equals("artical")){
					String entity = item.getEntityAs(String.class);
					Map<String, String> map = UrlContentDecoder.getEntityMap(entity);
//					for (Entry<String,String> pair : map.entrySet()) {
//						System.out.println(pair.getKey()+":"+pair.getValue());
//						
//					}
					artical.setAuthorId(map.get("authorId"));
					artical.setIntroduction(map.get("introduction"));
					artical.setLatitude(Double.parseDouble(map.get("latitude")));
					artical.setLongtitude(Double.parseDouble(map.get("longtitude")));
					artical.setLocationDesc(map.get("locationDesc"));
					artical.setTitle(map.get("title"));
					artical.setArticalType(map.get("articalType"));
					artical.setSaveType(Integer.parseInt(map.get("saveType")));
					artical.setImageUri(map.get("imageUri"));
					artical.setDate(new Date());
					continue;
				}

			}
			
			
			
			if(rawHtml!=null){
				for (Entry<String, String> url : urlMap.entrySet()) {
					rawHtml = rawHtml.replace(url.getKey(), url.getValue());
				}
				ans = FileManager.saveFile(toLocal,FileManager.ARTICAL_CONTENT_SUBPATH,FileManager.HTML,rawHtml.getBytes());
				ans = toRemote+ans;
			}
			
			artical.setImageUri(urlMap.get(artical.getImageUri()));
			artical.setArticalHref(ans);
			
			ArticalApi.addArtical(artical);
			
		}
		System.out.println(ans);

		return ans;
	}

}
