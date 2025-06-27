package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.ActivacionAmpliadaConId;
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
  @PreUpdate
  public void preGuardarYPreActualizar(FormacionContinuadaConId solicitud) {
    formacionContinuadaServicio.comprobarViabilidadSolicitud(solicitud);
    formacionContinuadaServicio.comprobarRechazoSolicitud(solicitud);
  }
  
  @PostLoad
  public void calcularCoste(FormacionContinuadaConId solicitud) {
    solicitud.setCosteCentimos(formacionContinuadaServicio.calcularCosteCentimos(solicitud));
  }
}
