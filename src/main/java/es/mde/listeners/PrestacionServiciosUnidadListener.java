package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.repositorios.PrestacionServiciosUnidadDAO;
import es.mde.servicios.PrestacionServiciosUnidadServicio;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class PrestacionServiciosUnidadListener {

  private static PrestacionServiciosUnidadDAO prestacionServiciosUnidadDAO;
  private static PrestacionServiciosUnidadServicio prestacionServiciosUnidadServicio;

  @Autowired
  public void init(PrestacionServiciosUnidadDAO dao, PrestacionServiciosUnidadServicio servicio) {
    prestacionServiciosUnidadDAO = dao;
    prestacionServiciosUnidadServicio = servicio;
  }

  @PrePersist
  public void crearPrestacion(PrestacionServiciosUnidadConId prestacion) {
    prestacionServiciosUnidadServicio.crearSolicitud(prestacion);
  }

  @PreUpdate
  public void actualizarPrestacion(PrestacionServiciosUnidadConId prestacion) {
    prestacionServiciosUnidadServicio.actualizarSolicitud(prestacion.getId(), prestacion);
  }
}
