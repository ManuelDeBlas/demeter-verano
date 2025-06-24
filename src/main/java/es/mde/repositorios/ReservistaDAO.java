package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.ReservistaConId;

/**
 * Repositorio JPA para gestionar los reservistas. Proporciona métodos CRUD para
 * la entidad {@link ReservistaConId}.
 * 
 * Este repositorio está expuesto como un recurso REST con el path
 * "reservistas".
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "reservistas", itemResourceRel = "reservista", collectionResourceRel = "reservistas")
public interface ReservistaDAO extends JpaRepository<ReservistaConId, Long> {

}
