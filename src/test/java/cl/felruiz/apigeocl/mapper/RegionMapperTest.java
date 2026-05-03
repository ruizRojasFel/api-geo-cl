package cl.felruiz.apigeocl.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cl.felruiz.apigeocl.dto.RegionDTO;
import cl.felruiz.apigeocl.model.Region;

/**
 * Tests unitarios para RegionMapper.
 */

class RegionMapperTest {

  private final RegionMapper mapper = new RegionMapper();

  @Test
  @DisplayName("Debe convertir Region a RegionDTO correctamente")
  void toDTO_conRegionValida_retornaDTO() {
    // Arrange → preparar datos
    Region region = Region.builder()
        .id(1L)
        .numero("VIII")
        .nombre("Biobío")
        .capital("Concepción")
        .build();

    // Act → ejecutar
    RegionDTO dto = mapper.toDTO(region);

    // Assert → verificar
    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getNumero()).isEqualTo("VIII");
    assertThat(dto.getNombre()).isEqualTo("Biobío");
    assertThat(dto.getCapital()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("Debe retornar null cuando Region es null")
  void toDTO_conNull_retornaNull() {
    assertThat(mapper.toDTO(null)).isNull();
  }
}