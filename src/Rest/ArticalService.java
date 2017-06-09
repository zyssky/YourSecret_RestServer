package Rest;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Api.ArticalApi;
import Model.Artical;

@Path("/artical")
public class ArticalService {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getArticals(){
		Artical artical = new Artical();
		artical.setUuid(Long.toHexString(new Random().nextLong()));
		ArticalApi.addArtical(artical);
		return "hello";
	}
	

}
