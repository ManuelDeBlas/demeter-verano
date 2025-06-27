package es.mde.entidades;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.mde.secres.Reservista;
import es.mde.secres.Solicitud;
import es.mde.secres.Solicitud.Estados;
import jakarta.persistence.CascadeType;
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
  
  @OneToMany(mappedBy = "reservista", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private Collection<SolicitudConId> solicitudes = new ArrayList<>();

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

  public Collection<SolicitudConId> getSolicitudes() {
    return solicitudes;
  }

  public void addSolicitudConId(SolicitudConId solicitud) {
    solicitudes.add(solicitud);
    solicitud.setReservista(this);
  }

  public void removeSolicitudConId(SolicitudConId solicitud) {
    solicitudes.remove(solicitud);
    solicitud.setReservista(null);
  }
  
  /**
   * Obtiene el número de días consumidos por el reservista.
   * 
   * @param anho
   *
   * @return el número de días consumidos.
   */
  public int getDiasConsumidos(int anho) {
    int diasConsumidos = 0;
    for (SolicitudConId solicitud : getSolicitudes()) {
      if (solicitud.getFechaInicio().getYear() == anho
          && (solicitud.getEstado().equals(Estados.ACEPTADA_PENDIENTE_PUBLICACION)
              || solicitud.getEstado().equals(Estados.PUBLICADA))) {
        diasConsumidos += solicitud.getDiasDuracion();
      }
    }

    return diasConsumidos;
  }


}
