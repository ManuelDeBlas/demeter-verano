package es.mde.entidades;

import es.mde.secres.ActivacionAmpliada;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Representa una activación ampliada. Extiende la funcionalidad de
 * {@link SolicitudConId} e implementa la interfaz {@link ActivacionAmpliada}.
 * 
 * Esta entidad está mapeada a la base de datos con un valor de discriminador
 * "Activacion ampliada". Contiene como información adicional el motivo de la
 * activación.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Entity
@DiscriminatorValue("Activacion ampliada")
public class ActivacionAmpliadaConId extends SolicitudConId implements ActivacionAmpliada {

  /**
   * Motivo de la activación ampliada.
   */
  private String motivo;

  /**
   * Obtiene el motivo de la activación ampliada.
   * 
   * @return el motivo de la activación.
   */
  @Override
  public String getMotivo() {
    return motivo;
  }

  /**
   * Establece el motivo de la activación ampliada.
   * 
   * @param motivo el motivo a establecer.
   */
  @Override
  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

}
