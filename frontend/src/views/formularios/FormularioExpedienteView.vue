<script>
  import { ESTADOS_EXPEDIENTE } from "@/constants/app";
  import { formatearAtributoEnElFrontend } from "@/utils/utils";
  import SolicitudEnFormularioExpediente from "@/components/componentes-en-formulario/SolicitudEnFormularioExpediente.vue";
  import { useExpedientesStore } from "@/stores/expedientes";
  import { useSolicitudesStore } from "@/stores/solicitudes";
  import { mapState, mapActions } from "pinia";
  import ProgressSpinner from "primevue/progressspinner";

  export default {
    name: "FormularioExpedienteView",
    components: { SolicitudEnFormularioExpediente, ProgressSpinner },
    data() {
      return {
        editando: true,
        consultando: useExpedientesStore().consultando,
        mensajeModal: "",
        mostrarModal: false,
        seleccionSolicitud: "",
        ESTADOS_EXPEDIENTE: ESTADOS_EXPEDIENTE,
      };
    },
    computed: {
      expedienteAbierto() {
        let expediente = useExpedientesStore().elementoAbierto;
        if (expediente === null) {
          expediente = {
            numeroExpediente: "",
            estado: "ABIERTO",
            solicitudes: [],
          };
          this.editando = false;
        }
        return expediente;
      },
      solicitudesDisponibles() {
        return useSolicitudesStore().elementos.filter(
          (s) =>
            !(this.expedienteAbierto.solicitudes || []).some(
              (es) => es._links.self.href === s._links.self.href
            ) &&
            s.estado === "PENDIENTE_EVALUACION" &&
            s.tipoSolicitud === this.expedienteAbierto.tipoSolicitud &&
            new Date(s.fechaInicio).getFullYear() ===
              this.expedienteAbierto.anho
        );
      },
      ...mapState(useSolicitudesStore, { solicitudes: "elementos" }),
    },
    methods: {
      formatearAtributoEnElFrontend,
      ...mapActions(useExpedientesStore, [
        "anhadirExpediente",
        "editarExpediente",
        "eliminarElemento",
        "agregarSolicitudAExpediente",
        "eliminarSolicitudDeExpediente",
      ]),
      async enviarFormulario() {
        this.mostrarModal = true;
        this.mensajeModal = "";
        if (this.editando) {
          this.mensajeModal = await this.editarExpediente(
            this.expedienteAbierto
          );
        } else {
          this.mensajeModal = await this.anhadirExpediente(
            this.expedienteAbierto
          );
        }
      },
      async agregarSolicitud(solicitud) {
        this.mostrarModal = true;
        this.mensajeModal = "";
        this.mensajeModal = await this.agregarSolicitudAExpediente(
          solicitud,
          this.expedienteAbierto
        );
      },
      async eliminarSolicitud(solicitud) {
        this.mensajeModal = await this.eliminarSolicitudDeExpediente(
          solicitud,
          this.expedienteAbierto
        );
        this.mostrarModal = true;
      },
      eliminarExpediente() {
        this.eliminarElemento(this.expedienteAbierto);
        this.$router.push({ path: "/listado/expedientes" });
      },
      volverAlListado() {
        this.$router.push({ path: "/listado/expedientes" });
      },
      cerrarModal() {
        this.mostrarModal = false;
        useExpedientesStore().elementoAbierto = null;
        useExpedientesStore().consultando = false;
        this.$router.push({ path: "/listado/expedientes" });
      },
    },
  };
</script>

<template>
  <div class="container mt-3">
    <h2 v-if="!editando">Añadir expediente</h2>
    <h2 v-if="editando && consultando">Consultar expediente</h2>
    <h2 v-if="editando && !consultando">Modificar expediente</h2>
    <form @submit.prevent="enviarFormulario" class="row g-3 mt-2">
      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Número expediente</label>
          <input
            v-model="expedienteAbierto.numeroExpediente"
            type="text"
            class="form-control"
            :disabled="editando || consultando"
            pattern="^T64(PS|EX|FC)A(ENE|FEB|MAR|ABR|MAY|JUN|JUL|AGO|SEP|OCT|NOV|DIC)([0-9]{2})[1-9][0-9]*$"
            title="Formato: T64 seguido de PS, EX o FC, luego A, el mes (ej. ENE), el año (25) y el número de expediente que corresponda (ej. 1). Ejemplo completo: T64PSAENE251"
            required
          />
        </div>
        <div class="col-md-6">
          <label class="form-label">Estado</label>
          <select
            v-model="expedienteAbierto.estado"
            class="form-select"
            :disabled="!editando || consultando"
          >
            <option
              v-for="estado in ESTADOS_EXPEDIENTE"
              :key="estado"
              :value="estado"
            >
              {{ formatearAtributoEnElFrontend(estado) }}
            </option>
          </select>
        </div>
      </div>

      <div class="row align-items-end mt-3">
        <div class="col-md-10" v-if="editando && !consultando">
          <label class="form-label">Añadir solicitud</label>
          <select v-model="seleccionSolicitud" class="form-select">
            <option value="">Seleccionar</option>
            <option
              v-for="solicitud in solicitudesDisponibles"
              :key="solicitud._links.self.href"
              :value="solicitud"
            >
              DNI: {{ solicitud.reservista.dni }} , Inicio:
              {{ solicitud.fechaInicio }} , Fin: {{ solicitud.fechaFin }}
              {{ solicitud.nombreUco }}
            </option>
          </select>
        </div>
        <button
          type="button"
          class="btn btn-primary col-md-2"
          :disabled="!seleccionSolicitud"
          @click="agregarSolicitud(seleccionSolicitud)"
        >
          Añadir solicitud
        </button>
      </div>

      <div class="row mt-3">
        <label class="form-label">Solicitudes</label>
        <small v-if="!editando && !consultando"
          ><br />&nbsp;Se podrán asignar solicitudes una vez se haya añadido el
          expediente al sistema</small
        >

        <ul class="list-unstyled">
          <li
            v-for="solicitud in expedienteAbierto.solicitudes"
            :key="solicitud._links.self.href"
          >
            <solicitud-en-formulario-expediente
              :solicitud="solicitud"
              @eliminar="eliminarSolicitud(solicitud)"
            />
          </li>
        </ul>
      </div>

      <div class="d-flex justify-content-end gap-2 mt-4 mb-3">
        <button
          v-if="!editando"
          type="button"
          class="btn btn-light"
          @click="volverAlListado"
        >
          Cancelar cambios
        </button>
        <button v-if="!editando" type="submit" class="btn btn-success">
          Añadir
        </button>
        <button
          v-if="editando && consultando"
          type="button"
          class="btn btn-success"
          @click="volverAlListado"
        >
          Volver al listado
        </button>
        <template v-if="editando && !consultando">
          <button type="button" class="btn btn-light" @click="volverAlListado">
            Cancelar cambios
          </button>
          <button
            type="button"
            @click="eliminarExpediente"
            class="btn btn-danger ms-2"
          >
            Eliminar expediente
          </button>
          <button type="submit" class="btn btn-success ms-2">
            Guardar cambios
          </button>
        </template>
      </div>
    </form>

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
            <button
              type="button"
              class="btn-close"
              @click="cerrarModal"
            ></button>
          </div>
          <ProgressSpinner v-if="this.mensajeModal === ''" />
          <div class="modal-body">
            <p>{{ mensajeModal }}</p>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              @click="cerrarModal"
            >
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </div>
    <!-- Fin Modal -->
  </div>
</template>
