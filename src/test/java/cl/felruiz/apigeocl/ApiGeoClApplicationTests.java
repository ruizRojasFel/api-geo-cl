package cl.felruiz.apigeocl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de integración completo. Verifica que toda la aplicación levanta
 * correctamente.
 */

@SpringBootTest
@ActiveProfiles("test")
class ApiGeoClApplicationTests {

  @Test
  @DisplayName("El contexto de Spring debe cargar correctamente")
  void contextLoads() {
  }
}