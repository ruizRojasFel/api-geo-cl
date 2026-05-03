package cl.felruiz.apigeocl.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.model.Comuna;
import cl.felruiz.apigeocl.model.Provincia;
import cl.felruiz.apigeocl.model.Region;

/**
 * Tests unitarios para ComunaMapper.
 * Verifica que la conversión Comuna → ComunaDTO
 * incluya correctamente el contexto de provincia y región.
 */

class ComunaMapperTest {

  private final ComunaMapper mapper = new ComunaMapper();

  @Test
  @DisplayName("Debe convertir Comuna a ComunaDTO con contexto de provincia y región")
  void toDTO_conComunaValida_retornaDTOConContexto() {
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

    Comuna comuna = Comuna.builder()
        .id(219L)
        .nombre("Concepción")
        .codigoCut("08101")
        .provincia(provincia)
        .build();

    // Act
    ComunaDTO dto = mapper.toDTO(comuna);

    // Assert
    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(219L);
    assertThat(dto.getNombre()).isEqualTo("Concepción");
    assertThat(dto.getCodigoCut()).isEqualTo("08101");
    assertThat(dto.getProvinciaId()).isEqualTo(38L);
    assertThat(dto.getProvinciaNombre()).isEqualTo("Concepción");
    assertThat(dto.getRegionId()).isEqualTo(11L);
    assertThat(dto.getRegionNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("Debe retornar null cuando Comuna es null")
  void toDTO_conNull_retornaNull() {
    assertThat(mapper.toDTO(null)).isNull();
  }
}