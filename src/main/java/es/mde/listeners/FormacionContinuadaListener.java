package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.FormacionContinuadaConId;
import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.repositorios.FormacionContinuadaDAO;
import es.mde.servicios.FormacionContinuadaServicio;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class FormacionContinuadaListener {

  private static FormacionContinuadaServicio formacionContinuadaServicio;

  @Autowired
  public void init(FormacionContinuadaServicio servicio) {
    formacionContinuadaServicio = servicio;
  }

  @PrePersist
  public void crearFormacion(FormacionContinuadaConId formacion) {
    formacionContinuadaServicio.actualizarSolicitud(formacion);
  }

  // TODO Arreglar este apaño
  private static final ThreadLocal<Boolean> enActualizacion = ThreadLocal.withInitial(() -> false);

  @PreUpdate
  public void actualizar(FormacionContinuadaConId solicitud) {
    if (enActualizacion.get()) return;
    try {
      enActualizacion.set(true);
      formacionContinuadaServicio.actualizarSolicitud(solicitud);
    } finally {
      enActualizacion.remove();
    }
  }
  
  @PostLoad
  public void calcularCoste(FormacionContinuadaConId solicitud) {
    solicitud.setCosteCentimos(formacionContinuadaServicio.calcularCosteCentimos(solicitud));
  }
}
