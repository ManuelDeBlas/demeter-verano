package es.mde.listeners;

import javax.sound.midi.VoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.mde.entidades.ActivacionAmpliadaConId;
import es.mde.entidades.PrestacionServiciosUnidadConId;
import es.mde.repositorios.PrestacionServiciosUnidadDAO;
import es.mde.servicios.PrestacionServiciosUnidadServicio;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
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
  @PreUpdate
  public void preGuardarYPreActualizar(PrestacionServiciosUnidadConId solicitud) {
    prestacionServiciosUnidadServicio.comprobarViabilidadSolicitud(solicitud);
    prestacionServiciosUnidadServicio.comprobarRechazoSolicitud(solicitud);
    System.err.println("Coste antes de actualizar: " + solicitud.getCosteCentimos());
    int nuevoCoste = prestacionServiciosUnidadServicio.calcularCosteCentimos(solicitud);
    System.err.println("Nuevo coste: " + nuevoCoste);
    if (solicitud.getCosteCentimos() != nuevoCoste) {
      solicitud.setCosteCentimos(nuevoCoste);
    }
    System.err.println("Coste después de actualizar: " + solicitud.getCosteCentimos());
  }

  @PostPersist
  @PostUpdate
  public void postGuardarYActualizar(PrestacionServiciosUnidadConId solicitud) {
    System.err.println("Coste antes de actualizar: " + solicitud.getCosteCentimos());
    int nuevoCoste = prestacionServiciosUnidadServicio.calcularCosteCentimos(solicitud);
    System.err.println("Nuevo coste: " + nuevoCoste);
    if (solicitud.getCosteCentimos() != nuevoCoste) {
      solicitud.setCosteCentimos(nuevoCoste);
    }
    System.err.println("Coste después de actualizar: " + solicitud.getCosteCentimos());
  }

}
