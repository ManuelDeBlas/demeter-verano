<script>
  import { useExpedientesStore } from "@/stores/expedientes";
  import {
    formatearAtributoEnElFrontend,
    formatearCentimosAEuros,
  } from "@/utils/utils";

  export default {
    props: ["expediente"],
    methods: {
      formatearAtributoEnElFrontend,
      formatearCentimosAEuros,
      abrirElementoEditar() {
        useExpedientesStore().elementoAbierto = this.expediente; // Guarda el elemento en el store para editarlo
        useExpedientesStore().consultando = false;
        this.$router.push({
          name: "FormularioExpedienteView",
        });
      },
      abrirElementoConsulta() {
        useExpedientesStore().elementoAbierto = this.expediente; // Guarda el elemento en el store para consultarlo
        useExpedientesStore().consultando = true;
        this.$router.push({
          name: "FormularioExpedienteView",
        });
      },
    },
  };
</script>

<template>
  <div
    class="row justify-content-between align-items-center colorSecundario p-2 mb-2 shadow"
  >
    <div class="col-md-6 text-start">
      <div class="mb-0">
        {{ expediente.numeroExpediente }}
        <strong>{{ formatearAtributoEnElFrontend(expediente.estado) }}</strong>
        Coste: {{ formatearCentimosAEuros(expediente.coste) }}
      </div>
    </div>
    <div class="col-md-6 text-end">
      <div class="d-flex justify-content-end align-items-center gap-3">
        <div class="d-flex align-items-center">
          <button
            type="button"
            class="btn btn-info"
            @click="abrirElementoConsulta"
          >
            Consultar
          </button>
          <button
            type="button"
            class="btn btn-primary ms-2"
            @click="abrirElementoEditar"
          >
            Modificar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
