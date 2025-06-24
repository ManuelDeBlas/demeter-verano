package es.mde.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.FormacionContinuadaConId;
import es.mde.repositorios.FormacionContinuadaDAO;
import es.mde.servicios.FormacionContinuadaServicio;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class FormacionContinuadaListener {

  private static FormacionContinuadaDAO formacionContinuadaDAO;
  private static FormacionContinuadaServicio formacionContinuadaServicio;

  @Autowired
  public void init(FormacionContinuadaDAO dao, FormacionContinuadaServicio servicio) {
    formacionContinuadaDAO = dao;
    formacionContinuadaServicio = servicio;
  }

  @PrePersist
  public void crearFormacion(FormacionContinuadaConId formacion) {
    formacionContinuadaServicio.crearSolicitud(formacion);
  }

  @PreUpdate
  public void actualizarFormacion(FormacionContinuadaConId formacion) {
    formacionContinuadaServicio.actualizarSolicitud(formacion.getId(), formacion);
  }
}
