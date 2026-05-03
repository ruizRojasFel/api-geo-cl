package cl.felruiz.apigeocl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para exponer datos de una Región.
 *
 * ¿Por qué no usamos directamente la entidad Region?
 * - La entidad tiene @OneToMany con provincias → podría
 *   generar respuestas JSON infinitas o muy pesadas
 * - El DTO define exactamente qué datos expone la API
 * - Si cambia la BD internamente, el DTO protege el contrato
 *   con los consumidores de la API
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