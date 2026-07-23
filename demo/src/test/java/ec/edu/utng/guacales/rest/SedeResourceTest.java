package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.SedeDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SedeResourceTest {

    @Test
    public void aceptaUnaSedeValida() {
        SedeDTO sede = sedeValida();

        assertNull(SedeResource.validar(sede));
    }

    @Test
    public void rechazaCamposObligatoriosVacios() {
        SedeDTO sede = sedeValida();
        sede.setCiudad(" ");

        assertEquals("La ciudad es obligatoria", SedeResource.validar(sede));
    }

    @Test
    public void rechazaCapacidadNoPositiva() {
        SedeDTO sede = sedeValida();
        sede.setCapacidad(0);

        assertEquals("La capacidad debe ser mayor que cero", SedeResource.validar(sede));
    }

    private SedeDTO sedeValida() {
        SedeDTO sede = new SedeDTO();
        sede.setNombre("Estadio Olímpico");
        sede.setCiudad("Quito");
        sede.setPais("Ecuador");
        sede.setCapacidad(35000);
        return sede;
    }
}
