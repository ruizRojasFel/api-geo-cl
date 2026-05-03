package cl.felruiz.apigeocl.exception;

/**
 * Excepción personalizada para cuando un recurso
 * no existe en la base de datos.
 *
 * Extiende RuntimeException → excepción no verificada,
 * no obliga a declarar "throws" en los métodos que la lanzan.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String mensaje) {
    super(mensaje);
  }
}