#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.resources.*;
import ${package}.util.*;
import ch.qos.logback.classic.LoggerContext;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import com.wordnik.swagger.reader.ClassReaders$;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.slf4j.LoggerFactory;

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

        environment.addFilter(CrossOriginFilter.class, "/*")
        .setInitParam("allowedOrigins", "*")
        .setInitParam("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin")
       	.setInitParam("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
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

        SwaggerConfig config = ConfigFactory.config();
        config.setBasePath("/");
        ConfigFactory.setConfig(config);


        env.jersey().register(new ApiListingResourceJSON());
        env.jersey().register(new ApiDeclarationProvider());
        env.jersey().register(new ResourceListingProvider());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());
    }
}
