
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.resources;

import ${package}.view.SwaggerView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/docs")
@Produces(MediaType.TEXT_HTML)
public class SwaggerResource {
    private final String applicationContextPath;
    private final String indexFile;

    public SwaggerResource(String contextPath, String indexFile) {
        this.applicationContextPath = contextPath;
        this.indexFile = indexFile;
    }

    @GET
    public SwaggerView get() {
        return new SwaggerView(applicationContextPath, indexFile);
    }

}
