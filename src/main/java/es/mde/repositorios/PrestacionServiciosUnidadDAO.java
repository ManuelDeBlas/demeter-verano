package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.PrestacionServiciosUnidadConId;

/**
 * Repositorio JPA para gestionar las prestaciones de servicios en unidades.
 * Proporciona métodos CRUD para la entidad
 * {@link PrestacionServiciosUnidadConId}.
 * 
 * Este repositorio está expuesto como un recurso REST con el path
 * "prestaciones-servicios-unidad".
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "prestaciones-servicios-unidad", itemResourceRel = "prestacion-servicios-unidad", collectionResourceRel = "prestaciones-servicios-unidad")
public interface PrestacionServiciosUnidadDAO extends JpaRepository<PrestacionServiciosUnidadConId, Long> {

}
