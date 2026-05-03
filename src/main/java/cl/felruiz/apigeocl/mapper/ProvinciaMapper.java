package cl.felruiz.apigeocl.mapper;

import org.springframework.stereotype.Component;

import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.model.Provincia;

/**
 * Mapper para convertir entre Provincia (entidad) y ProvinciaDTO.
 */
@Component
public class ProvinciaMapper {

  /**
   * Convierte Provincia (entidad JPA) → ProvinciaDTO.
   * Navega la relación provincia → region para obtener
   * el contexto de la región sin una consulta extra.
   */
  public ProvinciaDTO toDTO(Provincia provincia) {
    if (provincia == null) return null;

    return ProvinciaDTO.builder()
      .id(provincia.getId())
      .nombre(provincia.getNombre())
      .capital(provincia.getCapital())
      .regionId(provincia.getRegion().getId())
      .regionNombre(provincia.getRegion().getNombre())
      .build();
  }
}