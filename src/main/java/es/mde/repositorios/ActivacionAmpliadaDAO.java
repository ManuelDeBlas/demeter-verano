package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.ActivacionAmpliadaConId;

/**
 * Repositorio JPA para gestionar las activaciones ampliadas. Proporciona
 * métodos CRUD para la entidad {@link ActivacionAmpliadaConId}.
 * 
 * Este repositorio está expuesto como un recurso REST con el path
 * "activaciones-ampliadas".
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "activaciones-ampliadas", itemResourceRel = "activacion-ampliada", collectionResourceRel = "activaciones-ampliadas")
public interface ActivacionAmpliadaDAO extends JpaRepository<ActivacionAmpliadaConId, Long> {

}
