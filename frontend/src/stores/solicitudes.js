import { crearStore } from "@/stores/fabricaStore";
import { get } from "@/utils/api-service";
import { useReservistasStore } from "@/stores/reservistas";

export const useSolicitudesStore = crearStore("solicitudes", {
  async cargarReservistaEnSolicitud(solicitud) {
    const reservistaAPI = await get(solicitud._links.reservista.href);
    const reservistaEnStore = useReservistasStore().recuperarObjetoDelStore(
      reservistaAPI.data._links.self.href
    );
    solicitud.reservista = reservistaEnStore;
    // reservistaEnStore.solicitudes.push(solicitud);  // Esto genera una referencia circular.
  },
  async anhadirSolicitud(solicitudACrear) {
    try {
      solicitudACrear.reservista = solicitudACrear.reservista._links.self.href;
      console.log(solicitudACrear);
      const tipoSolicitud = solicitudACrear.tipoSolicitud;
      const solicitudEnStore = await useSolicitudesStore().anhadirElemento(
        solicitudACrear
      );
      await useSolicitudesStore().cargarReservistaEnSolicitud(solicitudEnStore);
      solicitudEnStore.tipoSolicitud = tipoSolicitud;
      console.log(solicitudEnStore);

      return "Solicitud añadida correctamente";
    } catch (error) {
      return error;
    }
  },
  async editarSolicitud(solicitudAEditar) {
    try {
      const reservistaEnStore = solicitudAEditar.reservista;
      const tipoSolicitud = solicitudAEditar.tipoSolicitud;
      solicitudAEditar.reservista = solicitudAEditar.reservista._links.self.href;
      delete solicitudAEditar.costeCentimos;
      const solicitudEnStore = await this.editarElemento(solicitudAEditar);
      solicitudEnStore.reservista = reservistaEnStore;
      solicitudEnStore.tipoSolicitud = tipoSolicitud;

      return "Solicitud editada correctamente";
    } catch (error) {
      return error;
    }
  },
});
