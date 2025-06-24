package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.PresupuestoConId;

/**
 * Repositorio JPA para gestionar presupuestos. Expone un recurso REST en la
 * ruta "presupuestos-secres".
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "presupuestos-secres", itemResourceRel = "presupuesto-secres", collectionResourceRel = "presupuestos-secres")
public interface PresupuestoSecresDAO extends JpaRepository<PresupuestoConId, Long> {

  /**
   * Obtiene un presupuesto por el año.
   * 
   * @param anho el año del presupuesto
   * @return el presupuesto correspondiente al año
   */
  PresupuestoConId getByAnho(int anho);

}
