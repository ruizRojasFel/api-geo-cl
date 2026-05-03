package cl.felruiz.apigeocl.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.model.Provincia;
import cl.felruiz.apigeocl.model.Region;

/**
 * Tests unitarios para ProvinciaMapper.
 * Verifica que la conversión Provincia → ProvinciaDTO
 * incluya correctamente el contexto de la región padre.
 */

class ProvinciaMapperTest {

  private final ProvinciaMapper mapper = new ProvinciaMapper();

  @Test
  @DisplayName("Debe convertir Provincia a ProvinciaDTO con contexto de región")
  void toDTO_conProvinciaValida_retornaDTOConRegion() {
    // Arrange
    Region region = Region.builder()
        .id(11L)
        .numero("VIII")
        .nombre("Biobío")
        .capital("Concepción")
        .build();

    Provincia provincia = Provincia.builder()
        .id(38L)
        .nombre("Concepción")
        .capital("Concepción")
        .region(region)
        .build();

    // Act
    ProvinciaDTO dto = mapper.toDTO(provincia);

    // Assert
    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(38L);
    assertThat(dto.getNombre()).isEqualTo("Concepción");
    assertThat(dto.getCapital()).isEqualTo("Concepción");
    assertThat(dto.getRegionId()).isEqualTo(11L);
    assertThat(dto.getRegionNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("Debe retornar null cuando Provincia es null")
  void toDTO_conNull_retornaNull() {
    assertThat(mapper.toDTO(null)).isNull();
  }
}