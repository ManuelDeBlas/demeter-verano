package es.mde.repositorios;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.SolicitudConId;

/**
 * Repositorio JPA para gestionar las solicitudes. Proporciona métodos CRUD para la entidad
 * {@link SolicitudConId}.
 * 
 * Este repositorio está expuesto como un recurso REST con el path "solicitudes".
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "solicitudes", itemResourceRel = "solicitud",
    collectionResourceRel = "solicitudes")
public interface SolicitudDAO extends JpaRepository<SolicitudConId, Long> {

  List<SolicitudConId> findByFechaInicioBetween(LocalDate start, LocalDate end);
}
