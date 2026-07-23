package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.PartidoRequestDTO;
import ec.edu.utng.guacales.dto.SeleccionDTO;
import ec.edu.utng.guacales.model.Seleccion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EliminatoriasContractTest {

    @Test
    public void convierteUnaSeleccionSinGrupo() {
        Seleccion seleccion = new Seleccion();
        seleccion.setNombre("Ecuador");
        seleccion.setActivo(true);
        seleccion.setGrupo(null);

        SeleccionDTO dto = SeleccionResource.convertir(seleccion);

        assertEquals("Ecuador", dto.getNombre());
        assertNull(dto.getGrupo());
    }

    @Test
    public void conservaFaseOctavosSinGrupo() {
        PartidoRequestDTO partido = new PartidoRequestDTO();
        partido.setFase("OCTAVOS");
        partido.setGrupoId(null);

        assertEquals("OCTAVOS", partido.getFase());
        assertNull(partido.getGrupoId());
    }
}
