package cl.felruiz.apigeocl.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de error.
 * Se devuelve cuando algo sale mal (404, 400, 500).
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