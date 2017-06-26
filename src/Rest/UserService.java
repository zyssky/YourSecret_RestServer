package Rest;

import java.io.File;
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
import Model.UserResponse;
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
	public UserResponse login(@FormParam("identifier") String identifier){
		User user = UserApi.createOrUpdateToken(identifier);

		System.out.println(identifier);
		UserResponse response = new UserResponse();
		response.code = 999;
		response.message = "fail";
		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		if(user!=null){
			response.code = 200;
			response.message = "success";
			response.nickName = user.getNickName();
			response.userIconPath = toRemote+user.getUserIconPath().replace(File.separator, "/");
			response.token = user.getToken();
			
		}
		return response;
	}
	
	@Path("register")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse register(@FormDataParam("phoneNum") String phoneNum,@FormDataParam("identifier") String identifier,
			@FormDataParam("nickName") String nickName){
		
		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		String toLocal = context.getRealPath("");
		
		System.out.println(identifier);
		
		String defaulticonPath = "/userIcon/default.jpg";
		
		UserResponse response = new UserResponse();
		response.code = 999;
		response.message = "fail";
		
		User user = new User();
		user.setPhoneNum(phoneNum);
		user.setNickName(nickName);
		user.setIdentifier(identifier);
//		String filePath = FileManager.saveFile(toLocal, FileManager.USER_ICON_SUBPATH, FileManager.JPG, imageData);
		user.setUserIconPath(defaulticonPath);
		if(UserApi.addUser(user)){
			response.code = 200;
			response.message = "success";
			response.userIconPath = defaulticonPath;
		}
		System.out.println(defaulticonPath);
		return response;
	}
	
	@Path("security")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse modify(@FormParam("phoneNum")String phoneNum,@FormParam("theold") String theOld,@FormParam("thenew")String theNew){
		UserResponse response = new UserResponse();
		response.code = 999;
		response.message = "fail";
		
		if(UserApi.modifyPassword(phoneNum,theOld,theNew)){
			response.code = 200;
			response.message = "success";
		}
		return response;
	}
	
	@Path("modify")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public UserResponse modify(@FormDataParam("token") String token,
			@FormDataParam("nickName") String nickName,@FormDataParam("image") InputStream imageData){
		
		String toRemote = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		String toLocal = context.getRealPath("");
		
		UserResponse response = new UserResponse();
		response.code = 999;
		response.message = "fail";
		
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
				response.code = 200;
				response.message = "success";
				
				response.nickName = user.getNickName();
				response.userIconPath =toRemote + user.getUserIconPath().replace(File.separator, "/");

			}
		}
		return response;
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
