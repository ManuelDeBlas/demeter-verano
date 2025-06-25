package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.ActivacionAmpliadaConId;
import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.repositorios.ActivacionAmpliadaDAO;
import es.mde.servicios.ActivacionAmpliadaServicio;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class ActivacionAmpliadaListener {
  
  private static ActivacionAmpliadaServicio activacionAmpliadaServicio;
  
  @Autowired
  public void init(ActivacionAmpliadaServicio activacionAmpliadaServicio) {
    this.activacionAmpliadaServicio = activacionAmpliadaServicio;
  }
  
  @PrePersist
  public void crearActivacionAmpliada(ActivacionAmpliadaConId solicitud) {
    activacionAmpliadaServicio.actualizarSolicitud(solicitud);
  }
  
  // TODO Arreglar este apaño
  private static final ThreadLocal<Boolean> enActualizacion = ThreadLocal.withInitial(() -> false);

  @PreUpdate
  public void actualizar(ActivacionAmpliadaConId solicitud) {
    if (enActualizacion.get()) return;
    try {
      enActualizacion.set(true);
      activacionAmpliadaServicio.actualizarSolicitud(solicitud);
    } finally {
      enActualizacion.remove();
    }
  }
  
  @PostLoad
  public void calcularCoste(ActivacionAmpliadaConId solicitud) {
    solicitud.setCosteCentimos(activacionAmpliadaServicio.calcularCosteCentimos(solicitud));
  }

}
