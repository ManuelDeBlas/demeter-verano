package es.mde.servicios;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mde.entidades.ReservistaConId;
import es.mde.entidades.SolicitudConId;
import es.mde.repositorios.CostePorDiaDAO;
import es.mde.secres.Solicitud.Estados;
import jakarta.persistence.EntityManager;

/**
 * Servicio abstracto para gestionar operaciones comunes sobre solicitudes. Proporciona
 * funcionalidades para crear, actualizar y validar solicitudes, así como calcular el coste asociado
 * en céntimos.
 * 
 * @param <T> Tipo de solicitud que extiende {@link SolicitudConId}
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
public abstract class AbstractSolicitudServicio<T extends SolicitudConId> {

  private Logger log = LoggerFactory.getLogger(AbstractSolicitudServicio.class);

  private final EntityManager entityManager;
  private final CostePorDiaDAO costePorDiaDAO;
  private final EmailSenderServicio emailSenderServicio;

  /**
   * Constructor para inyección de dependencias.
   * 
   * @param entityManager EntityManager para gestión JPA.
   * @param solicitudDAO DAO para solicitudes.
   * @param costePorDiaDAO DAO para costes por día.
   */
  public AbstractSolicitudServicio(EntityManager entityManager, CostePorDiaDAO costePorDiaDAO,
      EmailSenderServicio emailSenderServicio) {
    this.entityManager = entityManager;
    this.costePorDiaDAO = costePorDiaDAO;
    this.emailSenderServicio = emailSenderServicio;
  }

  /**
   * Calcula el coste en céntimos de una solicitud basándose en la duración y el coste diario
   * asociado al empleo del reservista.
   * 
   * @param solicitud solicitud para la cual se calcula el coste.
   * @return coste en céntimos.
   */
  public int calcularCosteCentimos(T solicitud) {
    System.err.println("Coste: " + solicitud.getCosteCentimos());
    // Aquí se produce un bucle infinito ya que para buscar en el DAO JPA tiene que guardar antes la
    // entidad, lo cual dispara otra vez el listener
    int costeDiaCentimos =
        costePorDiaDAO.findByEmpleo(solicitud.getReservista().getEmpleo()).getCentimos();
    System.err.println("Empleo: " + costeDiaCentimos);
    int duracion = solicitud.getDiasDuracion();
    System.err.println("duracion" + duracion);
    return Math.toIntExact(duracion * costeDiaCentimos);
  }

  /**
   * Comprueba si la solicitud es viable respecto a las fechas de compromiso y reconocimiento médico
   * del reservista. Lanza excepción si no es válida.
   * 
   * @param solicitud solicitud a validar.
   * @throws IllegalArgumentException si la solicitud no cumple las restricciones
   */
  public void comprobarViabilidadSolicitud(SolicitudConId solicitud) {
    // Esto está implementado en el Frontend poniendo el botón de modificar cuando la solicitud está
    // pendiente de evaluación
    // if (solicitud.getEstado() == Estados.ACEPTADA_PENDIENTE_PUBLICACION || solicitud.getEstado()
    // == Estados.PUBLICADA) {
    // throw new IllegalArgumentException("ERROR: La solicitud no se puede modificar ya que su
    // estado actual es: " + solicitud.getEstado().toString());
    // }
    ReservistaConId reservista =
        entityManager.getReference(ReservistaConId.class, solicitud.getReservista().getId());

    LocalDate fechaFinCompromiso = reservista.getFechaFinCompromiso();
    if ((fechaFinCompromiso.isEqual(solicitud.getFechaInicio())
        || fechaFinCompromiso.isAfter(solicitud.getFechaInicio()))
        && (fechaFinCompromiso.isEqual(solicitud.getFechaFin())
            || fechaFinCompromiso.isBefore(solicitud.getFechaFin()))) {
      throw new IllegalArgumentException("ERROR: La fecha de fin de compromiso del reservista ("
          + fechaFinCompromiso + ") transcurre durante la activación");
    }

    LocalDate fechaCaducidadReconocimientoMedico =
        reservista.getFechaCaducidadReconocimientoMedico();
    if ((fechaCaducidadReconocimientoMedico.isEqual(solicitud.getFechaInicio())
        || fechaCaducidadReconocimientoMedico.isAfter(solicitud.getFechaInicio()))
        && (fechaCaducidadReconocimientoMedico.isEqual(solicitud.getFechaFin())
            || fechaCaducidadReconocimientoMedico.isBefore(solicitud.getFechaFin()))) {
      throw new IllegalArgumentException(
          "ERROR: La fecha de caducidad del reconocimiento medico del reservista ("
              + fechaCaducidadReconocimientoMedico + ") transcurre durante la activación");
    }
  }

  public void comprobarRechazoSolicitud(SolicitudConId solicitud) {
    if (solicitud.getEstado() == Estados.RECHAZADA) {
      log.info("Se envía el email de cambio de estado a " + solicitud.getEmailPoc());
      emailSenderServicio.enviarEmail(solicitud.getEmailPoc(),
          "El estado de la solicitud de activación se ha modificado",
          "El estado de la solicitud de activación del reservista con DNI "
              + solicitud.getReservista().getDni() + " entre las fechas "
              + solicitud.getFechaInicio() + " - " + solicitud.getFechaFin() + " ha cambiado a "
              + solicitud.getEstado().toString() + ".");
    }
  }

}
