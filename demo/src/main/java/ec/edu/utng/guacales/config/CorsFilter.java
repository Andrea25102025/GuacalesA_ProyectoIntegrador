package ec.edu.utng.guacales.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * CORS orientado a los frontends públicos (:5080 / :5081) y al admin.
 * Responde preflight OPTIONS sin pasar al recurso.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, x-requested-with");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        responseContext.getHeaders().putSingle("Access-Control-Expose-Headers", "authorization, content-type");
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "86400");

        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(Response.Status.NO_CONTENT.getStatusCode());
        }
    }
}
