package Rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

@Path("/artical")
public class ArticalService {
	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;
		
	public static String FAIL = "fail";
		
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getArticals(){
		Artical artical = new Artical();
//		artical.setUuid(Long.toHexString(new Random().nextLong()));
		ArticalApi.addArtical(artical);
		return "hello";
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
					String childPath = FileManager.saveImage(toLocal, inputStream);
					urlMap.put(name.replace("image:", ""), toRemote+childPath);
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
				String childPath = FileManager.saveArtical(toLocal, rawHtml.getBytes());
				ans = toRemote+childPath;
			}
			
			artical.setImageUri(urlMap.get(artical.getImageUri()));
			artical.setArticalHref(ans);
			
			ArticalApi.addArtical(artical);
			
		}
		System.out.println(ans);

		return ans;
	}

}
