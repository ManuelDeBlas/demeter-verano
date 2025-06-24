import { API_BASE_URL } from "@/config/app";
import { defineStore } from "pinia";
import { getNombreDAO } from "@/utils/utils";
import { get, post, deleteEntidad, put } from "@/utils/api-service";

export function crearStore(nombreColeccion, accionesAdicionales = {}) {
  return defineStore(nombreColeccion, {
    state: () => ({
      elementos: [],
      elementoAbierto: null,
      consultando: false,
    }),
    actions: {
      async cargarElementos() {
        await get(API_BASE_URL + "/" + nombreColeccion)
          .then((response) => {
            if (response.data._embedded) {
              const embedded = response.data._embedded;
              this.elementos = Object.values(embedded).flat();
            }
          })
          .catch((error) => {
            error;
          });
      },
      recuperarObjetoDelStore(href) {
        const objetoRecuperado = this.elementos.filter(
          (elemento) => elemento._links.self.href === href
        )[0];
        return objetoRecuperado;
      },
      async anhadirElemento(objetoACrear) {
        // Esto evita hacer un post a '/soliticitudes'
        if (nombreColeccion === "solicitudes") {
          nombreColeccion = getNombreDAO(objetoACrear.tipoSolicitud);
        }
        try {
          const respuesta = await post(
            objetoACrear,
            API_BASE_URL + "/" + nombreColeccion
          );
          if (respuesta.status === 201) {
            const nuevoObjetoEnApi = respuesta.data;
            this.elementos.unshift(nuevoObjetoEnApi);

            return nuevoObjetoEnApi;
          }
        } catch (error) {
          return error;
        }
      },
      async eliminarElemento(objetoAEliminar) {
        try {
          const hrefAEliminar = objetoAEliminar._links.self.href;
          const respuesta = await deleteEntidad(hrefAEliminar);
          if (respuesta.status === 200) {
            const objetoEliminado = respuesta.data;
            const indice = this.elementos.findIndex(
              (elemento) =>
                elemento._links.self.href === objetoEliminado._links.self.href
            );
            if (indice !== -1) {
              this.elementos.splice(indice, 1);
            }

            return objetoEliminado;
          }
        } catch (error) {
          return error;
        }
      },
      async editarElemento(objetoEditado) {
        try {
          const respuesta = await put(
            objetoEditado,
            objetoEditado._links.self.href
          );
          if (respuesta.status === 200) {
            const objetoEditadoEnApi = respuesta.data;
            const indice = this.elementos.findIndex(
              (elemento) =>
                elemento._links.self.href ===
                objetoEditadoEnApi._links.self.href
            );
            if (indice !== -1) {
              this.elementos[indice] = objetoEditadoEnApi;
            }

            return objetoEditadoEnApi;
          }
        } catch (error) {
          return error;
        }
      },
      ...accionesAdicionales,
    },
  });
}
