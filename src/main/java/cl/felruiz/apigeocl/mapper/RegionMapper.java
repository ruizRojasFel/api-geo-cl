package cl.felruiz.apigeocl.mapper;

import org.springframework.stereotype.Component;

import cl.felruiz.apigeocl.dto.RegionDTO;
import cl.felruiz.apigeocl.model.Region;

/**
 * Mapper para convertir entre Region (entidad) y RegionDTO.
 */
@Component
public class RegionMapper {

  /**
   * Convierte Region (entidad JPA) → RegionDTO.
   * Solo expone los campos que se desean mostrar al exterior.
   */
  public RegionDTO toDTO(Region region) {
    if (region == null) return null;

    return RegionDTO.builder()
      .id(region.getId())
      .numero(region.getNumero())
      .nombre(region.getNombre())
      .capital(region.getCapital())
      .build();
  }
}