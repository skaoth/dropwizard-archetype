#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.resources;

import ${package}.core.Saying;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/helloworld")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "hello", description = "sample endpoint doc")
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    @ApiOperation(value = "sayhello", notes = "sample notes", response = Saying.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 200, message = "Status OK")})
    public Saying sayHello() {
        final String value = String.format(template, defaultName);
        return new Saying(counter.incrementAndGet(), value);
    }
}
