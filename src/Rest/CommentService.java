package Rest;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Api.CommentApi;
import Model.Comment;
import antlr.collections.List;

@Path("/comment")
public class CommentService {
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String putComment(@FormParam("content") String content,@FormParam("articalHref") String articalHref,
			@FormParam("iconPath") String iconPath,@FormParam("nickName") String nickName,@FormParam("authorId") String authorId){
		Comment comment = new Comment(content,articalHref,iconPath,nickName,authorId);
		if(CommentApi.addComment(comment))
			return "success";
		return "fail";
	}
	
	@POST
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getComments(@FormParam("articalHref") String articalHref,@FormParam("commentPage")String commentPage){
		int pageNo = 0;
		try {
			pageNo = Integer.parseInt(commentPage);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return CommentApi.getComments(pageNo,articalHref);
	}
	
	@POST
	@Path("user")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getUserComments(@FormParam("token") String token,@FormParam("lastDate") String date){
		if(date==null)
			date = "0";
		return CommentApi.getUserComments(token,date);
	}
	

}
