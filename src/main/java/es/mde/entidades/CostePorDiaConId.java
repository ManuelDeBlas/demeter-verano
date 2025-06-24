package es.mde.entidades;

import es.mde.secres.CostePorDia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un coste por día con identificador persistente.
 * Hereda los campos 'empleo' y 'centimos' de la clase {@link CostePorDia}.
 * 
 * Se almacena en la tabla 'COSTES_POR_DIA'.
 */
@Entity
@Table(name = "COSTES_POR_DIA")
public class CostePorDiaConId extends CostePorDia {

  /**
   * Identificador único del registro en base de datos. Se genera automáticamente
   * usando la estrategia IDENTITY.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  /**
   * Obtiene el identificador único del coste.
   * 
   * @return el identificador del coste.
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador único del coste.
   * 
   * @param id el identificador a establecer.
   */
  public void setId(Long id) {
    this.id = id;
  }

}
