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
  @PreUpdate
  public void preGuardarYPreActualizar(ActivacionAmpliadaConId solicitud) {
    activacionAmpliadaServicio.comprobarViabilidadSolicitud(solicitud);
    activacionAmpliadaServicio.comprobarRechazoSolicitud(solicitud);
  }
  
  @PostLoad
  public void calcularCoste(ActivacionAmpliadaConId solicitud) {
    solicitud.setCosteCentimos(activacionAmpliadaServicio.calcularCosteCentimos(solicitud));
  }

}
