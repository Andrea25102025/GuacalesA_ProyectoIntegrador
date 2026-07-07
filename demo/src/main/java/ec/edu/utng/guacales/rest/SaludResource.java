package ec.edu.utng.guacales.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Endpoint de salud para que los frontends y UTNGolCoin verifiquen
 * que el Servicio de Estadísticas está en línea.
 */
@Path("/salud")
@Produces(MediaType.APPLICATION_JSON)
public class SaludResource {

    @GET
    public Response salud() {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", "OK");
        cuerpo.put("servicio", "estadisticas");
        cuerpo.put("marcaTiempo", Instant.now().toString());
        return Response.ok(cuerpo).build();
    }
}
