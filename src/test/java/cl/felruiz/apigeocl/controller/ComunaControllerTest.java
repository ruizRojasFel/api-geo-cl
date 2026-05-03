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
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.service.ComunaService;

/**
 * Integration Tests para ComunaController.
 * Prueba los endpoints HTTP incluyendo búsqueda normalizada.
 */

@WebMvcTest(ComunaController.class)
class ComunaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ComunaService comunaService;

  @Test
  @DisplayName("GET /api/v1/comunas debe retornar 200 con lista")
  void obtenerTodas_retorna200ConLista() throws Exception {
    // Arrange
    List<ComunaDTO> comunas = List.of(
        ComunaDTO.builder()
            .id(219L).nombre("Concepción").codigoCut("08101")
            .provinciaId(38L).provinciaNombre("Concepción")
            .regionId(11L).regionNombre("Biobío").build()
    );
    when(comunaService.obtenerTodas()).thenReturn(comunas);

    // Act & Assert
    mockMvc.perform(get("/api/v1/comunas"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")))
        .andExpect(jsonPath("$[0].codigoCut", is("08101")));
  }

  @Test
  @DisplayName("GET /api/v1/comunas/{id} con ID válido debe retornar 200")
  void obtenerPorId_conIdValido_retorna200() throws Exception {
    // Arrange
    ComunaDTO comuna = ComunaDTO.builder()
        .id(219L).nombre("Concepción").codigoCut("08101")
        .provinciaId(38L).provinciaNombre("Concepción")
        .regionId(11L).regionNombre("Biobío").build();
    when(comunaService.obtenerPorId(219L)).thenReturn(comuna);

    // Act & Assert
    mockMvc.perform(get("/api/v1/comunas/219"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre", is("Concepción")))
        .andExpect(jsonPath("$.regionNombre", is("Biobío")));
  }

  @Test
  @DisplayName("GET /api/v1/comunas/{id} con ID inexistente debe retornar 404")
  void obtenerPorId_conIdInexistente_retorna404() throws Exception {
    // Arrange
    when(comunaService.obtenerPorId(999L))
        .thenThrow(new ResourceNotFoundException("Comuna con id 999 no encontrada"));

    // Act & Assert
    mockMvc.perform(get("/api/v1/comunas/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.mensaje", containsString("999")));
  }

  @Test
  @DisplayName("GET /api/v1/comunas/buscar?nombre=concepcion debe retornar resultados")
  void buscarPorNombre_sinTilde_retornaResultados() throws Exception {
    // Arrange
    List<ComunaDTO> comunas = List.of(
        ComunaDTO.builder()
            .id(219L).nombre("Concepción").codigoCut("08101")
            .provinciaId(38L).provinciaNombre("Concepción")
            .regionId(11L).regionNombre("Biobío").build()
    );
    when(comunaService.buscarPorNombre("concepcion")).thenReturn(comunas);

    // Act & Assert
    mockMvc.perform(get("/api/v1/comunas/buscar").param("nombre", "concepcion"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")));
  }

  @Test
  @DisplayName("GET /api/v1/comunas/buscar sin parámetro debe retornar 400")
  void buscarPorNombre_sinParametro_retorna400() throws Exception {
    // Act & Assert → @RequestParam obligatorio, Spring retorna 400 automáticamente
    mockMvc.perform(get("/api/v1/comunas/buscar"))
        .andExpect(status().isBadRequest());
  }
}