package es.mde.util;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.mde.dtos.SolicitudConLinks;
import es.mde.entidades.SolicitudConId;

/**
 * Utilidad para cargar hipervínculos HATEOAS en objetos DTO de tipo
 * {@link SolicitudConLinks}.
 * 
 * Esta clase genera enlaces relevantes para una solicitud concreta, como el
 * enlace a sí misma (self), al expediente asociado y al reservista
 * correspondiente.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
public class CargadorLinks {

  /**
   * Carga enlaces HATEOAS genéricos para una solicitud concreta.
   * 
   * Copia las propiedades de la entidad {@link SolicitudConId} al DTO
   * {@link SolicitudConLinks} y añade enlaces como `self`, `expediente` y
   * `reservista`.
   * 
   * @param <T>       Tipo de la solicitud (subtipo de {@link SolicitudConId}).
   * @param solicitud La solicitud desde la que se generarán los enlaces.
   * @param rutaApi   La ruta base del controlador REST (por ejemplo:
   *                  "activaciones", "formaciones").
   * @return Un DTO de tipo {@link SolicitudConLinks} con los enlaces generados.
   */
  public static <T extends SolicitudConId> SolicitudConLinks cargarLinksGenerico(T solicitud, String rutaApi) {
    SolicitudConLinks dto = new SolicitudConLinks();
    BeanUtils.copyProperties(solicitud, dto);

    Map<String, Map<String, String>> links = new LinkedHashMap<>();

    URI selfUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/" + rutaApi + "/{id}")
        .buildAndExpand(solicitud.getId()).toUri();

    links.put("self", Map.of("href", selfUri.toString()));
    links.put("expediente", Map.of("href", selfUri.toString() + "/expediente"));
    links.put("reservista", Map.of("href", selfUri.toString() + "/reservista"));

    dto.set_links(links);

    return dto;
  }

}
