package cl.felruiz.apigeocl.controller;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.service.ProvinciaService;

/**
 * Integration Tests para ProvinciaController.
 */

@WebMvcTest(ProvinciaController.class)
class ProvinciaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProvinciaService provinciaService;

  @Test
  @DisplayName("GET /api/v1/provincias debe retornar 200 con lista")
  void obtenerTodas_retorna200ConLista() throws Exception {
    // Arrange
    List<ProvinciaDTO> provincias = List.of(
        ProvinciaDTO.builder()
            .id(38L).nombre("Concepción").capital("Concepción")
            .regionId(11L).regionNombre("Biobío").build(),
        ProvinciaDTO.builder()
            .id(22L).nombre("Santiago").capital("Santiago")
            .regionId(7L).regionNombre("Metropolitana de Santiago").build());
    when(provinciaService.obtenerTodas()).thenReturn(provincias);

    // Act & Assert
    mockMvc.perform(get("/api/v1/provincias"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")))
        .andExpect(jsonPath("$[1].nombre", is("Santiago")));
  }

  @Test
  @DisplayName("GET /api/v1/provincias/{id} con ID válido debe retornar 200")
  void obtenerPorId_conIdValido_retorna200() throws Exception {
    // Arrange
    ProvinciaDTO provincia = ProvinciaDTO.builder()
        .id(38L).nombre("Concepción").capital("Concepción")
        .regionId(11L).regionNombre("Biobío").build();
    when(provinciaService.obtenerPorId(38L)).thenReturn(provincia);

    // Act & Assert
    mockMvc.perform(get("/api/v1/provincias/38"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre", is("Concepción")))
        .andExpect(jsonPath("$.regionNombre", is("Biobío")));
  }

  @Test
  @DisplayName("GET /api/v1/provincias/{id} con ID inexistente debe retornar 404")
  void obtenerPorId_conIdInexistente_retorna404() throws Exception {
    // Arrange
    when(provinciaService.obtenerPorId(999L))
        .thenThrow(new ResourceNotFoundException("Provincia con id 999 no encontrada"));

    // Act & Assert
    mockMvc.perform(get("/api/v1/provincias/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.mensaje", containsString("999")));
  }

  @Test
  @DisplayName("GET /api/v1/provincias/buscar?nombre=concepcion debe retornar resultados")
  void buscarPorNombre_sinTilde_retornaResultados() throws Exception {
    // Arrange
    List<ProvinciaDTO> provincias = List.of(
        ProvinciaDTO.builder()
            .id(38L).nombre("Concepción").capital("Concepción")
            .regionId(11L).regionNombre("Biobío").build());
    when(provinciaService.buscarPorNombre("concepcion")).thenReturn(provincias);

    // Act & Assert
    mockMvc.perform(get("/api/v1/provincias/buscar").param("nombre", "concepcion"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")));
  }

  @Test
  @DisplayName("GET /api/v1/provincias/buscar sin parámetro debe retornar 400")
  void buscarPorNombre_sinParametro_retorna400() throws Exception {
    mockMvc.perform(get("/api/v1/provincias/buscar"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.mensaje", containsString("nombre")));
  }

  @Test
  @DisplayName("GET /api/v1/provincias/{id}/comunas debe retornar 200 con comunas")
  void obtenerComunas_retorna200ConLista() throws Exception {
    List<ComunaDTO> comunas = List.of(
        ComunaDTO.builder()
            .id(219L).nombre("Concepción").codigoCut("08101")
            .provinciaId(38L).provinciaNombre("Concepción")
            .regionId(11L).regionNombre("Biobío").build());
    when(provinciaService.obtenerComunasPorProvincia(38L)).thenReturn(comunas);

    mockMvc.perform(get("/api/v1/provincias/38/comunas"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")));
  }
}
