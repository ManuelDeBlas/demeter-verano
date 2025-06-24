package es.mde.servicios;

import org.springframework.stereotype.Service;

import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.repositorios.CostePorDiaDAO;
import es.mde.repositorios.SolicitudDAO;
import jakarta.persistence.EntityManager;

/**
 * Servicio específico para gestionar las solicitudes de Prestación de Servicios
 * en Unidad.
 * 
 * Extiende de {@link AbstractSolicitudServicio} para heredar la lógica común de
 * creación, actualización y cálculo de costes de solicitudes.
 * 
 * No redefine el cálculo de coste, ya que usa el coste por día estándar basado
 * en el empleo del reservista.
 * 
 * @author ---
 * @version 1.0
 */
@Service
public class PrestacionServiciosUnidadServicio extends AbstractSolicitudServicio<PrestacionServiciosUnidadConId> {

  /**
   * Constructor que inyecta los DAOs y el EntityManager necesarios para el
   * servicio.
   * 
   * @param entityManager  El EntityManager de JPA.
   * @param solicitudDAO   DAO para gestionar las solicitudes.
   * @param costePorDiaDAO DAO para obtener los costes por día según el empleo.
   */
  public PrestacionServiciosUnidadServicio(EntityManager entityManager, SolicitudDAO solicitudDAO,
      CostePorDiaDAO costePorDiaDAO, EmailSenderServicio emailSenderServicio) {
    super(entityManager, solicitudDAO, costePorDiaDAO, emailSenderServicio);
  }

}
