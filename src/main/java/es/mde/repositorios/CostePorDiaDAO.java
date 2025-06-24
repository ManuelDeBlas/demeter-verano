package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.CostePorDiaConId;

/**
 * Interfaz DAO para la entidad {@link CostePorDiaConId}. Proporciona
 * operaciones CRUD y consultas específicas sobre los costes por día asociados a
 * diferentes empleos.
 * 
 * Esta interfaz se expone como recurso REST con rutas personalizadas: - Path
 * base: /costes-por-dia - Nombre del recurso individual: coste-por-dia - Nombre
 * de la colección: costes-por-dia
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "costes-por-dia", itemResourceRel = "coste-por-dia", collectionResourceRel = "costes-por-dia")
public interface CostePorDiaDAO extends JpaRepository<CostePorDiaConId, Long> {

  /**
   * Busca un coste por día según el empleo.
   * 
   * @param empleo nombre del empleo a buscar.
   * @return el coste por día asociado al empleo o null si no existe.
   */
  CostePorDiaConId findByEmpleo(@Param("empleo") String empleo);

}
