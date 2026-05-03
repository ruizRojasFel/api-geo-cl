package cl.felruiz.apigeocl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para exponer datos de una Provincia.
 * Incluye información básica de su Región para dar contexto
 * al consumidor sin necesidad de hacer una segunda llamada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaDTO {
  private Long id;
  private String nombre;
  private String capital;
  // Contexto de la región padre
  private Long regionId;
  private String regionNombre;
}