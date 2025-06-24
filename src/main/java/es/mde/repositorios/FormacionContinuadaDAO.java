package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.FormacionContinuadaConId;

/**
 * Repositorio JPA para gestionar las formaciones continuadas. Proporciona
 * métodos CRUD para la entidad {@link FormacionContinuadaConId}.
 * 
 * Este repositorio está expuesto como un recurso REST con el path
 * "formaciones-continuadas".
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "formaciones-continuadas", itemResourceRel = "formacion-continuada", collectionResourceRel = "formaciones-continuadas")
public interface FormacionContinuadaDAO extends JpaRepository<FormacionContinuadaConId, Long> {

}
