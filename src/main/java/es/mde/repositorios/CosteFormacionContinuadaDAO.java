package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.CosteFormacionContinuadaConId;

/**
 * Interfaz DAO para la entidad {@link CosteFormacionContinuadaConId}. Extiende
 * JpaRepository para proveer operaciones CRUD y consultas sobre los costes de
 * formación continuada.
 * 
 * La anotación @RepositoryRestResource expone esta interfaz como un recurso
 * REST con las rutas personalizadas.
 * 
 * Path base: /costes-fc Nombre de recurso individual: coste-fc Nombre de
 * colección: costes-fc
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@RepositoryRestResource(path = "costes-fc", itemResourceRel = "coste-fc", collectionResourceRel = "costes-fc")
public interface CosteFormacionContinuadaDAO extends JpaRepository<CosteFormacionContinuadaConId, Long> {

  /**
   * Busca un coste de formación continuada por su clave única.
   * 
   * @param clave clave identificativa del coste.
   * @return el coste que corresponde a la clave o null si no existe.
   */
  CosteFormacionContinuadaConId findByClave(@Param("clave") String clave);

}
