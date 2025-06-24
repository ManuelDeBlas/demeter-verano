package es.mde.entidades;

import es.mde.secres.Presupuesto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un presupuesto con identificador único. Hereda los
 * atributos y métodos de la clase {@link Presupuesto}.
 * 
 * Esta clase se mapea a la tabla 'PRESUPUESTOS' en la base de datos.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Entity
@Table(name = "PRESUPUESTOS")
public class PresupuestoConId extends Presupuesto {

  /**
   * Identificador único del presupuesto. Se genera automáticamente con la
   * estrategia IDENTITY.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  /**
   * Obtiene el identificador único del presupuesto.
   * 
   * @return el identificador del presupuesto.
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador único del presupuesto.
   * 
   * @param id el identificador a establecer.
   */
  public void setId(Long id) {
    this.id = id;
  }

}
