package es.mde.entidades;

import es.mde.secres.CosteFormacionContinuada;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un coste de formación continuada con identificador
 * persistente. Extiende la clase base {@link CosteFormacionContinuada}, que
 * contiene los campos comunes.
 */
@Entity
@Table(name = "COSTES_FC")
public class CosteFormacionContinuadaConId extends CosteFormacionContinuada {

  /**
   * Identificador único de la entidad. Se genera automáticamente con la
   * estrategia IDENTITY (típica en bases de datos como MySQL).
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  /**
   * Devuelve el identificador del coste.
   *
   * @return id único
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador del coste.
   *
   * @param id identificador único
   */
  public void setId(Long id) {
    this.id = id;
  }

}
