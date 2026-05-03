package cl.felruiz.apigeocl.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de error.
 * Se devuelve cuando algo sale mal (404, 400, 500).
 *
 * Ejemplo de respuesta JSON:
 * {
 *   "status": 404,
 *   "error": "Not Found",
 *   "mensaje": "Región con id 999 no encontrada",
 *   "timestamp": "2024-04-16T10:30:00"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
  private int status;
  private String error;
  private String mensaje;
  private LocalDateTime timestamp;
}