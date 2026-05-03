package cl.felruiz.apigeocl.mapper;

import org.springframework.stereotype.Component;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.model.Comuna;

/**
 * Mapper para convertir entre Comuna (entidad) y ComunaDTO.
 */
@Component
public class ComunaMapper {

  /**
   * Convierte Comuna (entidad JPA) → ComunaDTO.
   * Navega las relaciones:
   * comuna → provincia (para provinciaId y provinciaNombre)
   * comuna → provincia → region (para regionId y regionNombre)
   */
  public ComunaDTO toDTO(Comuna comuna) {
    if (comuna == null) return null;

    return ComunaDTO.builder()
      .id(comuna.getId())
      .nombre(comuna.getNombre())
      .codigoCut(comuna.getCodigoCut())
      .provinciaId(comuna.getProvincia().getId())
      .provinciaNombre(comuna.getProvincia().getNombre())
      .regionId(comuna.getProvincia().getRegion().getId())
      .regionNombre(comuna.getProvincia().getRegion().getNombre())
      .build();
  }
}
