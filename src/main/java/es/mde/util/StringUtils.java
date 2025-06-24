package es.mde.util;

/**
 * Clase de utilidades para operaciones con cadenas de texto relacionadas con
 * cantidades monetarias.
 * <p>
 * Actualmente proporciona métodos para convertir valores monetarios expresados
 * en céntimos a una representación en euros.
 * </p>
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
public class StringUtils {

  /**
   * Convierte una cantidad en céntimos a una cadena en formato euros con coma
   * como separador decimal.
   * <p>
   * Por ejemplo, una entrada de <code>1234</code> devolverá <code>"12,34"</code>.
   * </p>
   * 
   * @param centimos La cantidad en céntimos (por ejemplo, 1234 representa 12
   *                 euros con 34 céntimos).
   * @return Una cadena formateada como euros con dos decimales separados por coma
   *         (ej: "12,34").
   */
  public static String centimosToEurosString(int centimos) {
    int euros = centimos / 100;
    int decimales = centimos % 100;
    return euros + "," + (decimales < 10 ? "0" + decimales : decimales);
  }

}
