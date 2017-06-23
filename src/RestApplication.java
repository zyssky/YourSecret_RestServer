import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import Rest.ArticalService;
import Rest.CommentService;
import Rest.UserService;

@ApplicationPath("/Rest/*")
public class RestApplication extends Application {

    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();

        // Add your resources.
        resources.add(ArticalService.class);
        resources.add(UserService.class);
        resources.add(CommentService.class);

        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);
        
        try {
			Class jsonProvider = Class.forName("org.glassfish.jersey.jackson.JacksonFeature");
			resources.add(jsonProvider);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Class jsonProvider = Class.forName("org.glassfish.jersey.moxy.json.MoxyJsonFeature");
        // Class jsonProvider = Class.forName("org.glassfish.jersey.jettison.JettisonFeature");

        return resources;
    }

}
