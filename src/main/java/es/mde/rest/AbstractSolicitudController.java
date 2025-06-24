package es.mde.rest;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.mde.dtos.SolicitudConLinks;
import es.mde.entidades.SolicitudConId;
import es.mde.util.CargadorLinks;

/**
 * Controlador abstracto para manejar operaciones comunes sobre solicitudes.
 * Permite crear y actualizar solicitudes.
 * 
 * @param <T> tipo de solicitud que extiende {@link SolicitudConId}
 */
public abstract class AbstractSolicitudController<T extends SolicitudConId> {

  private static final Logger log = LoggerFactory.getLogger(AbstractSolicitudController.class);

  /**
   * Método genérico para crear una nueva solicitud.
   * 
   * @param solicitud objeto solicitud a crear
   * @param creador   función que realiza la creación y devuelve la solicitud
   *                  creada
   * @param rutaApi   ruta base de la API para construir los enlaces HATEOAS
   * @return ResponseEntity con el estado HTTP y el cuerpo con la solicitud creada
   *         y sus links
   */
  ResponseEntity<?> crearSolicitud(T solicitud, Function<T, T> creador, String rutaApi) {
    try {
      T solicitudCreada = creador.apply(solicitud);
      SolicitudConLinks dto = CargadorLinks.cargarLinksGenerico(solicitudCreada, rutaApi);
      URI selfUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(solicitudCreada.getId()).toUri();
      return ResponseEntity.created(selfUri).body(dto);
    } catch (Exception e) {
      log.error("Error creando solicitud", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  /**
   * Método genérico para actualizar una solicitud existente.
   * 
   * @param id           identificador de la solicitud a actualizar
   * @param solicitud    objeto solicitud con datos para actualización
   * @param actualizador función que realiza la actualización y devuelve la
   *                     solicitud actualizada
   * @param rutaApi      ruta base de la API para construir los enlaces HATEOAS
   * @return ResponseEntity con el estado HTTP y el cuerpo con la solicitud
   *         actualizada y sus links
   */
  protected ResponseEntity<?> actualizarSolicitud(Long id, T solicitud, BiFunction<Long, T, T> actualizador,
      String rutaApi) {
    try {
      T solicitudActualizada = actualizador.apply(id, solicitud);
      SolicitudConLinks dto = CargadorLinks.cargarLinksGenerico(solicitudActualizada, rutaApi);
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      log.error("Error actualizando solicitud con id " + id, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
