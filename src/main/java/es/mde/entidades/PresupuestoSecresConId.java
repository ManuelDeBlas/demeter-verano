package es.mde.entidades;

import es.mde.listeners.PresupuestoSecresListener;
import es.mde.secres.Presupuesto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Entidad JPA que representa un presupuesto con identificador único. Hereda los atributos y métodos
 * de la clase {@link Presupuesto}.
 * 
 * Esta clase se mapea a la tabla 'PRESUPUESTOS' en la base de datos.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Entity
@Table(name = "PRESUPUESTOS")
@EntityListeners(PresupuestoSecresListener.class)
public class PresupuestoSecresConId {

  /**
   * Identificador único del presupuesto. Se genera automáticamente con la estrategia IDENTITY.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  private int cantidadCentimosConcedido;
  private int anho;
  
  @Transient
  private int cantidadCentimosGastado;

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

  public int getCantidadCentimosConcedido() {
    return cantidadCentimosConcedido;
  }

  public void setCantidadCentimosConcedido(int cantidadCentimosConcedido) {
    this.cantidadCentimosConcedido = cantidadCentimosConcedido;
  }

  public int getAnho() {
    return anho;
  }

  public void setAnho(int anho) {
    this.anho = anho;
  }
  
  public int getCantidadCentimosGastado() {
    return cantidadCentimosGastado;
  }
  
  public void setCantidadCentimosGastado(int cantidadCentimosGastado) {
    this.cantidadCentimosGastado = cantidadCentimosGastado;
  }

}
