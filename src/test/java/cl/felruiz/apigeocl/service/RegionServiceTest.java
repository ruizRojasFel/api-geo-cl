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
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.dto.RegionDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.mapper.ComunaMapper;
import cl.felruiz.apigeocl.mapper.ProvinciaMapper;
import cl.felruiz.apigeocl.mapper.RegionMapper;
import cl.felruiz.apigeocl.model.Comuna;
import cl.felruiz.apigeocl.model.Provincia;
import cl.felruiz.apigeocl.model.Region;
import cl.felruiz.apigeocl.repository.ComunaRepository;
import cl.felruiz.apigeocl.repository.ProvinciaRepository;
import cl.felruiz.apigeocl.repository.RegionRepository;

/**
 * Tests unitarios para RegionService.
 */

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

  @Mock
  private RegionRepository regionRepository;

  @Mock
  private ProvinciaRepository provinciaRepository;

  @Mock
  private ComunaRepository comunaRepository;

  @Spy
  private RegionMapper regionMapper;

  @Spy
  private ProvinciaMapper provinciaMapper;

  @Spy
  private ComunaMapper comunaMapper;

  @InjectMocks
  private RegionService regionService;

  @BeforeEach
  public void setUp() {
    assertNotNull(regionMapper);
    assertNotNull(provinciaMapper);
    assertNotNull(comunaMapper);
  }

  @Test
  @DisplayName("obtenerTodas debe retornar lista de RegionDTO")
  void obtenerTodas_conRegiones_retornaListaDTO() {
    // Arrange → lo que devuelve el repository
    List<Region> regiones = List.of(
        Region.builder().id(1L).numero("VIII").nombre("Biobío").capital("Concepción").build(),
        Region.builder().id(2L).numero("XIII").nombre("Metropolitana de Santiago").capital("Santiago").build());
    when(regionRepository.findAll()).thenReturn(regiones);

    // Act
    List<RegionDTO> resultado = regionService.obtenerTodas();

    // Assert
    assertThat(resultado).hasSize(2);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Biobío");
    assertThat(resultado.get(1).getNombre()).isEqualTo("Metropolitana de Santiago");
  }

  @Test
  @DisplayName("obtenerPorId con ID válido debe retornar RegionDTO")
  void obtenerPorId_conIdValido_retornaDTO() {
    // Arrange
    Region region = Region.builder()
        .id(1L).numero("VIII").nombre("Biobío").capital("Concepción").build();
    when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

    // Act
    RegionDTO resultado = regionService.obtenerPorId(1L);

    // Assert
    assertThat(resultado).isNotNull();
    assertThat(resultado.getNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("obtenerPorId con ID inexistente debe lanzar ResourceNotFoundException")
  void obtenerPorId_conIdInexistente_lanzaExcepcion() {
    // Arrange → repository devuelve vacío
    when(regionRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert → lanza la excepción correcta
    assertThatThrownBy(() -> regionService.obtenerPorId(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("999");
  }

  @Test
  @DisplayName("buscarPorNombre debe normalizar input y retornar resultados")
  void buscarPorNombre_conTildes_retornaResultados() {
    // Arrange
    Region region = Region.builder()
        .id(1L).numero("VIII").nombre("Biobío").capital("Concepción").build();
    when(regionRepository.findByNombreNormalizado("biobio")).thenReturn(List.of(region));

    // Act → búsqueda sin tilde
    List<RegionDTO> resultado = regionService.buscarPorNombre("BIOBÍO");

    // Assert
    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("buscarPorNombre con texto vacío debe retornar lista vacía")
  void buscarPorNombre_conVacio_retornaListaVacia() {
    // Act
    List<RegionDTO> resultado = regionService.buscarPorNombre("   ");

    // Assert
    assertThat(resultado).isEmpty();
  }

  @Test
  @DisplayName("obtenerProvinciasPorRegion con ID válido retorna provincias")
  void obtenerProvinciasPorRegion_conIdValido_retornaProvincias() {
    Region region = Region.builder()
        .id(1L).numero("VIII").nombre("Biobío").capital("Concepción").build();
    Provincia provincia = Provincia.builder()
        .id(38L).nombre("Concepción").capital("Concepción").region(region).build();

    when(regionRepository.existsById(1L)).thenReturn(true);
    when(provinciaRepository.findByRegionId(1L)).thenReturn(List.of(provincia));

    List<ProvinciaDTO> resultado = regionService.obtenerProvinciasPorRegion(1L);

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("obtenerProvinciasPorRegion con ID inexistente lanza excepción")
  void obtenerProvinciasPorRegion_conIdInexistente_lanzaExcepcion() {
    when(regionRepository.existsById(999L)).thenReturn(false);

    assertThatThrownBy(() -> regionService.obtenerProvinciasPorRegion(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("999");
  }

  @Test
  @DisplayName("obtenerComunasPorNombreRegion debe retornar lista de comunas")
  void obtenerComunasPorNombreRegion_retornaComunas() {
    // Arrange
    Region region = Region.builder()
        .id(1L)
        .nombre("Biobío")
        .build();
    Provincia provincia = Provincia.builder()
        .id(38L)
        .nombre("Concepción")
        .capital("Concepción")
        .region(region)
        .build();
    Comuna comuna = Comuna.builder()
        .id(1L)
        .nombre("Concepción")
        .codigoCut("08101")
        .provincia(provincia)
        .build();

    when(regionRepository.findByNombreNormalizadoExact(any()))
        .thenReturn(Optional.of(region));
    when(comunaRepository.findByProvinciaRegionId(1L)).thenReturn(List.of(comuna));

    // Act
    List<ComunaDTO> resultado = regionService.obtenerComunasPorNombreRegion("Biobío");

    // Assert
    assertNotNull(resultado);
    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Concepción");
    verify(regionRepository).findByNombreNormalizadoExact(any());
    verify(comunaRepository).findByProvinciaRegionId(1L);
  }

  @Test
  @DisplayName("obtenerComunasPorNombreRegion debe lanzar excepción si no existe")
  void obtenerComunasPorNombreRegion_lanzaExcepcion() {
    // Arrange
    when(regionRepository.findByNombreNormalizadoExact(any()))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> regionService.obtenerComunasPorNombreRegion("NoExiste"))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("NoExiste");
  }
}
