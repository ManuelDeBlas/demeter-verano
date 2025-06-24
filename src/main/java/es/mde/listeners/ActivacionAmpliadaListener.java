package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.ActivacionAmpliadaConId;
import es.mde.repositorios.ActivacionAmpliadaDAO;
import es.mde.servicios.ActivacionAmpliadaServicio;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class ActivacionAmpliadaListener {
  
  private static ActivacionAmpliadaDAO activacionAmpliadaDAO;
  private static ActivacionAmpliadaServicio activacionAmpliadaServicio;
  
  @Autowired
  public void init(ActivacionAmpliadaDAO activacionAmpliadaDAO, ActivacionAmpliadaServicio activacionAmpliadaServicio) {
    this.activacionAmpliadaDAO = activacionAmpliadaDAO;
    this.activacionAmpliadaServicio = activacionAmpliadaServicio;
  }
  
  @PrePersist
  public void crearActivacionAmpliada(ActivacionAmpliadaConId activacionAmpliada) {
    activacionAmpliadaServicio.crearSolicitud(activacionAmpliada);
  }
  
  @PreUpdate
  public void actualizarACtivacionAmpliada(ActivacionAmpliadaConId activacionAmpliada) {
    activacionAmpliadaServicio.actualizarSolicitud(activacionAmpliada.getId(), activacionAmpliada);
  }

}
