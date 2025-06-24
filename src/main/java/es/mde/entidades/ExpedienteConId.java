package es.mde.entidades;

import java.util.Collection;

import es.mde.secres.Expediente;
import es.mde.secres.Solicitud;
import es.mde.secres.Solicitud.Estados;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un expediente persistente con identificador único.
 * Hereda los atributos y métodos de la clase {@link Expediente}. Añade la
 * gestión de relaciones con las solicitudes (SolicitudConId).
 * 
 * Se almacena en la tabla 'EXPEDIENTES'.
 * 
 * @author Manuel
 * @version 1.0
 */
@Entity
@Table(name = "EXPEDIENTES")
public class ExpedienteConId extends Expediente {

  /**
   * Identificador único del expediente. Se genera automáticamente con la
   * estrategia IDENTITY.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  /**
   * Devuelve el identificador único del expediente.
   * 
   * @return identificador del expediente
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador único del expediente.
   * 
   * @param id identificador a establecer
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Devuelve la colección de solicitudes asociadas a este expediente. Se anota
   * como @OneToMany sobreescribiendo la definición en la clase base.
   * 
   * @return colección de solicitudes
   */
  @Override
  @OneToMany(targetEntity = SolicitudConId.class) // Asegura que la relación sea con la clase persistente
  public Collection<Solicitud> getSolicitudes() {
    return super.getSolicitudes();
  }

  /**
   * Añade una solicitud al expediente y establece la relación bidireccional.
   * 
   * @param solicitud solicitud a añadir
   */
  public void addSolicitudConId(SolicitudConId solicitud) {
    getSolicitudes().add(solicitud);
    solicitud.setExpediente(this);
    solicitud.setEstado(Estados.ACEPTADA_PENDIENTE_PUBLICACION);
  }

  /**
   * Elimina una solicitud del expediente, anula la relación y reinicia su estado.
   * 
   * @param solicitud solicitud a eliminar
   */
  public void removeSolicitud(SolicitudConId solicitud) {
    getSolicitudes().remove(solicitud);
    solicitud.setExpediente(null);
    solicitud.setEstado(Estados.PENDIENTE_EVALUACION);
  }

}
