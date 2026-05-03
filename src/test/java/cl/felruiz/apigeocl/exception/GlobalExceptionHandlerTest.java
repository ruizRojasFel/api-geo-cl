package cl.felruiz.apigeocl.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import cl.felruiz.apigeocl.dto.ErrorDTO;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("handleNotFound debe retornar 404 con mensaje")
  void handleNotFound_retorna404() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Región con id 999 no encontrada");

    ResponseEntity<ErrorDTO> response = handler.handleNotFound(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    assertThat(response.getBody())
        .isNotNull()
        .satisfies(body -> {
          assertThat(body.getStatus()).isEqualTo(404);
          assertThat(body.getMensaje()).contains("999");
          assertThat(body.getTimestamp()).isNotNull();
        });
  }

  @Test
  @DisplayName("handleMissingParam debe retornar 400 con nombre del parámetro")
  void handleMissingParam_retorna400() {
    MissingServletRequestParameterException ex = new MissingServletRequestParameterException("nombre", "String");

    ResponseEntity<ErrorDTO> response = handler.handleMissingParam(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    assertThat(response.getBody())
        .isNotNull()
        .satisfies(body -> {
          assertThat(body.getStatus()).isEqualTo(400);
          assertThat(body.getMensaje()).contains("nombre");
        });
  }

  @Test
  @DisplayName("handleGeneral debe retornar 500")
  void handleGeneral_retorna500() {
    Exception ex = new RuntimeException("Error inesperado");

    ResponseEntity<ErrorDTO> response = handler.handleGeneral(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

    assertThat(response.getBody())
        .isNotNull()
        .satisfies(body -> {
          assertThat(body.getStatus()).isEqualTo(500);
          assertThat(body.getError()).isEqualTo("Internal Server Error");
        });
  }
}
