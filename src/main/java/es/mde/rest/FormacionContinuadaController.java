package es.mde.rest;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.mde.entidades.FormacionContinuadaConId;
import es.mde.servicios.FormacionContinuadaServicio;

/**
 * Controlador REST para gestionar las formaciones continuadas.
 * 
 * Define los endpoints específicos para la entidad
 * {@link FormacionContinuadaConId}.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestController
public class FormacionContinuadaController extends AbstractSolicitudController<FormacionContinuadaConId> {

  /**
   * Servicio que contiene la lógica de negocio para formación continuada.
   */
  private final FormacionContinuadaServicio formacionContinuadaServicio;

  /**
   * Constructor para la inyección de dependencias del servicio.
   * 
   * @param formacionContinuadaServicio servicio para gestionar formación
   *                                    continuada
   */
  public FormacionContinuadaController(FormacionContinuadaServicio formacionContinuadaServicio) {
    this.formacionContinuadaServicio = formacionContinuadaServicio;
  }

  /**
   * Endpoint para crear una nueva solicitud de formación continuada.
   * 
   * @param formacionContinuadaConId datos de la solicitud recibidos en el cuerpo
   *                                 de la petición HTTP
   * @return ResponseEntity con el estado HTTP y el recurso creado con enlaces
   *         HATEOAS
   */
  @PostMapping("/formaciones-continuadas")
  public ResponseEntity<?> crearFormacionContinuada(@RequestBody FormacionContinuadaConId formacionContinuadaConId) {

    return crearSolicitud(formacionContinuadaConId, formacionContinuadaServicio::crearSolicitud,
        "formaciones-continuadas");
  }

  /**
   * Endpoint para actualizar una solicitud existente de formación continuada.
   * 
   * @param id                       identificador de la solicitud a actualizar
   *                                 (extraído de la URL)
   * @param formacionContinuadaConId datos actualizados recibidos en el cuerpo de
   *                                 la petición HTTP
   * @return ResponseEntity con el estado HTTP y el recurso actualizado con
   *         enlaces HATEOAS
   */
  @PutMapping("/formaciones-continuadas/{id}")
  public ResponseEntity<?> actualizar(@PathVariable Long id,
      @RequestBody FormacionContinuadaConId formacionContinuadaConId) {

    return actualizarSolicitud(id, formacionContinuadaConId, formacionContinuadaServicio::actualizarSolicitud,
        "formaciones-continuadas");
  }

}
