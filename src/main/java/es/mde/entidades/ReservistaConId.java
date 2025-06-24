package es.mde.entidades;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.mde.secres.Reservista;
import es.mde.secres.Solicitud;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un reservista con identificador único. Extiende la
 * funcionalidad de la clase {@link Reservista}.
 * 
 * Mapea a la tabla "RESERVISTAS" en la base de datos. Contiene las solicitudes
 * asociadas a este reservista.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Entity
@Table(name = "RESERVISTAS")
public class ReservistaConId extends Reservista {

  /**
   * Identificador único del reservista. Se genera automáticamente con la
   * estrategia IDENTITY.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  /**
   * Devuelve el identificador único del reservista.
   * 
   * @return el id del reservista.
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador único del reservista.
   * 
   * @param id identificador a establecer.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Obtiene la colección de solicitudes asociadas al reservista. Se ignora en la
   * serialización JSON para evitar referencias cíclicas.
   * 
   * @return la colección de solicitudes.
   */
  @Override
  @OneToMany(targetEntity = SolicitudConId.class)
  @JsonIgnore
  public Collection<Solicitud> getSolicitudes() {
    return super.getSolicitudes();
  }

  /**
   * Añade una solicitud al reservista y establece la relación bidireccional.
   * 
   * @param solicitud solicitud a añadir.
   */
  public void addSolicitudConId(SolicitudConId solicitud) {
    super.getSolicitudes().add(solicitud);
    solicitud.setReservista(this);
  }

}
