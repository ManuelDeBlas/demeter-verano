package es.mde.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import es.mde.repositorios.ExpedienteDAO;
import es.mde.servicios.ExpedienteServicio;

/**
 * Controlador REST para gestionar los expedientes. Proporciona endpoints para
 * asignar y desasignar solicitudes.
 * 
 * Este controlador utiliza el servicio {@link ExpedienteServicio} para realizar
 * las operaciones relacionadas con los expedientes.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestController
public class ExpedienteController {

  private final ExpedienteServicio expedienteServicio;
  private final ExpedienteDAO expedienteDAO;

  /**
   * Constructor que inyecta el servicio de expedientes.
   * 
   * @param expedienteServicio Servicio para la lógica de negocio de expedientes.
   */
  @Autowired
  public ExpedienteController(ExpedienteServicio expedienteServicio, ExpedienteDAO expedienteDAO) {
    this.expedienteServicio = expedienteServicio;
    this.expedienteDAO = expedienteDAO;
  }

  @GetMapping("/expedientes/coste-expediente/{numeroExpediente}")
  @ResponseBody
  public int getCosteCentimosExpedienteByNumeroExpediente(@PathVariable("numeroExpediente") String numeroExpediente) {

    return expedienteDAO.getCosteCentimosExpedienteByNumeroExpediente(numeroExpediente);
  }

  /**
   * Asigna una solicitud a un expediente.
   * 
   * Este endpoint permite asignar una solicitud a un expediente específico.
   * Además, se envía una notificación tras completar la operación.
   * 
   * @param expedienteId ID del expediente al que se asignará la solicitud.
   * @param solicitudId  ID de la solicitud que se asignará.
   * @return Respuesta HTTP con el resultado de la operación.
   */
  @PatchMapping("/expedientes/{expedienteId}/asignar-solicitud/{solicitudId}")
  public ResponseEntity<String> asignarSolicitud(@PathVariable("expedienteId") Long expedienteId,
      @PathVariable("solicitudId") Long solicitudId) {
    ResponseEntity<String> respuestaEntity = null;
    try {
      respuestaEntity = ResponseEntity.ok(expedienteServicio.asignarSolicitudAExpediente(expedienteId, solicitudId));
    } catch (Exception e) {
      respuestaEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    return respuestaEntity;
  }

  /**
   * Desasigna una solicitud de un expediente.
   * 
   * Este endpoint permite desasignar una solicitud de un expediente específico.
   * Además, se envía una notificación tras completar la operación.
   * 
   * @param expedienteId ID del expediente del que se desasignará la solicitud.
   * @param solicitudId  ID de la solicitud que se desasignará.
   * @return Respuesta HTTP con el resultado de la operación.
   */
  @PatchMapping("/expedientes/{expedienteId}/desasignar-solicitud/{solicitudId}")
  public ResponseEntity<String> desasignarSolicitud(@PathVariable("expedienteId") Long expedienteId,
      @PathVariable("solicitudId") Long solicitudId) {
    ResponseEntity<String> respuestaEntity = null;
    try {
      respuestaEntity = ResponseEntity
          .ok(expedienteServicio.desasignarSolicitudDeExpediente(expedienteId, solicitudId));
    } catch (Exception e) {
      respuestaEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    return respuestaEntity;
  }

}
