package es.mde.entidades;

import es.mde.secres.SolicitudImpl;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa una solicitud con identificador único. Extiende la implementación
 * base {@link SolicitudImpl}.
 * 
 * Esta entidad se mapea a la tabla "SOLICITUDES" en la base de datos. Utiliza
 * herencia SINGLE_TABLE con columna discriminadora "TIPO" para distinguir tipos
 * concretos de solicitudes.
 * 
 * Contiene asociaciones a un reservista y un expediente.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Entity
@Table(name = "SOLICITUDES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO")
public abstract class SolicitudConId extends SolicitudImpl {

  /**
   * Identificador único de la solicitud. Se genera automáticamente con estrategia
   * IDENTITY.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  /**
   * Reservista asociado a esta solicitud. Se carga de forma perezosa (lazy).
   */
  @ManyToOne(targetEntity = ReservistaConId.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "RESERVISTA")
  private ReservistaConId reservista;

  /**
   * Expediente al que pertenece la solicitud. Se carga de forma perezosa (lazy).
   */
  @ManyToOne(targetEntity = ExpedienteConId.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "EXPEDIENTE")
  private ExpedienteConId expediente;

  /**
   * Obtiene el identificador único de la solicitud.
   * 
   * @return el id de la solicitud.
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador único de la solicitud.
   * 
   * @param id el identificador a establecer.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Obtiene el reservista asociado a esta solicitud.
   * 
   * @return el reservista.
   */
  public ReservistaConId getReservista() {
    return reservista;
  }

  /**
   * Establece el reservista asociado a esta solicitud.
   * 
   * @param reservista el reservista a asociar.
   */
  public void setReservista(ReservistaConId reservista) {
    this.reservista = reservista;
  }

  /**
   * Obtiene el expediente al que pertenece esta solicitud.
   * 
   * @return el expediente asociado.
   */
  public ExpedienteConId getExpediente() {
    return expediente;
  }

  /**
   * Establece el expediente asociado a esta solicitud.
   * 
   * @param expediente el expediente a asociar.
   */
  public void setExpediente(ExpedienteConId expediente) {
    this.expediente = expediente;
  }

}
