package cl.felruiz.apigeocl.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.mapper.ComunaMapper;
import cl.felruiz.apigeocl.mapper.ProvinciaMapper;
import cl.felruiz.apigeocl.model.Comuna;
import cl.felruiz.apigeocl.model.Provincia;
import cl.felruiz.apigeocl.model.Region;
import cl.felruiz.apigeocl.repository.ComunaRepository;
import cl.felruiz.apigeocl.repository.ProvinciaRepository;

@ExtendWith(MockitoExtension.class)
class ProvinciaServiceTest {

  @Mock
  private ProvinciaRepository provinciaRepository;

  @Mock
  private ComunaRepository comunaRepository;

  @Spy
  private ProvinciaMapper provinciaMapper;

  @Spy
  private ComunaMapper comunaMapper;

  @InjectMocks
  private ProvinciaService provinciaService;

  @BeforeEach
  public void setUp() {
    assertNotNull(provinciaMapper);
    assertNotNull(comunaMapper);
  }

  private Region buildRegion() {
    return Region.builder()
        .id(11L).numero("VIII").nombre("Biobío").capital("Concepción").build();
  }

  private Provincia buildProvincia() {
    return Provincia.builder()
        .id(38L).nombre("Concepción").capital("Concepción").region(buildRegion()).build();
  }

  @Test
  @DisplayName("obtenerTodas debe retornar lista de ProvinciaDTO")
  void obtenerTodas_retornaListaDTO() {
    when(provinciaRepository.findAll()).thenReturn(List.of(buildProvincia()));

    List<ProvinciaDTO> resultado = provinciaService.obtenerTodas();

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("obtenerPorId con ID válido debe retornar ProvinciaDTO")
  void obtenerPorId_conIdValido_retornaDTO() {
    when(provinciaRepository.findById(38L)).thenReturn(Optional.of(buildProvincia()));

    ProvinciaDTO resultado = provinciaService.obtenerPorId(38L);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getNombre()).isEqualTo("Concepción");
    assertThat(resultado.getRegionNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("obtenerPorId con ID inexistente debe lanzar excepción")
  void obtenerPorId_conIdInexistente_lanzaExcepcion() {
    when(provinciaRepository.findById(999L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> provinciaService.obtenerPorId(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("999");
  }

  @Test
  @DisplayName("buscarPorNombre debe normalizar y retornar resultados")
  void buscarPorNombre_conTildes_retornaResultados() {
    when(provinciaRepository.findByNombreNormalizado("concepcion"))
        .thenReturn(List.of(buildProvincia()));

    List<ProvinciaDTO> resultado = provinciaService.buscarPorNombre("CONCEPCIÓN");

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("buscarPorNombre con texto vacío debe retornar lista vacía")
  void buscarPorNombre_conVacio_retornaListaVacia() {
    assertThat(provinciaService.buscarPorNombre("   ")).isEmpty();
  }

  @Test
  @DisplayName("obtenerComunasPorProvincia con ID válido retorna comunas")
  void obtenerComunasPorProvincia_conIdValido_retornaComunas() {
    Provincia provincia = buildProvincia();
    Comuna comuna = Comuna.builder()
        .id(219L).nombre("Concepción").codigoCut("08101").provincia(provincia).build();

    when(provinciaRepository.existsById(38L)).thenReturn(true);
    when(comunaRepository.findByProvinciaId(38L)).thenReturn(List.of(comuna));

    List<ComunaDTO> resultado = provinciaService.obtenerComunasPorProvincia(38L);

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("obtenerComunasPorProvincia con ID inexistente lanza excepción")
  void obtenerComunasPorProvincia_conIdInexistente_lanzaExcepcion() {
    when(provinciaRepository.existsById(999L)).thenReturn(false);

    assertThatThrownBy(() -> provinciaService.obtenerComunasPorProvincia(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("999");
  }
}
