package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.repositorios.PrestacionServiciosUnidadDAO;
import es.mde.servicios.PrestacionServiciosUnidadServicio;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class PrestacionServiciosUnidadListener {

  private static PrestacionServiciosUnidadServicio prestacionServiciosUnidadServicio;

  @Autowired
  public void init(PrestacionServiciosUnidadServicio servicio) {
    prestacionServiciosUnidadServicio = servicio;
  }

  @PrePersist
  public void crearPrestacion(PrestacionServiciosUnidadConId prestacion) {
    prestacionServiciosUnidadServicio.actualizarSolicitud(prestacion);
  }

  // TODO Arreglar este apaño
  private static final ThreadLocal<Boolean> enActualizacion = ThreadLocal.withInitial(() -> false);

  @PreUpdate
  public void actualizar(PrestacionServiciosUnidadConId solicitud) {
    if (enActualizacion.get()) return;
    try {
      enActualizacion.set(true);
      prestacionServiciosUnidadServicio.actualizarSolicitud(solicitud);
    } finally {
      enActualizacion.remove();
    }
  }
  
  @PostLoad
  public void calcularCoste(PrestacionServiciosUnidadConId solicitud) {
    solicitud.setCosteCentimos(prestacionServiciosUnidadServicio.calcularCosteCentimos(solicitud));
  }
}
