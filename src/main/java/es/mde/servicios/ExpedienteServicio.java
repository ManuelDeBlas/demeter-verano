package es.mde.servicios;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.mde.entidades.ExpedienteConId;
import es.mde.entidades.PresupuestoConId;
import es.mde.entidades.SolicitudConId;
import es.mde.repositorios.ExpedienteDAO;
import es.mde.repositorios.PresupuestoSecresDAO;
import es.mde.repositorios.SolicitudDAO;
import es.mde.util.StringUtils;

/**
 * Servicio para gestionar la lógica de negocio relacionada con los expedientes.
 * Incluye operaciones para asignar y desasignar solicitudes, validaciones y
 * notificaciones por email.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Service
public class ExpedienteServicio {

  /**
   * Registro auxiliar para devolver las entidades que se modificarán: expediente,
   * solicitud y presupuesto.
   */
  private record EntidadesAModificar(ExpedienteConId expediente, SolicitudConId solicitud,
      PresupuestoConId presupuesto) {
  }

  private final ExpedienteDAO expedienteDAO;
  private final SolicitudDAO solicitudDAO;
  private final PresupuestoSecresDAO presupuestoSecresDAO;
  private final EmailSenderServicio emailSenderServicio;

  /** Máximo número de días permitidos para una activación en un año */
  @Value("${maximo-dias-activacion}")
  private int maximoDiasActivacion;

  /**
   * Constructor que inyecta los DAOs y el servicio para envío de emails.
   */
  @Autowired
  public ExpedienteServicio(ExpedienteDAO expedienteDAO, SolicitudDAO solicitudDAO, PresupuestoSecresDAO presupuestoSecresDAO,
      EmailSenderServicio emailSenderServicio) {
    this.expedienteDAO = expedienteDAO;
    this.solicitudDAO = solicitudDAO;
    this.presupuestoSecresDAO = presupuestoSecresDAO;
    this.emailSenderServicio = emailSenderServicio;
  }

  /**
   * Asigna una solicitud a un expediente tras validar las condiciones de negocio
   * y envía una notificación por email.
   * 
   * @param expedienteId ID del expediente.
   * @param solicitudId  ID de la solicitud.
   * @return Mensaje con el resultado de la operación.
   */
  public String asignarSolicitudAExpediente(Long expedienteId, Long solicitudId) {
    EntidadesAModificar entidades = obtenerEntidades(expedienteId, solicitudId);
    ExpedienteConId expediente = entidades.expediente();
    SolicitudConId solicitud = entidades.solicitud();
    PresupuestoConId presupuesto = entidades.presupuesto();

    if (expediente.getAnho() != solicitud.getFechaInicio().getYear()) {
      throw new IllegalArgumentException(
          "ERROR: No se pudo agregar la solicitud al expediente ya que el año de la solicitud ("
              + solicitud.getFechaInicio().getYear() + ") no es igual al año del expediente (" + expediente.getAnho()
              + ")");
    }

    if (solicitud.getExpediente() != null) {
      throw new IllegalArgumentException(
          "ERROR: Esta solicitud ya está asignada al expediente " + solicitud.getExpediente().getNumeroExpediente()
              + ". Elimínela de su expediente antes de asignarla a otro.");
    }

    if (!Objects.equals(solicitud.getTipoSolicitud(), expediente.getTipoSolicitud())) {
      throw new IllegalArgumentException("ERROR: El tipo de la solicitud (" + solicitud.getTipoSolicitud()
          + ") no coincide con el tipo del expediente (" + expediente.getTipoSolicitud() + ")");
    }

    if (solicitud.getDiasDuracion()
        + solicitud.getReservista().getDiasConsumidos(solicitud.getFechaInicio().getYear()) > maximoDiasActivacion) {
      throw new IllegalArgumentException("ERROR: El reservista cuenta con "
          + String.valueOf(
              maximoDiasActivacion - solicitud.getReservista().getDiasConsumidos(solicitud.getFechaInicio().getYear()))
          + " dias disponibles y la activación dura " + solicitud.getDiasDuracion() + " dias");
    }

    int costeCentimosSolicitud = solicitud.getCosteCentimos();

    if (solicitud.isPagaSecres() && presupuesto.getCantidadCentimos() < costeCentimosSolicitud) {
      throw new IllegalArgumentException(
          "ERROR: La SECRES no dispone de suficiente presupuesto en el año " + expediente.getAnho()
              + ". Aumente el presupuesto disponible hasta " + StringUtils.centimosToEurosString(costeCentimosSolicitud)
              + " € o cambie al pagador de la solicitud para poder asignarla a un expediente.");
    }

    expediente.addSolicitudConId(solicitud);
    if (solicitud.isPagaSecres()) {
      presupuesto.setCantidadCentimos(presupuesto.getCantidadCentimos() - costeCentimosSolicitud);
    }
    guardarCambios(expediente, solicitud, presupuesto);

    String respuesta = "Solicitud del reservista " + solicitud.getReservista().getDni() + " asignada al expediente "
        + expediente.getNumeroExpediente();
    emailSenderServicio.enviarEmail(solicitud.getEmailPoc(), "Solicitud asignada al expediente", respuesta);

    return respuesta + " correctamente. El presupuesto restante para el año " + presupuesto.getAnho() + " es de "
        + StringUtils.centimosToEurosString(presupuesto.getCantidadCentimos()) + " €.";
  }

  /**
   * Desasigna una solicitud de un expediente y envía una notificación por email.
   * 
   * @param expedienteId ID del expediente.
   * @param solicitudId  ID de la solicitud.
   * @return Mensaje con el resultado de la operación.
   */
  public String desasignarSolicitudDeExpediente(Long expedienteId, Long solicitudId) {
    EntidadesAModificar entidades = obtenerEntidades(expedienteId, solicitudId);
    ExpedienteConId expediente = entidades.expediente();
    SolicitudConId solicitud = entidades.solicitud();
    PresupuestoConId presupuesto = entidades.presupuesto();

    if (expediente.getSolicitudes().contains(solicitud)) {
      expediente.removeSolicitud(solicitud);

      if (solicitud.isPagaSecres()) {
        presupuesto.setCantidadCentimos(presupuesto.getCantidadCentimos() + solicitud.getCosteCentimos());
      }
    } else {
      throw new IllegalArgumentException("ERROR: El expediente " + expediente.getNumeroExpediente()
          + " no contiene la solicitud que se quiere eliminar.");
    }

    guardarCambios(expediente, solicitud, presupuesto);

    String respuesta = "Solicitud del reservista " + solicitud.getReservista().getDni() + " desasignada del expediente "
        + expediente.getNumeroExpediente();
    emailSenderServicio.enviarEmail(solicitud.getEmailPoc(), "Solicitud desasignada del expediente", respuesta);

    return respuesta + " correctamente. El presupuesto restante para el año " + presupuesto.getAnho() + " es de "
        + StringUtils.centimosToEurosString(presupuesto.getCantidadCentimos()) + " €.";
  }

  /**
   * Obtiene las entidades Expediente, Solicitud y Presupuesto a modificar,
   * validando que existan y lanzando excepciones si no.
   * 
   * @param expedienteId ID del expediente.
   * @param solicitudId  ID de la solicitud.
   * @return Un record con las tres entidades a modificar.
   */
  private EntidadesAModificar obtenerEntidades(Long expedienteId, Long solicitudId) {
    Optional<ExpedienteConId> expedienteOpt = expedienteDAO.findById(expedienteId);
    Optional<SolicitudConId> solicitudOpt = solicitudDAO.findById(solicitudId);

    if (solicitudOpt.isEmpty()) {
      throw new IllegalArgumentException("ERROR: Solicitud no encontrada");
    }
    if (expedienteOpt.isEmpty()) {
      throw new IllegalArgumentException("ERROR: Expediente no encontrado");
    }

    SolicitudConId solicitud = solicitudOpt.get();
    ExpedienteConId expediente = expedienteOpt.get();
    int anho = expediente.getAnho();

    PresupuestoConId presupuesto = presupuestoSecresDAO.getByAnho(anho);
    if (presupuesto == null) {
      throw new IllegalArgumentException("ERROR: No existe presupuesto para el año " + anho);
    }

    return new EntidadesAModificar(expediente, solicitud, presupuesto);
  }

  /**
   * Guarda las entidades expediente, solicitud y presupuesto en la base de datos.
   * 
   * @param expediente  Expediente modificado.
   * @param solicitud   Solicitud modificada.
   * @param presupuesto Presupuesto modificado.
   */
  private void guardarCambios(ExpedienteConId expediente, SolicitudConId solicitud, PresupuestoConId presupuesto) {
    expedienteDAO.save(expediente);
    solicitudDAO.save(solicitud);
    presupuestoSecresDAO.save(presupuesto);
  }
}
