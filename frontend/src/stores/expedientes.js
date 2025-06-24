import { API_BASE_URL } from "@/config/app";
import { getId } from "@/utils/utils";
import { crearStore } from "@/stores/fabricaStore";
import { useSolicitudesStore } from "@/stores/solicitudes";
import { usePresupuestosSecresStore } from "@/stores/presupuestos-secres";
import { get, patch } from "@/utils/api-service";

export const useExpedientesStore = crearStore("expedientes", {
  async anhadirExpediente(expedienteAAnhadir) {
    try {
      const expedienteEnStore = await this.anhadirElemento(expedienteAAnhadir);
      expedienteEnStore.solicitudes = [];
      expedienteEnStore.coste = 0;

      return "Expediente añadido correctamente";
    } catch (error) {
      return error;
    }
  },
  async editarExpediente(expedienteAEditar) {
    try {
      const solicitudesEnStore = expedienteAEditar.solicitudes;
      delete expedienteAEditar.solicitudes;
      const expedienteEnStore = await this.editarElemento(expedienteAEditar);
      expedienteEnStore.solicitudes = solicitudesEnStore;

      return "Expediente editado correctamente";
    } catch (error) {
      return error;
    }
  },
  async agregarSolicitudAExpediente(solicitud, expediente) {
    try {
      const expedienteId = getId(expediente._links.self.href);
      const solicitudId = getId(solicitud._links.self.href);
      const respuesta = await patch(
        `${API_BASE_URL}/expedientes/${expedienteId}/asignar-solicitud/${solicitudId}`
      );
      if (respuesta.status === 200) {
        expediente.solicitudes.push(solicitud);
        solicitud.expediente = expediente;
        solicitud.estado = "ACEPTADA_PENDIENTE_PUBLICACION";
        await usePresupuestosSecresStore().cargarElementos();
        expediente.costeCentimos = this.calcularCosteExpediente(expediente);

        return "Solicitud añadida al expediente correctamente";
      }
    } catch (error) {
      return error.response.data;
    }
  },
  async eliminarSolicitudDeExpediente(solicitud, expediente) {
    try {
      const expedienteId = getId(expediente._links.self.href);
      const solicitudId = getId(solicitud._links.self.href);
      const respuesta = await patch(
        `${API_BASE_URL}/expedientes/${expedienteId}/desasignar-solicitud/${solicitudId}`
      );
      if (respuesta.status === 200) {
        const indice = expediente.solicitudes.findIndex(
          (e) => e._links.self.href === solicitud._links.self.href
        );
        if (indice !== -1) {
          expediente.solicitudes.splice(indice, 1);
        }
        solicitud.expediente = null;
        solicitud.estado = "PENDIENTE_EVALUACION";
        await usePresupuestosSecresStore().cargarElementos();
        expediente.costeCentimos = calcularCosteExpediente(expediente);

        return "Solicitud eliminada del expediente correctamente";
      }
    } catch (error) {
      return error.response.data;
    }
  },
  async cargarSolicitudesEnExpediente(expediente) {
    expediente.solicitudes = [];
    const solicitudesEnExpedienteAPI = await get(
      expediente._links.solicitudes.href
    );
    const embedded = solicitudesEnExpedienteAPI.data._embedded;
    const keys = Object.keys(embedded);
    const firstKey = keys[0];
    const solicitudesAPI = embedded[firstKey];
    for (let solicitudAPI of solicitudesAPI) {
      const soliditudEnStore = useSolicitudesStore().recuperarObjetoDelStore(
        solicitudAPI._links.self.href
      );
      expediente.solicitudes.push(soliditudEnStore);
      // soliditudEnStore.expediente = expediente;  // Esto genera una referencia circular.
    }
    expediente.costeCentimos = calcularCosteExpediente(expediente);
  },
  calcularCosteExpediente(expediente) {
    return expediente.solicitudes.reduce((total, solicitud) => {
      return total + (solicitud[costeCentimos] || 0);
    }, 0);
  },
});
