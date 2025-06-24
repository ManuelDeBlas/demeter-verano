import { crearStore } from "@/stores/fabricaStore";

export const useReservistasStore = crearStore(
  "reservistas", {
    crearListadoSolicitudes(reservista) {
      reservista.solicitudes = [];
    }
  }
);