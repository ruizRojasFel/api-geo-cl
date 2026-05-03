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
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.mapper.ComunaMapper;
import cl.felruiz.apigeocl.model.Comuna;
import cl.felruiz.apigeocl.model.Provincia;
import cl.felruiz.apigeocl.model.Region;
import cl.felruiz.apigeocl.repository.ComunaRepository;

@ExtendWith(MockitoExtension.class)
class ComunaServiceTest {

  @Mock
  private ComunaRepository comunaRepository;

  @Spy
  private ComunaMapper comunaMapper;

  @InjectMocks
  private ComunaService comunaService;

  @BeforeEach
  public void setUp() {
    assertNotNull(comunaMapper);
  }

  private Comuna buildComuna() {
    Region region = Region.builder()
        .id(11L).numero("VIII").nombre("Biobío").capital("Concepción").build();
    Provincia provincia = Provincia.builder()
        .id(38L).nombre("Concepción").capital("Concepción").region(region).build();
    return Comuna.builder()
        .id(219L).nombre("Concepción").codigoCut("08101").provincia(provincia).build();
  }

  @Test
  @DisplayName("obtenerTodas debe retornar lista de ComunaDTO")
  void obtenerTodas_retornaListaDTO() {
    when(comunaRepository.findAll()).thenReturn(List.of(buildComuna()));

    List<ComunaDTO> resultado = comunaService.obtenerTodas();

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
    assertThat(resultado.get(0).getCodigoCut()).isEqualTo("08101");
  }

  @Test
  @DisplayName("obtenerPorId con ID válido debe retornar ComunaDTO")
  void obtenerPorId_conIdValido_retornaDTO() {
    when(comunaRepository.findById(219L)).thenReturn(Optional.of(buildComuna()));

    ComunaDTO resultado = comunaService.obtenerPorId(219L);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getNombre()).isEqualTo("Concepción");
    assertThat(resultado.getProvinciaNombre()).isEqualTo("Concepción");
    assertThat(resultado.getRegionNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("obtenerPorId con ID inexistente debe lanzar excepción")
  void obtenerPorId_conIdInexistente_lanzaExcepcion() {
    when(comunaRepository.findById(999L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> comunaService.obtenerPorId(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("999");
  }

  @Test
  @DisplayName("buscarPorNombre debe normalizar y retornar resultados")
  void buscarPorNombre_conTildes_retornaResultados() {
    when(comunaRepository.findByNombreNormalizado("concepcion"))
        .thenReturn(List.of(buildComuna()));

    List<ComunaDTO> resultado = comunaService.buscarPorNombre("CONCEPCIÓN");

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("buscarPorNombre con texto vacío debe retornar lista vacía")
  void buscarPorNombre_conVacio_retornaListaVacia() {
    assertThat(comunaService.buscarPorNombre("   ")).isEmpty();
  }

  @Test
  @DisplayName("buscarPorNombre con null debe retornar lista vacía")
  void buscarPorNombre_conNull_retornaListaVacia() {
    assertThat(comunaService.buscarPorNombre(null)).isEmpty();
  }

  @Test
  @DisplayName("obtenerComunasPorRegion debe retornar comunas de la región")
  void obtenerComunasPorRegion_retornaComunas() {
    when(comunaRepository.findByProvinciaRegionId(11L))
        .thenReturn(List.of(buildComuna()));

    List<ComunaDTO> resultado = comunaService.obtenerComunasPorRegion(11L);

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getRegionId()).isEqualTo(11L);
  }
}
