import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import Rest.ArticalService;

@ApplicationPath("/Rest/*")
public class RestApplication extends Application {

    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();

        // Add your resources.
        resources.add(ArticalService.class);

        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);

        return resources;
    }

}
