package cl.felruiz.apigeocl.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para TextUtils.
 * 
 * Convención de nombres: método_escenario_resultadoEsperado
 */

class TextUtilsTest {

  @Test
  @DisplayName("Debe eliminar tildes")
  void normalizar_conTildes_eliminaTildes() {
    assertThat(TextUtils.normalizar("Concepción")).isEqualTo("concepcion");
    assertThat(TextUtils.normalizar("Biobío")).isEqualTo("biobio");
    assertThat(TextUtils.normalizar("Los Ríos")).isEqualTo("los rios");
  }

  @Test
  @DisplayName("Debe convertir a minúsculas")
  void normalizar_conMayusculas_convierteAMinusculas() {
    assertThat(TextUtils.normalizar("SANTIAGO")).isEqualTo("santiago");
    assertThat(TextUtils.normalizar("CONCEPCIÓN")).isEqualTo("concepcion");
  }

  @Test
  @DisplayName("Debe eliminar espacios extras")
  void normalizar_conEspaciosExtras_eliminaEspacios() {
    assertThat(TextUtils.normalizar("  Concepción  ")).isEqualTo("concepcion");
    assertThat(TextUtils.normalizar("San  Pedro")).isEqualTo("san pedro");
  }

  @Test
  @DisplayName("Debe normalizar ñ a n")
  void normalizar_conEnie_convierteeAn() {
    assertThat(TextUtils.normalizar("Ñuñoa")).isEqualTo("nunoa");
    assertThat(TextUtils.normalizar("Ñuble")).isEqualTo("nuble");
  }

  @Test
  @DisplayName("Debe manejar null y vacío")
  void normalizar_conNullOVacio_retornaVacio() {
    assertThat(TextUtils.normalizar(null)).isEqualTo("");
    assertThat(TextUtils.normalizar("")).isEqualTo("");
    assertThat(TextUtils.normalizar("   ")).isEqualTo("");
  }

  @Test
  @DisplayName("Debe normalizar combinación de problemas")
  void normalizar_conCombinacion_normalizaTodo() {
    assertThat(TextUtils.normalizar("  lOs ÁnGeLes  ")).isEqualTo("los angeles");
    assertThat(TextUtils.normalizar("  SAN  PEDRO  ")).isEqualTo("san pedro");
  }
}