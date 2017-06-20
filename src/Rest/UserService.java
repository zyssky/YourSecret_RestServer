package Rest;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONException;
import org.json.JSONObject;

import Api.UserApi;
import Model.User;
import Util.FileManager;

@Path("/user")
public class UserService {
	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;
	
	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@FormParam("identifier") String identifier){
		User user = UserApi.createOrUpdateToken(identifier);
		String ansString = null;
		if(user!=null){
			ansString = getAnswerString(200, "success", user.toJson());
		}
		else{
			ansString = getAnswerString(999, "fail", null);
		}
		return ansString;
	}
	
	@Path("register")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String register(@FormDataParam("phoneNum") String phoneNum,@FormDataParam("identifier") String identifier,
			@FormDataParam("nickName") String nickName,@FormDataParam("image") InputStream imageData){
		
		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		String toLocal = context.getRealPath("");
		
		User user = new User();
		user.setPhoneNum(phoneNum);
		user.setNickName(nickName);
		user.setIdentifier(identifier);
		String filePath = FileManager.saveFile(toLocal, FileManager.USER_ICON_SUBPATH, FileManager.JPG, imageData);
		user.setUserIconPath(filePath);
		if(UserApi.addUser(user)){
			return getAnswerString(200, "success", null);
		}
		else{
			return getAnswerString(999, "fail", null);
		}
	}
	
	@Path("modify")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String modify(@FormDataParam("token") String token,
			@FormDataParam("nickName") String nickName,@FormDataParam("image") InputStream imageData){
		
		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		String toLocal = context.getRealPath("");
		
		if(token!=null){
			User user = UserApi.getUser(token);
			if(nickName!=null)
				user.setNickName(nickName);
			if(imageData!=null){
				FileManager.deleteFile(toLocal+user.getUserIconPath());
				String filePath = FileManager.saveFile(toLocal, FileManager.USER_ICON_SUBPATH, FileManager.JPG, imageData);
				user.setUserIconPath(filePath);
			}
			if(UserApi.updateUser(user)){
				return getAnswerString(200, "success", user.toJson());
			}
			else{
				return getAnswerString(999, "fail", null);
			}
		}

		else{
			return getAnswerString(999, "fail", null);
		}
	}
	
	
	
	private String getAnswerString(int code,String message,Object data){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("code", code);
			jsonObject.put("message", message);
			jsonObject.put("data", data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
		
	}

}
