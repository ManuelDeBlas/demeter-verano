<script>
  import { useSolicitudesStore } from "@/stores/solicitudes";
  import {
    formatearAtributoEnElFrontend,
    formatearFecha,
    formatearCentimosAEuros,
  } from "@/utils/utils";

  export default {
    props: ["solicitud"],
    methods: {
      formatearAtributoEnElFrontend,
      formatearFecha,
      formatearCentimosAEuros,
      abrirElementoConsulta() {
        useSolicitudesStore().consultando = true;
        useSolicitudesStore().elementoAbierto = this.solicitud; // Guarda el elemento en el store para consultarlo
        this.$router.push({ name: "FormularioSolicitudView" });
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
        {{ solicitud.nombreUco }}
        <strong>
          {{ solicitud.reservista.empleo }}
          {{ solicitud.reservista.nombre }}
          {{ solicitud.reservista.apellido1 }}
          {{ solicitud.reservista.apellido2 }}</strong
        >
        {{ solicitud.reservista.dni }} Duración:
        {{ formatearFecha(solicitud.fechaInicio) }} -
        {{ formatearFecha(solicitud.fechaFin) }} Coste:
        {{ formatearCentimosAEuros(solicitud.costeCentimos) }}
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
              class="btn btn-danger ms-2"
              @click="$emit('eliminar', solicitud)"
            >
              Eliminar solicitud
            </button>
        </div>
      </div>
    </div>
  </div>
</template>
