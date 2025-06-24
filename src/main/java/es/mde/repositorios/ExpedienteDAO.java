package es.mde.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.ExpedienteConId;

/**
 * Repositorio JPA para gestionar los expedientes. Proporciona métodos CRUD para
 * la entidad {@link ExpedienteConId}.
 * 
 * Este repositorio está expuesto como un recurso REST con el path
 * "expedientes". Además, extiende la interfaz personalizada
 * {@link ExpedienteDAOCustom} para consultas específicas.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "expedientes", itemResourceRel = "expediente", collectionResourceRel = "expedientes")
public interface ExpedienteDAO extends JpaRepository<ExpedienteConId, Long>, ExpedienteDAOCustom {

  /**
   * Busca un expediente por su número identificativo único.
   * 
   * @param numeroExpediente número de expediente a buscar.
   * @return Optional que contiene el expediente si se encuentra, o vacío si no
   *         existe.
   */
  Optional<ExpedienteConId> findByNumeroExpediente(String numeroExpediente);

}
