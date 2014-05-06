#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.resources.*;
import ${package}.util.*;
import ch.qos.logback.classic.LoggerContext;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
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
    }

    @Override
    public void run(final DropwizardConfiguration configuration, final Environment environment) {
        registerResources(configuration, environment);

        //configureLogging(environment);
    }

    private void configureLogging(Environment environment) {
        // enable response/request logging
        environment.jersey().property(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, LoggingFilter.class.getName());
        environment.jersey().property(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, LoggingFilter.class.getName());

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        LogbackUtils.loadProperties("conf/logback.xml");
    }



    private void registerResources(DropwizardConfiguration configuration, Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
    }
}
