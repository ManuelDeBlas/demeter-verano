package es.mde.rest;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.mde.entidades.ActivacionAmpliadaConId;
import es.mde.servicios.ActivacionAmpliadaServicio;

/**
 * Controlador REST para gestionar las activaciones ampliadas.
 * 
 * Define los endpoints específicos para la entidad
 * {@link ActivacionAmpliadaConId}.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestController
public class ActivacionAmpliadaController extends AbstractSolicitudController<ActivacionAmpliadaConId> {

  /**
   * Servicio que contiene la lógica de negocio para activaciones ampliadas.
   */
  private final ActivacionAmpliadaServicio activacionAmpliadaServicio;

  /**
   * Constructor para inyección de dependencia del servicio.
   * 
   * @param activacionAmpliadaServicio servicio para gestionar activaciones
   *                                   ampliadas
   */
  public ActivacionAmpliadaController(ActivacionAmpliadaServicio activacionAmpliadaServicio) {
    this.activacionAmpliadaServicio = activacionAmpliadaServicio;
  }

  /**
   * Endpoint para crear una nueva activación ampliada.
   * 
   * @param activacionAmpliadaConId datos de la solicitud recibidos en el cuerpo
   *                                HTTP
   * @return ResponseEntity con la respuesta HTTP y el recurso creado con sus
   *         links
   */
  @PostMapping("/activaciones-ampliadas")
  public ResponseEntity<?> crear(@RequestBody ActivacionAmpliadaConId activacionAmpliadaConId) {
    return crearSolicitud(activacionAmpliadaConId, activacionAmpliadaServicio::crearSolicitud,
        "activaciones-ampliadas");
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
  @PutMapping("/activaciones-ampliadas/{id}")
  public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ActivacionAmpliadaConId solicitud) {
    return actualizarSolicitud(id, solicitud, activacionAmpliadaServicio::actualizarSolicitud,
        "activaciones-ampliadas");
  }

}
