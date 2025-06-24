package es.mde.entidades;

import es.mde.secres.FormacionContinuada;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Representa una formación continuada. Extiende la funcionalidad de
 * {@link SolicitudConId} e implementa la interfaz {@link FormacionContinuada}.
 * 
 * Esta entidad está mapeada a la base de datos con un valor de discriminador
 * "Formacion continuada". Contiene información específica como el tiempo máximo
 * permitido y la escala asociada.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Entity
@DiscriminatorValue("Formacion continuada")
public class FormacionContinuadaConId extends SolicitudConId implements FormacionContinuada {

  /**
   * Escala asociada a la formación continuada.
   */
  private String escala;

  /**
   * Obtiene la escala asociada a la formación continuada.
   * 
   * @return la escala asociada.
   */
  @Override
  public String getEscala() {
    return escala;
  }

  /**
   * Establece la escala asociada a la formación continuada.
   * 
   * @param escala la escala a establecer.
   */
  public void setEscala(String escala) {
    this.escala = escala;
  }

  /**
   * Las formaciones continuadas nunca las paga la SECRES
   *
   * @return false
   */
  @Override
  public boolean isPagaSecres() {
    return false;
  }

}
