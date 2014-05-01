#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

public class LogbackUtils {
    public static void loadProperties(String path) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
           JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            configurator.doConfigure(path);
        } catch (JoranException je) {
            je.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
