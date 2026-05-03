package cl.felruiz.apigeocl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para exponer datos de una Comuna.
 * Incluye contexto de provincia y región porque los
 * consumidores casi siempre necesitan esa información
 * completa sin tener que hacer múltiples llamadas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComunaDTO {
  private Long id;
  private String nombre;
  private String codigoCut;
  // Contexto de provincia padre
  private Long provinciaId;
  private String provinciaNombre;
  // Contexto de región abuelo
  private Long regionId;
  private String regionNombre;
}