<script>
  import { usePresupuestosSecresStore } from "@/stores/presupuestos-secres";
  import { formatearCentimosAEuros } from "@/utils/utils";
  import { mapActions } from "pinia";
  import ProgressSpinner from "primevue/progressspinner";

  export default {
    name: "ListadoPresupuestosSecresView",
    components: { ProgressSpinner },
    data() {
      return {
        nuevosValores: {},
        anho: "",
        cantidadCentimosConcedido: "",
        mensajeModal: "",
        mostrarModal: false,
      };
    },
    computed: {
      presupuestos() {
        return usePresupuestosSecresStore().elementos;
      },
    },
    methods: {
      formatearCentimosAEuros,
      ...mapActions(usePresupuestosSecresStore, [
        "anhadirElemento",
        "editarElemento",
      ]),

      async anhadirNuevoPresupuesto(presupuesto) {
        const patron = /^[0-9]+,[0-9]{2}$/;
        if (!patron.test(this.cantidadCentimosConcedido)) {
          this.mensajeModal =
            "Formato inválido. Formato requerido: número entero, coma, dos decimales (ej. 123,45)";
          this.mostrarModal = true;
          return;
        }

        this.mostrarModal = true;
        this.mensajeModal = "";
        presupuesto.cantidadCentimosConcedido = parseInt(
          this.cantidadCentimosConcedido.replace(",", "")
        );
        await this.anhadirElemento(presupuesto);
        this.anho = "";
        this.cantidadCentimosConcedido = "";
        this.mensajeModal = "Presupuesto añadido correctamente.";
      },

      async editarPresupuesto(presupuesto) {
        const clave = presupuesto._links.self.href;
        const nuevoValor = this.nuevosValores[clave];
        const patron = /^[0-9]+,[0-9]{2}$/;

        if (!patron.test(nuevoValor)) {
          this.mensajeModal =
            "Formato inválido. Formato requerido: número entero, coma, dos decimales (ej. 123,45)";
          this.mostrarModal = true;
          return;
        }

        this.mostrarModal = true;
        this.mensajeModal = "";
        const cantidadCentimosConcedido =
          parseInt(nuevoValor.replace(",", "")) || presupuesto.cantidadCentimosConcedido;
        await this.editarElemento({
          ...presupuesto,
          cantidadCentimosConcedido,
        });
        this.mensajeModal = "Presupuesto actualizado correctamente.";
        this.nuevosValores[clave] = "";
      },

      cerrarModal() {
        this.mostrarModal = false;
        this.mensajeModal = "";
      },
    },
  };
</script>

<template>
  <div class="container">
    <h1 class="titulo py-4">Presupuestos SECRES</h1>

    <table class="table-auto w-full border border-gray-300 mb-4">
      <thead class="bg-gray-100">
        <tr>
          <th class="border px-4 py-2">Año</th>
          <th class="border px-4 py-2">Cantidad concedida</th>
          <th class="border px-4 py-2">Nuevo valor</th>
          <th class="border px-4 py-2">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="presupuesto in presupuestos"
          :key="presupuesto._links.self.href"
        >
          <td class="border px-4 py-2">{{ presupuesto.anho }}</td>
          <td class="border px-4 py-2">
            {{ formatearCentimosAEuros(presupuesto.cantidadCentimosConcedido) }}
          </td>
          <td class="px-2 py-2">
            <input
              v-model="nuevosValores[presupuesto._links.self.href]"
              type="text"
              pattern="^[0-9]+,[0-9]{2}$"
              title="Formato: número entero, coma, dos decimales"
              class="form-control w-full"
              placeholder="Cantidad en euros"
            />
          </td>
          <td class="px-4 py-2">
            <button
              @click="
                editarPresupuesto({
                  ...presupuesto,
                  cantidadCentimosConcedido:
                    parseInt(
                      nuevosValores[presupuesto._links.self.href].replace(
                        ',',
                        ''
                      )
                    ) || presupuesto.cantidadCentimosConcedido,
                })
              "
              class="btn btn-success"
            >
              Actualizar
            </button>
          </td>
        </tr>
        <!-- Añadir nuevo presupuesto -->
        <tr class="">
          <td class="px-2 py-2">
            <input
              v-model="anho"
              type="text"
              class="form-control w-full"
              placeholder="Nuevo año"
              required
            />
          </td>
          <td class="px-2 py-2">
            <input
              v-model="cantidadCentimosConcedido"
              type="text"
              pattern="^[0-9]+,[0-9]{2}$"
              title="Formato: número entero, coma, dos decimales"
              class="form-control w-full"
              placeholder="Cantidad en euros"
            />
          </td>
          <td></td>
          <td class="px-4 py-2">
            <button
              @click="
                anhadirNuevoPresupuesto({
                  anho: anho,
                  cantidadCentimosConcedido: parseInt(cantidadCentimosConcedido.replace(',', '')),
                })
              "
              class="btn btn-primary"
            >
              Añadir
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- Modal -->
  <div
    v-if="mostrarModal"
    class="modal fade show"
    tabindex="-1"
    style="display: block; background-color: rgba(0, 0, 0, 0.5)"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Información</h5>
          <button type="button" class="btn-close" @click="cerrarModal"></button>
        </div>
        <ProgressSpinner v-if="this.mensajeModal === ''" />
        <div class="modal-body">
          <p>{{ mensajeModal }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="cerrarModal">
            Cerrar
          </button>
        </div>
      </div>
    </div>
  </div>
  <!-- Fin Modal -->
</template>
