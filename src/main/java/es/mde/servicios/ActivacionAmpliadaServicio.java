package es.mde.servicios;

import org.springframework.stereotype.Service;

import es.mde.entidades.ActivacionAmpliadaConId;
import es.mde.repositorios.CostePorDiaDAO;
import es.mde.repositorios.SolicitudDAO;
import jakarta.persistence.EntityManager;

/**
 * Servicio para gestionar las solicitudes de tipo
 * {@link ActivacionAmpliadaConId}. Hereda la lógica común de
 * {@link AbstractSolicitudServicio} y puede extender funcionalidades
 * específicas para este tipo de solicitud.
 * 
 * Se marca con @Service para ser detectado y gestionado por Spring.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Service
public class ActivacionAmpliadaServicio extends AbstractSolicitudServicio<ActivacionAmpliadaConId> {

  /**
   * Constructor que inyecta las dependencias necesarias para operar.
   * 
   * @param entityManager  EntityManager para operaciones JPA.
   * @param solicitudDAO   DAO para gestionar solicitudes.
   * @param costePorDiaDAO DAO para obtener costes diarios.
   */
  public ActivacionAmpliadaServicio(EntityManager entityManager, SolicitudDAO solicitudDAO,
      CostePorDiaDAO costePorDiaDAO, EmailSenderServicio emailSenderServicio) {
    super(entityManager, solicitudDAO, costePorDiaDAO, emailSenderServicio);
  }

}
