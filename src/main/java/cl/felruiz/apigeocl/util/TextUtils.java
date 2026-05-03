package cl.felruiz.apigeocl.util;

import java.text.Normalizer;

/**
 * Utilidades para normalización de texto en búsquedas.
 */

public class TextUtils {

  private TextUtils() {}

  /**
   * Normaliza texto eliminando tildes, espacios extra y convirtiendo a minúsculas.
   */

  public static String normalizar(String texto) {
    if (texto == null || texto.isBlank()) return "";
    return Normalizer
        .normalize(texto.trim().replaceAll("\\s+", " "), Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .toLowerCase();
  }
}