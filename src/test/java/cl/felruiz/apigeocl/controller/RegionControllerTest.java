package cl.felruiz.apigeocl.controller;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
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
import cl.felruiz.apigeocl.dto.RegionDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.service.RegionService;

/**
 * Integration Tests para RegionController.
 */

@WebMvcTest(RegionController.class)
class RegionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private RegionService regionService;

  @Test
  @DisplayName("GET /api/v1/regiones debe retornar 200 con lista de regiones")
  void obtenerTodas_retorna200ConLista() throws Exception {
    // Arrange
    List<RegionDTO> regiones = List.of(
        RegionDTO.builder().id(1L).numero("VIII").nombre("Biobío").capital("Concepción").build(),
        RegionDTO.builder().id(2L).numero("XIII").nombre("Metropolitana de Santiago").capital("Santiago").build());
    when(regionService.obtenerTodas()).thenReturn(regiones);

    // Act & Assert
    mockMvc.perform(get("/api/v1/regiones"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].nombre", is("Biobío")))
        .andExpect(jsonPath("$[1].nombre", is("Metropolitana de Santiago")));
  }

  @Test
  @DisplayName("GET /api/v1/regiones/{id} con ID válido debe retornar 200")
  void obtenerPorId_conIdValido_retorna200() throws Exception {
    // Arrange
    RegionDTO region = RegionDTO.builder()
        .id(1L).numero("VIII").nombre("Biobío").capital("Concepción").build();
    when(regionService.obtenerPorId(1L)).thenReturn(region);

    // Act & Assert
    mockMvc.perform(get("/api/v1/regiones/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre", is("Biobío")))
        .andExpect(jsonPath("$.capital", is("Concepción")));
  }

  @Test
  @DisplayName("GET /api/v1/regiones/{id} con ID inexistente debe retornar 404")
  void obtenerPorId_conIdInexistente_retorna404() throws Exception {
    // Arrange
    when(regionService.obtenerPorId(999L))
        .thenThrow(new ResourceNotFoundException("Región con id 999 no encontrada"));

    // Act & Assert
    mockMvc.perform(get("/api/v1/regiones/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.mensaje", containsString("999")));
  }

  @Test
  @DisplayName("GET /api/v1/regiones/buscar?nombre=nuble debe retornar 200 con resultados")
  void buscarPorNombre_conNombre_retorna200() throws Exception {
    // Arrange
    List<RegionDTO> regiones = List.of(
        RegionDTO.builder().id(10L).numero("XVI").nombre("Ñuble").capital("Chillán").build());
    when(regionService.buscarPorNombre("nuble")).thenReturn(regiones);

    // Act & Assert
    mockMvc.perform(get("/api/v1/regiones/buscar").param("nombre", "nuble"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Ñuble")));
  }

  @Test
  @DisplayName("GET /api/v1/regiones/{id}/provincias debe retornar 200 con provincias")
  void obtenerProvincias_retorna200ConLista() throws Exception {
    List<ProvinciaDTO> provincias = List.of(
        ProvinciaDTO.builder()
            .id(38L).nombre("Concepción").capital("Concepción")
            .regionId(11L).regionNombre("Biobío").build());
    when(regionService.obtenerProvinciasPorRegion(11L)).thenReturn(provincias);

    mockMvc.perform(get("/api/v1/regiones/11/provincias"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")));
  }

  @Test
  @DisplayName("GET /api/v1/regiones/comunas?nombre debe retornar 200")
  void obtenerComunasPorNombreRegion_retorna200() throws Exception {
    // Arrange
    ComunaDTO comunaDTO = ComunaDTO.builder()
        .id(1L)
        .nombre("Concepción") // o el nombre que quieras para el test
        .build();

    when(regionService.obtenerComunasPorNombreRegion(any()))
        .thenReturn(List.of(comunaDTO));

    // Act & Assert
    mockMvc.perform(get("/api/v1/regiones/comunas")
        .param("nombre", "Biobío"))
        .andExpect(status().isOk())
        // Opcional: puedes agregar verificaciones JSON aquí como en tus otros tests
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nombre", is("Concepción")));
  }

  @Test
  @DisplayName("GET /api/v1/regiones/comunas sin nombre debe retornar 400")
  void obtenerComunasPorNombreRegion_sinNombre_retorna400() throws Exception {
    mockMvc.perform(get("/api/v1/regiones/comunas"))
        .andExpect(status().isBadRequest());
  }
}
