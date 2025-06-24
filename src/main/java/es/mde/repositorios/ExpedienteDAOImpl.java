package es.mde.repositorios;

import org.springframework.beans.factory.annotation.Autowired;

import es.mde.entidades.ExpedienteConId;
import es.mde.secres.Solicitud;

/**
 * Implementación personalizada del repositorio para gestionar los expedientes..
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
public class ExpedienteDAOImpl implements ExpedienteDAOCustom {

  /**
   * Repositorio JPA para gestionar los expedientes.
   */
  @Autowired
  private ExpedienteDAO expedienteDAO;

  /**
   * Obtiene el coste total en céntimos de todas las solicitudes asociadas a un
   * expediente, identificado por su número de expediente.
   * 
   * @param numeroExpediente el número único que identifica al expediente.
   * @return la suma de los costes en céntimos de todas las solicitudes asociadas
   *         al expediente.
   * @throws IllegalArgumentException si no se encuentra el expediente con el
   *                                  número dado.
   */
  @Override
  public int getCosteCentimosExpedienteByNumeroExpediente(String numeroExpediente) {
    int costeCentimos = 0;
    ExpedienteConId expediente = expedienteDAO.findByNumeroExpediente(numeroExpediente)
        .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado: " + numeroExpediente));
    for (Solicitud solicitud : expediente.getSolicitudes()) {
      costeCentimos += solicitud.getCosteCentimos();
    }

    return costeCentimos;
  }

}
