package cl.felruiz.apigeocl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para exponer datos de una Región.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
  private Long id;
  private String numero;
  private String nombre;
  private String capital;
}