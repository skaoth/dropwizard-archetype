#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.view;

import com.google.common.base.Charsets;
import io.dropwizard.views.View;

public class SwaggerView extends View {
    private final String swaggerAssetsPath;
    private final String contextPath;

    public SwaggerView(String applicationContextPath, String indexFile) {
        super(indexFile, Charsets.UTF_8);
        if(applicationContextPath.charAt(0) != '/') {
            applicationContextPath = '/' + applicationContextPath;
        }

        if (applicationContextPath.equals("/")) {
            swaggerAssetsPath = "/swagger";
            contextPath = "";
        } else {
            swaggerAssetsPath = applicationContextPath + "/swagger";
            contextPath = applicationContextPath;
        }
    }

    public String getSwaggerAssetsPath() {
        return swaggerAssetsPath;
    }

    public String getContextPath() {
        return contextPath;
    }
}
