package es.mde.servicios;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import es.mde.entidades.PresupuestoSecresConId;
import es.mde.entidades.SolicitudConId;
import es.mde.repositorios.PresupuestoSecresDAO;
import es.mde.repositorios.SolicitudDAO;

@Service
public class PresupuestoSecresServicio {

  private final PresupuestoSecresDAO presupuestoSecresDAO;
  private final SolicitudDAO solicitudDAO;

  public PresupuestoSecresServicio(PresupuestoSecresDAO presupuestoSecresDAO,
      SolicitudDAO solicitudDAO) {
    this.presupuestoSecresDAO = presupuestoSecresDAO;
    this.solicitudDAO = solicitudDAO;
  }

  public int getCantidadCentimosRestante(int anho) {
    PresupuestoSecresConId presupuesto = presupuestoSecresDAO.getByAnho(anho);
    if (presupuesto == null) {
      throw new IllegalArgumentException("ERROR: No existe presupuesto para el año " + anho);
    }
    return presupuesto.getCantidadCentimosConcedido() - getCantidadCentimosGastado(anho);
  }

  private int getCantidadCentimosGastado(int anho) {
    int cantidad = 0;
    List<SolicitudConId> solicitudes = solicitudDAO.getByAnho(anho);
    for (SolicitudConId solicitud : solicitudes) {
      if (solicitud.isPagaSecres()) {
        cantidad += solicitud.getCosteCentimos();
      }
    }

    return cantidad;
  }
}
