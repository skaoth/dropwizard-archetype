#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.resources.*;
import ${package}.util.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class DropwizardApplication extends Application<DropwizardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "Dropwizard Example API";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardConfiguration> bootstrap) {
        // TODO: application initialization
        // register bundles, commands, or jackson modules here
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(final DropwizardConfiguration configuration, final Environment environment) {
        initSwagger(environment, configuration);
        registerResources(configuration, environment);
        enableCORS(environment);
    }

    private void registerResources(DropwizardConfiguration configuration, Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
    }

    private void initSwagger(Environment env, DropwizardConfiguration configuration) {
        SimpleServerFactory ssf = (SimpleServerFactory) configuration.getServerFactory();
        new AssetsBundle("/swagger").run(env);
        env.jersey().register(new SwaggerResource(ssf.getApplicationContextPath(), "index.ftl"));

        BeanConfig config = new BeanConfig();
        config.setVersion("1.0.0");
        config.setSchemes(new String[]{"http"});
        config.setBasePath("/");
        config.setResourcePackage("io.swagger.resources");
        config.setScan(true);

        env.jersey().register(new ApiListingResource());
        env.jersey().register(new SwaggerSerializers());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
    }

    private void enableCORS(Environment environment) {
        final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
