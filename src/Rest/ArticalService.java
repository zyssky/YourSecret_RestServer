package Rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
import javax.ws.rs.PathParam;
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
import Api.ImageApi;
import Model.Artical;
import Model.ArticalResponse;
import Util.FileManager;
import Util.UrlContentDecoder;
import antlr.collections.List;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;

@Path("/artical")
public class ArticalService {
	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;
	

		
	public static String FAIL = "fail";
	
    public static String ARTICLE_TYPE_HOT = "热点" ;
    public static String ARTICLE_TYPE_NOTICE = "推荐";
    public static String ARTICLE_TYPE_ARTICLE = "简书" ;
    
    public static String ARTICAL_CATOGORY_HOT = "附近热点";
    public static String ARTICAL_CATOGORY_GOOD= "附近简书";
    public static String ARTICAL_CATOGORY_PUSH = "附近推荐";
    public static String ARTICAL_CATOGORY_OUTSIDE = "外面的世界";
    public static double distance = 0.01;
    
	public static HashMap<String, String> map;
    
		
	@POST
	@Path("near")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ArrayList<Artical>> getArticals(@FormParam("latitude") String latitude,@FormParam("longitude") String longitude){
		double x = Double.parseDouble(latitude);
		double y = Double.parseDouble(longitude);
		double x1 = x-distance;
		double y1 = y+distance;
		double x2 = x+distance;
		double y2 = y-distance;
		
		int no = 0;
		
		Map<String, ArrayList<Artical>> map = new HashMap<String, ArrayList<Artical>>();
		map.put(ARTICAL_CATOGORY_HOT, ArticalApi.getNearArticals(no,ARTICLE_TYPE_HOT,x1,y1,x2,y2));
		map.put(ARTICAL_CATOGORY_PUSH, ArticalApi.getNearArticals(no,ARTICLE_TYPE_NOTICE,x1,y1,x2,y2));
		map.put(ARTICAL_CATOGORY_GOOD, ArticalApi.getNearArticals(no,ARTICLE_TYPE_ARTICLE,x1,y1,x2,y2));
		map.put(ARTICAL_CATOGORY_OUTSIDE, ArticalApi.getOutSideArticals(no,ARTICLE_TYPE_ARTICLE,x1,y1,x2,y2));
		
		return map;
	}
	
	@POST
	@Path("near/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Artical> getTypeArticals(@PathParam("type") String articalType,
			@FormParam("latitude") String latitude,@FormParam("longitude") String longitude,@FormParam("pageNO") String pageNo){
		double x = Double.parseDouble(latitude);
		double y = Double.parseDouble(longitude);
		double x1 = x-distance;
		double y1 = y+distance;
		double x2 = x+distance;
		double y2 = y-distance;
		
		int no = Integer.parseInt(pageNo);
		
		ArrayList<Artical> list = null;
		if(!articalType.equals(ARTICAL_CATOGORY_OUTSIDE))
			list = ArticalApi.getNearArticals(no,map.get(articalType),x1,y1,x2,y2);
		else {
			list = ArticalApi.getOutSideArticals(no, map.get(articalType), x1, y1, x2, y2);
		}
		
		return list;
	}
	
	@POST
	@Path("map")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Artical> getNearestArticals(@FormParam("latitude") String latitude,
			@FormParam("longitude") String longitude,@FormParam("pageNO") String pageNo){
		double x = Double.parseDouble(latitude);
		double y = Double.parseDouble(longitude);
		double x1 = x-distance;
		double y1 = y+distance;
		double x2 = x+distance;
		double y2 = y-distance;
		
		int no = Integer.parseInt(pageNo);
		
		return ArticalApi.getNearArticals(no, x1, y1, x2, y2);
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Artical> getUserArticals(@FormParam("token") String token){
		return ArticalApi.getUserArticals(token);
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteArtical(@FormParam("token") String token,@FormParam("articalHref") String articalHref){
		boolean status = ArticalApi.deleteArtical(token,articalHref);
		if(status){
			String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
			String toLocal = context.getRealPath("");
			
			String path = articalHref.replace(toRemote, toLocal);
			path = path.replace("/", File.separator);
			FileManager.deleteFile(path);
			
			ArrayList<String> list = ImageApi.removeImages(articalHref);
			for (String subpath : list) {
				FileManager.deleteFile(toLocal+subpath);
			}
			return "success";
		}
		return "fail";
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public ArticalResponse putArtical(FormDataMultiPart multiPart){

		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		String toLocal = context.getRealPath("");

		String articalHref = null;
		
		String clientUuid = null;
		
		Artical artical = new Artical();
		
		ArrayList<String> imageSubPaths = new ArrayList<String>();
		
		if(multiPart!=null){
			String rawHtml = null;
			
			Map<String, String> urlMap = new HashMap<String, String>();
			
			for (BodyPart item : multiPart.getBodyParts()) {
				String name = ((FormDataBodyPart) item).getName();
				System.out.println(name);
				
				//parse the multipart form
				if(name.startsWith("image")){
					InputStream inputStream = item.getEntityAs(InputStream.class);
					String filePath = FileManager.saveFile( toLocal, FileManager.ARTICAL_IMAGE_SUBPATH, FileManager.JPG, inputStream);
					urlMap.put(name.replace("image:", ""), toRemote+filePath.replace(File.separator, "/"));
					imageSubPaths.add(filePath);
					continue;
				}
				
				if(name.equals("html")){
					rawHtml = item.getEntityAs(String.class);
					continue;
				}
				
				if(name.equals("artical")){
					String entity = item.getEntityAs(String.class);
					Map<String, String> map = UrlContentDecoder.getEntityMap(entity);

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
					
					clientUuid = map.get("uuid");
					continue;
				}

			}
			
			//replace the <img.../> tag in the html
			if(rawHtml!=null){
				for (Entry<String, String> url : urlMap.entrySet()) {
					rawHtml = rawHtml.replace(url.getKey(), url.getValue());
				}
				articalHref = FileManager.saveFile(toLocal,FileManager.ARTICAL_CONTENT_SUBPATH,FileManager.HTML,rawHtml.getBytes());
				articalHref = toRemote+articalHref.replace(File.separator, "/");
			}
			
			//generate thumbnail
			String sourceLoaclPath = urlMap.get(artical.getImageUri()).replace(toRemote, toLocal);
			String targetLocalPath = sourceLoaclPath.replace("/image/", File.separator+"firstImage"+File.separator);
			String targetRemotePath = urlMap.get(artical.getImageUri()).replace("image", "firstImage");
			try {
				FileManager.createFile(targetLocalPath);
				Thumbnails.of(sourceLoaclPath)
				.size(200, 200)
				.toFile(targetLocalPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			//update the artical's imageUri and set the articalHref
			artical.setImageUri(targetRemotePath);
			artical.setArticalHref(articalHref);
			
			//write to database
			ArticalApi.addArtical(artical);
			ImageApi.addImages(articalHref, imageSubPaths);
			
		}
		System.out.println(articalHref);
		
		ArticalResponse response = new ArticalResponse();
		
		if(articalHref != null){
			
			response.setArticalClientUuid(clientUuid);
			response.setArticalHref(articalHref);
			response.setCode(200);
			response.setMessage("success");
			response.setImageUri(artical.getImageUri());
			
		}
		else{

			response.setCode(999);
			response.setMessage("fail");
		}
		
		return response;
	}

}
