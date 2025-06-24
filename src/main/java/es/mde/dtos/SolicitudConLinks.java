package es.mde.dtos;

import java.util.Map;

import es.mde.entidades.SolicitudConId;

/**
 * DTO que extiende de SolicitudConId e incluye enlaces HATEOAS. Utilizado para
 * exponer una solicitud junto con los enlaces que permiten navegar la API.
 */
public class SolicitudConLinks extends SolicitudConId {

  /**
   * Mapa que contiene los enlaces HATEOAS asociados a esta solicitud. La
   * estructura típica sería: { "self": { "href": "/api/solicitudes/1" },
   * "reservista": { "href": "/api/solicitudes/1/reservista" }, ... }
   */
  private Map<String, Map<String, String>> _links;

  /**
   * Devuelve los enlaces HATEOAS asociados.
   *
   * @return Mapa con los enlaces
   */
  public Map<String, Map<String, String>> get_links() {
    return _links;
  }

  /**
   * Establece los enlaces HATEOAS.
   *
   * @param links Mapa con los enlaces
   */
  public void set_links(Map<String, Map<String, String>> links) {
    this._links = links;
  }
}
