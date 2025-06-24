<script>
  import { useReservistasStore } from "@/stores/reservistas";
  import { mapActions } from "pinia";

  export default {
    name: "FormularioReservistaView",
    data() {
      return {
        editando: true,
        mostrarModal: false,
        mensajeModal: "",
      };
    },
    computed: {
      reservistaAbierto: {
        get() {
          if (!useReservistasStore().elementoAbierto) {
            this.editando = false;
            useReservistasStore().elementoAbierto = {
              nombre: "",
              apellido1: "",
              apellido2: "",
              empleo: "",
              dni: "",
              telefonoParticular: "",
              fechaFinCompromiso: "",
              diasConsumidos: 0,
              localidadResidencia: "",
              subdelegacionDefensa: "",
            };
          }
          return useReservistasStore().elementoAbierto;
        },
        set(value) {
          useReservistasStore().elementoAbierto = value;
        },
      },
      diasConsumidos() {
        return this.reservistaAbierto.diasConsumidos || 0;
      },
    },
    methods: {
      ...mapActions(useReservistasStore, [
        "anhadirElemento",
        "editarElemento",
        "eliminarElemento",
      ]),
      async enviarFormulario() {
        try {
          if (this.editando) {
            let respuesta = await this.editarElemento(this.reservistaAbierto);
            this.mensajeModal = `Reservista editado correctamente`;
          } else {
            this.reservistaAbierto;
            let respuesta = await this.anhadirElemento(this.reservistaAbierto);
            this.mensajeModal = `Reservista añadido correctamente`;
          }
        } catch (error) {
          this.mensajeModal = `Error al procesar la solicitud: ${error.message}`;
        } finally {
          this.mostrarModal = true;
        }
      },
      volverAlListado() {
        this.mostrarModal = false;
        this.$router.push({ path: "/listado/reservistas" });
      },
    },
  };
</script>

<template>
  <div class="container mt-3">
    <h2>Consultar reservista</h2>
    <form @submit.prevent="enviarFormulario" class="row g-3 mt-2">
      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Empleo</label>
          <input
            disabled
            v-model="reservistaAbierto.empleo"
            type="text"
            class="form-control"
            required
          />
        </div>
        <div class="col-md-6">
          <label class="form-label">Nombre</label>
          <input
            disabled
            v-model="reservistaAbierto.nombre"
            type="text"
            class="form-control"
            required
          />
        </div>
      </div>

      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Primer apellido</label>
          <input
            disabled
            v-model="reservistaAbierto.apellido1"
            type="text"
            class="form-control"
            required
          />
        </div>
        <div class="col-md-6">
          <label class="form-label">Segundo apellido</label>
          <input
            disabled
            v-model="reservistaAbierto.apellido2"
            type="text"
            class="form-control"
            required
          />
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">DNI</label>
          <input
            disabled
            v-model="reservistaAbierto.dni"
            type="text"
            class="form-control"
            required
          />
        </div>
        <div class="col-md-6">
          <label class="form-label">Teléfono particular</label>
          <input
            disabled
            v-model="reservistaAbierto.telefonoParticular"
            type="text"
            class="form-control"
          />
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Fecha fin compromiso</label>
          <input
            disabled
            v-model="reservistaAbierto.fechaFinCompromiso"
            type="date"
            class="form-control"
          />
        </div>

        <div class="col-md-6">
          <label class="form-label"
            >Fecha caducidad reconocimiento médico</label
          >
          <input
            disabled
            v-model="reservistaAbierto.fechaCaducidadReconocimientoMedico"
            type="date"
            class="form-control"
          />
        </div>
      </div>

      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Localidad de residencia</label>
          <input
            disabled
            v-model="reservistaAbierto.localidadResidencia"
            type="text"
            class="form-control"
          />
        </div>
        <div class="col-md-6">
          <label class="form-label">Subdelegación de Defensa</label>
          <input
            disabled
            v-model="reservistaAbierto.subdelegacionDefensa"
            type="text"
            class="form-control"
          />
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Días consumidos</label>
          <input
            disabled
            v-model.number="diasConsumidos"
            type="number"
            class="form-control"
          />
        </div>
        <div class="d-flex justify-content-end gap-2 mb-3">
          <button
            type="button"
            class="btn btn-success"
            @click="volverAlListado"
          >
            Volver al listado
          </button>
        </div>
      </div>
    </form>
  </div>
</template>
