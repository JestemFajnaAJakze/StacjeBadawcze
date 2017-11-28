package lab6;

import lab6.example.service.StationsREST;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.*;

@ApplicationPath("/*")
public class RESTApplication extends ResourceConfig {

    public RESTApplication() {

        packages("lab6");
        register(StationsREST.class);
        register(JacksonFeature.class);

    }
}
