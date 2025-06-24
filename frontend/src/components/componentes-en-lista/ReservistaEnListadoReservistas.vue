<script>
  import { useReservistasStore } from "@/stores/reservistas";
  import { formatearFecha } from "@/utils/utils";

  export default {
    props: ["reservista"],
    methods: {
      abrirElementoConsulta() {
        useReservistasStore().elementoAbierto = this.reservista; // Guarda el elemento en el store para editarlo
        useReservistasStore().consultando = true;
        this.$router.push({ path: "/formulario/reservista" });
      },
      formatearFecha,
    },
  };
</script>

<template>
  <div
    class="row justify-content-between align-items-center colorSecundario p-2 mb-2 shadow"
  >
    <div class="col-md-6 text-start">
      <div class="mb-0">
        <strong>
          {{ reservista.empleo }}
          {{ reservista.nombre }}
          {{ reservista.apellido1 }}
          {{ reservista.apellido2 }}</strong
        ><br />
        DNI: {{ reservista.dni }}<br />
        Fecha fin de compromiso:
        {{ formatearFecha(reservista.fechaFinCompromiso) }}
        Fecha caducidad reconocimiento médico:
        {{ formatearFecha(reservista.fechaCaducidadReconocimientoMedico) }}
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
        </div>
      </div>
    </div>
  </div>
</template>
