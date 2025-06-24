<script>
  import { useCostesPorDiaStore } from "@/stores/costes-por-dia";
  import { formatearCentimosAEuros } from "@/utils/utils";
  import { mapActions } from "pinia";
  import ProgressSpinner from "primevue/progressspinner";

  export default {
    name: "ListadoCostesPorDiaView",
    components: { ProgressSpinner },
    data() {
      return {
        nuevosValores: {},
        mensajeModal: "",
        mostrarModal: false,
      };
    },
    computed: {
      costes() {
        return useCostesPorDiaStore().elementos.sort(
          (a, b) => b.centimos - a.centimos
        );
      },
    },
    methods: {
      formatearCentimosAEuros,
      ...mapActions(useCostesPorDiaStore, ["editarElemento"]),
      async editarCoste(coste) {
        this.mostrarModal = true;
        this.mensajeModal = "";
        await this.editarElemento(coste);
        this.mensajeModal = "Coste actualizado correctamente.";
        this.nuevosValores[coste._links.self.href] = "";
      },
      cerrarModal() {
        this.mostrarModal = false;
        this.mensajeModal = "";
      },
      validarYEditar(coste) {
        const clave = coste._links.self.href;
        const valor = this.nuevosValores[clave];

        const patron = /^[0-9]+,[0-9]{2}$/;

        if (patron.test(valor)) {
          const centimos = parseInt(valor.replace(",", ""));
          this.editarCoste({
            ...coste,
            centimos: centimos || coste.centimos,
          });
        } else {
          this.mensajeModal =
            "Formato inválido. Formato requerido: número entero, coma, dos decimales (ej. 123,45)";
          this.mostrarModal = true;
        }
      },
    },
  };
</script>

<template>
  <div class="container pb-4">
    <h1 class="titulo py-4">Costes por día</h1>
    <table class="table-auto w-full border border-gray-300">
      <thead class="bg-gray-100">
        <tr>
          <th class="border px-4 py-2">Empleo</th>
          <th class="border px-4 py-2">Coste</th>
          <th class="border px-4 py-2">Nuevo valor</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="coste in costes" :key="coste._links.self.href">
          <td class="border px-4 py-2">{{ coste.empleo }}</td>
          <td class="border px-4 py-2">
            {{ formatearCentimosAEuros(coste.centimos) }}
          </td>
          <td class="px-2 py-2">
            <input
              v-model="nuevosValores[coste._links.self.href]"
              type="text"
              pattern="^[0-9]+,[0-9]{2}$"
              title="Formato: número entero, coma, dos decimales"
              class="form-control w-full"
              placeholder="Cantidad en euros"
            />
          </td>
          <td class="px-4 py-2">
            <button @click="validarYEditar(coste)" class="btn btn-success">
              Actualizar
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
