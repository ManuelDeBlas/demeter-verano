package es.mde.repositorios;

import es.mde.entidades.ExpedienteConId;

/**
 * Interfaz personalizada para el repositorio JPA de expedientes. Define métodos
 * específicos que no están incluidos en JpaRepository, centrados en operaciones
 * personalizadas sobre la entidad {@link ExpedienteConId}.
 * 
 * Esta interfaz complementa la funcionalidad básica CRUD.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
public interface ExpedienteDAOCustom {

  /**
   * Obtiene el coste total en céntimos asociado a un expediente identificado por
   * su número de expediente.
   * 
   * @param numeroExpediente el número identificador único del expediente.
   * @return el coste total en céntimos para ese expediente.
   */
  int getCosteCentimosExpedienteByNumeroExpediente(String numeroExpediente);

}
