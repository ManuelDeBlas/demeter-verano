package es.mde.servicios;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import es.mde.entidades.PresupuestoSecresConId;
import es.mde.entidades.SolicitudConId;
import es.mde.repositorios.PresupuestoSecresDAO;
import es.mde.repositorios.SolicitudDAO;
import es.mde.secres.Solicitud.Estados;

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
    LocalDate start = LocalDate.of(anho, 1, 1);
    LocalDate end = LocalDate.of(anho, 12, 31);
    List<SolicitudConId> solicitudes = solicitudDAO.findByFechaInicioBetween(start, end);
    for (SolicitudConId solicitud : solicitudes) {
      if (solicitud.isPagaSecres()
          && (solicitud.getEstado().equals(Estados.ACEPTADA_PENDIENTE_PUBLICACION)
              || solicitud.getEstado().equals(Estados.PUBLICADA))) {
        cantidad += solicitud.getCosteCentimos();
      }
    }

    return cantidad;
  }
}
