package es.mde.servicios;

import org.springframework.stereotype.Service;

import es.mde.entidades.FormacionContinuadaConId;
import es.mde.repositorios.CosteFormacionContinuadaDAO;
import es.mde.repositorios.CostePorDiaDAO;
import es.mde.repositorios.SolicitudDAO;
import jakarta.persistence.EntityManager;

/**
 * Servicio específico para gestionar las solicitudes de Formación Continuada.
 * Calcula el coste en función del SMI y un coeficiente por escala, ambos
 * obtenidos de la base de datos.
 * 
 * Extiende de {@link AbstractSolicitudServicio} para heredar la lógica común de
 * gestión de solicitudes.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Service
public class FormacionContinuadaServicio extends AbstractSolicitudServicio<FormacionContinuadaConId> {

  private final CosteFormacionContinuadaDAO costeFormacionContinuadaDAO;

  /**
   * Constructor que inyecta los DAOs y el EntityManager necesarios para el
   * servicio.
   * 
   * @param entityManager               El EntityManager de JPA.
   * @param solicitudDAO                DAO para la entidad de solicitudes.
   * @param costePorDiaDAO              DAO para los costes estándar por día (no
   *                                    se usa en este caso).
   * @param costeFormacionContinuadaDAO DAO específico para obtener los costes
   *                                    asociados a la formación.
   */
  public FormacionContinuadaServicio(EntityManager entityManager, SolicitudDAO solicitudDAO,
      CostePorDiaDAO costePorDiaDAO, CosteFormacionContinuadaDAO costeFormacionContinuadaDAO,
      EmailSenderServicio emailSenderServicio) {
    super(entityManager, solicitudDAO, costePorDiaDAO, emailSenderServicio);
    this.costeFormacionContinuadaDAO = costeFormacionContinuadaDAO;
  }

  /**
   * Calcula el coste total de la formación continuada en céntimos de euro.
   * 
   * Se calcula como: {@code smi * coefEscala * mesesDuracion}, donde:
   * <ul>
   * <li>{@code smi} es el salario mínimo interprofesional en céntimos</li>
   * <li>{@code coefEscala} es un valor decimal asociado a la escala del
   * reservista</li>
   * <li>{@code mesesDuracion} es la duración de la formación en meses</li>
   * </ul>
   * 
   * @param formacionContinuadaConId La solicitud de formación continuada.
   * @return El coste calculado en céntimos.
   * @throws IllegalArgumentException si no se encuentra alguna de las claves
   *                                  necesarias.
   */
  @Override
  protected int calcularCosteCentimos(FormacionContinuadaConId formacionContinuadaConId) {
    int smi = costeFormacionContinuadaDAO.findByClave("smi-centimos").getValor();
    float cantidad = costeFormacionContinuadaDAO.findByClave(formacionContinuadaConId.getEscala()).getValor();
    return (int) (smi * cantidad * formacionContinuadaConId.getDuracionMeses());
  }

}
