package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.PresupuestoSecresConId;
import es.mde.servicios.PresupuestoSecresServicio;
import jakarta.persistence.PostLoad;

@Component
public class PresupuestoSecresListener {

  private static PresupuestoSecresServicio presupuestoSecresServicio;

  @Autowired
  public void init(PresupuestoSecresServicio servicio) {
    presupuestoSecresServicio = servicio;
  }

  @PostLoad
  public void calcularGastado(PresupuestoSecresConId presupuestoSecresConId) {
    presupuestoSecresConId.setCantidadCentimosGastado(presupuestoSecresServicio
        .calcularCantidadCentimosGastado(presupuestoSecresConId.getAnho()));
  }
}
