package es.mde.rest;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.mde.entidades.ActivacionAmpliadaConId;
import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.servicios.PrestacionServiciosUnidadServicio;

/**
 * Controlador REST para gestionar las solicitudes de prestación de servicios de
 * unidad.
 * 
 * Define el endpoint específico para la entidad
 * {@link PrestacionServiciosUnidadConId}.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestController
public class PrestacionServiciosController extends AbstractSolicitudController<PrestacionServiciosUnidadConId> {

  private final PrestacionServiciosUnidadServicio prestacionServiciosUnidadServicio;

  /**
   * Constructor para la inyección de dependencias del servicio.
   * 
   * @param prestacionServiciosUnidadServicio servicio para gestionar prestación
   *                                          de servicios
   */
  public PrestacionServiciosController(PrestacionServiciosUnidadServicio prestacionServiciosUnidadServicio) {
    this.prestacionServiciosUnidadServicio = prestacionServiciosUnidadServicio;
  }

  /**
   * Endpoint para crear una nueva solicitud de prestación de servicios de unidad.
   * 
   * @param prestacionServiciosUnidadConId datos de la solicitud recibidos en el
   *                                       cuerpo de la petición HTTP
   * @return ResponseEntity con el estado HTTP y el recurso creado con enlaces
   *         HATEOAS
   */
  @PostMapping("/prestaciones-servicios-unidad")
  public ResponseEntity<?> crearPrestacionServiciosUnidad(
      @RequestBody PrestacionServiciosUnidadConId prestacionServiciosUnidadConId) {

    return crearSolicitud(prestacionServiciosUnidadConId, prestacionServiciosUnidadServicio::crearSolicitud,
        "prestaciones-servicios-unidad");
  }

  /**
   * Endpoint para actualizar una activación ampliada existente.
   * 
   * @param id        identificador de la solicitud a actualizar (extraído de la
   *                  URL)
   * @param solicitud datos actualizados recibidos en el cuerpo HTTP
   * @return ResponseEntity con la respuesta HTTP y el recurso actualizado con sus
   *         links
   */
  @PutMapping("/prestaciones-servicios-unidad/{id}")
  public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody PrestacionServiciosUnidadConId solicitud) {
    System.err.println("controller" + solicitud.getCosteCentimos());
    return actualizarSolicitud(id, solicitud, prestacionServiciosUnidadServicio::actualizarSolicitud,
        "prestaciones-servicios-unidad");
  }

}
