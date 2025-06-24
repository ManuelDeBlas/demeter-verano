<script>
  import { useSolicitudesStore } from "@/stores/solicitudes";
  import { useReservistasStore } from "@/stores/reservistas";
  import { useUcosStore } from "@/stores/ucos";
  import { mapActions } from "pinia";
  import {
    formatearAtributoEnElFrontend,
    formatearCentimosAEuros,
  } from "@/utils/utils";
  import ProgressSpinner from "primevue/progressspinner";

  export default {
    name: "FormularioSolicitudView",
    components: { ProgressSpinner },
    data() {
      return {
        editando: true,
        consultando: useSolicitudesStore().consultando,
        mostrarModal: false,
        mostrarModalReservistas: false,
        mostrarModalUcos: false,
        busquedaUco: "",
        busquedaReservista: "",
        mostrarModalConfirmacion: false,
        mensajeModal: "",
        uco: null,
        reservista: null,
        inicio: null,
        fin: null,
      };
    },
    computed: {
      solicitudAbierta: {
        get() {
          if (!useSolicitudesStore().elementoAbierto) {
            this.editando = false;
            useSolicitudesStore().elementoAbierto = {
              nombreUco: "",
              ciu: "",
              telefonoPoc: "",
              emailPoc: "",
              fechaInicio: "",
              fechaFin: "",
              tipoSolicitud: "",
              pagaSecres: false,
              estado: "PENDIENTE_EVALUACION",
              reservista: null,
            };
          }
          return useSolicitudesStore().elementoAbierto;
        },
        set(value) {
          useSolicitudesStore().elementoAbierto = value;
        },
      },
      reservistasFiltrados() {
        const dniBuscado = this.busquedaReservista.toLowerCase();
        return useReservistasStore().elementos.filter((r) =>
          r.dni.toLowerCase().includes(dniBuscado)
        );
      },
      ucosFiltradas() {
        const ucoBuscada = this.busquedaUco.toLowerCase();
        return useUcosStore().ucos.filter(
          (uco) =>
            uco.nombreUco.toLowerCase().includes(ucoBuscada) ||
            uco.ciu.toLowerCase().includes(ucoBuscada)
        );
      },
      reservistaFormateado() {
        return this.solicitudAbierta.reservista
          ? `${this.solicitudAbierta.reservista.dni} ${this.solicitudAbierta.reservista.empleo} ${this.solicitudAbierta.reservista.nombre} ${this.solicitudAbierta.reservista.apellido1} ${this.solicitudAbierta.reservista.apellido2}`
          : "";
      },
      estadoFormateado() {
        return formatearAtributoEnElFrontend(this.solicitudAbierta.estado);
      },
      costeEuros() {
        if (!this.solicitudAbierta.costeCentimos) {
          return "El coste se calcula automáticamente al generar la solicitud";
        } else {
          return formatearCentimosAEuros(this.solicitudAbierta.costeCentimos);
        }
      },
    },
    methods: {
      formatearAtributoEnElFrontend,
      formatearCentimosAEuros,
      ...mapActions(useSolicitudesStore, [
        "editarSolicitud",
        "eliminarElemento",
        "anhadirSolicitud",
      ]),
      seleccionarUco(uco) {
        this.solicitudAbierta.nombreUco = uco.nombreUco;
        this.solicitudAbierta.ciu = uco.ciu;
        this.mostrarModalUcos = false;
      },
      seleccionarReservista(reservista) {
        this.solicitudAbierta.reservista = reservista;
        this.mostrarModalReservistas = false;
      },
      volverAlListado() {
        useSolicitudesStore().elementoAbierto = null;
        useSolicitudesStore().consultando = false;
        this.$router.push({ path: "/listado/solicitudes" });
      },
      async enviarFormulario() {
        try {
          this.mostrarModal = true;
          this.mensajeModal = "";
          this.inicio = new Date(this.solicitudAbierta.fechaInicio);
          this.fin = new Date(this.solicitudAbierta.fechaFin);
          if (this.inicio.getFullYear() !== this.fin.getFullYear()) {
            this.mostrarModalConfirmacion = true;
            return;
          }
          if (this.editando) {
            const respuesta = await this.editarSolicitud(this.solicitudAbierta);
            this.mensajeModal = respuesta;
          } else {
            const respuesta = await this.anhadirSolicitud(
              this.solicitudAbierta
            );
            this.mensajeModal = respuesta;
          }
        } catch (error) {
          this.mensajeModal = `Error al procesar la solicitud: ${error.message}`;
        } finally {
          this.envioPendiente = false;
        }
      },
      async rechazarSolicitud() {
        this.mostrarModal = true;
        this.mensajeModal = "";
        try {
          this.solicitudAbierta.estado = "RECHAZADA";
          await this.editarSolicitud(this.solicitudAbierta);
          this.mensajeModal = `Solicitud rechazada correctamente.`;
        } catch (error) {
          this.mensajeModal = `Error al rechazar la solicitud: ${error.message}`;
        }
      },
      async enviarDosSolicitudes() {
        this.mostrarModalConfirmacion = false;

        try {
          const finPrimerTramo = new Date(this.inicio.getFullYear(), 11, 32);
          const inicioSegundoTramo = new Date(this.fin.getFullYear(), 0, 2);

          const solicitud1 = { ...this.solicitudAbierta };
          const solicitud2 = { ...this.solicitudAbierta };

          solicitud1.fechaInicio = this.inicio.toISOString().split("T")[0];
          solicitud1.fechaFin = finPrimerTramo.toISOString().split("T")[0];

          solicitud2.fechaInicio = inicioSegundoTramo
            .toISOString()
            .split("T")[0];
          solicitud2.fechaFin = this.fin.toISOString().split("T")[0];

          if (this.editando) {
            await this.editarSolicitud(solicitud1);
            await this.anhadirSolicitud(solicitud2); // Se usa post para la segunda solicitud ya que no existe en la API aún
            this.mensajeModal = "Solicitudes editadas correctamente.";
          } else {
            await this.anhadirSolicitud(solicitud1);
            await this.anhadirSolicitud(solicitud2);
            this.mensajeModal = "Solicitudes añadidas correctamente.";
          }
        } catch (error) {
          this.mensajeModal = `Error al procesar las solicitudes: ${error.message}`;
        } finally {
          this.mostrarModal = true;
          this.envioPendiente = false;
        }
      },
      cancelarEnvio() {
        this.mostrarModalConfirmacion = false;
        this.envioPendiente = false;
      },
      async eliminarSolicitud() {
        try {
          await this.eliminarElemento(this.solicitudAbierta);
          this.mensajeModal = `Solicitud eliminada correctamente.`;
        } catch (error) {
          this.mensajeModal = `Error al eliminar la solicitud: ${error.message}`;
        } finally {
          this.mostrarModal = true;
        }
      },
      cerrarModal() {
        this.mostrarModal = false;
        useSolicitudesStore().elementoAbierto = null;
        useSolicitudesStore().consultando = false;
        this.$router.push({ path: "/listado/solicitudes" });
      },
    },
    watch: {
      "solicitudAbierta.tipoSolicitud"(nuevoValor) {
        if (nuevoValor === "PS") {
          this.solicitudAbierta.pagaSecres = true;
        }
        if (nuevoValor === "FC") {
          this.solicitudAbierta.pagaSecres = false;
        }
      },
    },
  };
</script>

<template>
  <div class="container mt-3">
    <h2 v-if="!editando">Añadir solicitud</h2>
    <h2 v-if="editando && consultando">Consultar solicitud</h2>
    <h2 v-if="editando && !consultando">Modificar solicitud</h2>
    <form @submit.prevent="enviarFormulario" class="row g-3 mt-2">
      <div class="row mt-3">
        <div class="col-md-6">
          <label class="form-label">Nombre UCO</label>
          <input
            disabled
            v-model="solicitudAbierta.nombreUco"
            type="text"
            class="form-control"
            required
          />
        </div>
        <div class="col-md-6">
          <label class="form-label">CIU</label>
          <input
            disabled
            v-model="solicitudAbierta.ciu"
            type="text"
            class="form-control"
            required
          />
        </div>
      </div>
      <div class="col-md-2 d-flex align-items-end">
        <button
          v-if="!consultando && !editando"
          type="button"
          class="btn btn-primary w-100"
          @click="mostrarModalUcos = true"
        >
          Seleccionar UCO
        </button>
      </div>

      <div class="row mt-3">
        <div class="col-md-12">
          <label class="form-label">Reservista</label>
          <input
            disabled
            v-model="reservistaFormateado"
            type="text"
            class="form-control"
            required
          />
        </div>
      </div>
      <div class="col-md-3 d-flex align-items-end">
        <button
          v-if="!consultando && !editando"
          type="button"
          class="btn btn-primary w-80"
          @click="mostrarModalReservistas = true"
        >
          Seleccionar Reservista
        </button>
      </div>

      <div v-if="editando" class="row mt-3">
        <div class="col-10">
          <label class="form-label">Estado</label>
          <input
            class="form-control"
            disabled
            readonly
            v-model="estadoFormateado"
          />
        </div>
        <div
          class="col-2 d-flex align-items-end"
          v-if="
            !consultando && solicitudAbierta.estado === 'PENDIENTE_EVALUACION'
          "
        >
          <button
            type="button"
            @click="rechazarSolicitud"
            class="btn btn-danger w-100"
          >
            Rechazar solicitud
          </button>
        </div>
      </div>

      <div class="row mt-3">
        <div class="col-md-6">
          <label>Fecha Inicio</label>
          <input
            :disabled="consultando"
            v-model="solicitudAbierta.fechaInicio"
            type="date"
            class="form-control mx-auto"
            required
          />
        </div>
        <div class="col-md-6">
          <label>Fecha Fin</label>
          <input
            :disabled="consultando"
            v-model="solicitudAbierta.fechaFin"
            type="date"
            class="form-control mx-auto"
            required
          />
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-md-6">
          <label>Teléfono POC</label>
          <input
            :disabled="consultando"
            v-model="solicitudAbierta.telefonoPoc"
            type="text"
            class="form-control mx-auto"
            pattern="^\+?[0-9]{7,15}$"
            title="Formato: Número de teléfono con entre 7 y 15 dígitos, opcionalmente con + al inicio"
            required
          />
        </div>
        <div class="col-md-6">
          <label>Email POC</label>
          <input
            :disabled="consultando"
            v-model="solicitudAbierta.emailPoc"
            type="email"
            class="form-control mx-auto"
            required
            placeholder="Dirección de email a la cual se enviarán todas las actualizaciones
            de la solicitud"
          />
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-md-6">
          <label>Tipo de Solicitud</label>
          <select
            :disabled="consultando"
            v-model="solicitudAbierta.tipoSolicitud"
            class="form-select mx-auto mt-3"
            required
          >
            <option value="FC">Formación continuada</option>
            <option value="EX">Activación ampliada</option>
            <option value="PS">Prestación servicios unidad</option>
          </select>
        </div>
        <div class="col-md-6">
          <input
            :disabled="
              solicitudAbierta.tipoSolicitud === 'PS' ||
              solicitudAbierta.tipoSolicitud === 'FC' ||
              consultando
            "
            v-model="solicitudAbierta.pagaSecres"
            type="checkbox"
            class="form-check-input"
            id="pagaSecresCheck"
          />
          <label class="form-check-label ms-2" for="pagaSecresCheck">
            Solicitud a cargo del crédito de la SECRES </label
          ><br />
          <small class="form-text text-muted">
            Las prestaciones de servicio en unidad son siempre a cargo del
            crédito de la SECRES. Las formaciones continuadas son a cargo del
            crédito de la unidad solicitante
          </small>
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-md-6">
          <label>Coste</label>
          <option disabled class="form-control mx-auto">
            {{ costeEuros }}
          </option>
        </div>
        <div v-if="solicitudAbierta.tipoSolicitud === 'FC'" class="col-md-6">
          <label>Escala</label>
          <select
            :disabled="consultando"
            v-model.number="solicitudAbierta.escala"
            class="form-control mx-auto"
            :required="solicitudAbierta.tipoSolicitud === 'FC'"
          >
            <option value="Oficiales">Oficiales</option>
            <option value="Suboficiales">Suboficiales</option>
            <option value="Tropa">Tropa</option>
          </select>
        </div>
      </div>
      <div v-if="solicitudAbierta.tipoSolicitud === 'EX'" class="form-group">
        <label class="form-label">Motivo</label>
        <textarea
          :disabled="consultando"
          v-model="solicitudAbierta.motivo"
          class="form-control mx-auto"
          :required="solicitudAbierta.tipoSolicitud === 'EX'"
        ></textarea>
      </div>
      <div class="d-flex justify-content-end mb-3 mt-3">
        <div v-if="!editando">
          <button
            type="button"
            class="btn btn-light mx-3"
            @click="volverAlListado"
          >
            Cancelar
          </button>
          <button type="submit" class="btn btn-success">Añadir</button>
        </div>
        <div v-if="editando && consultando">
          <button
            type="button"
            class="btn btn-success"
            @click="volverAlListado"
          >
            Volver al listado
          </button>
        </div>
        <div v-if="editando && !consultando">
          <button
            type="submit"
            class="btn btn-light mx-3"
            @click="volverAlListado"
          >
            Cancelar
          </button>
          <button
            type="button"
            @click="eliminarSolicitud"
            class="btn btn-danger ms-2"
          >
            Eliminar
          </button>
          <button type="submit" class="btn btn-success ms-2 mx-3">
            Guardar
          </button>
        </div>
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

    <div
      v-if="mostrarModalReservistas"
      class="modal fade show"
      tabindex="-1"
      style="display: block; background-color: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Seleccionar Reservista</h5>
            <button
              type="button"
              class="btn-close"
              @click="mostrarModalReservistas = false"
            ></button>
          </div>
          <div class="modal-body">
            <input
              type="text"
              class="form-control mb-3"
              placeholder="Buscar por DNI..."
              v-model="busquedaReservista"
            />
            <ul class="list-group">
              <li
                v-for="reservista in reservistasFiltrados"
                :key="reservista.id"
                class="list-group-item"
                @click="seleccionarReservista(reservista)"
              >
                {{ reservista.dni }}&nbsp; {{ reservista.empleo }}&nbsp;
                {{ reservista.apellido1 }}&nbsp;
                {{ reservista.apellido2 }},&nbsp;
                {{ reservista.nombre }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="mostrarModalUcos"
      class="modal fade show"
      tabindex="-1"
      style="display: block; background-color: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Seleccionar UCO</h5>
            <button
              type="button"
              class="btn-close"
              @click="mostrarModalUcos = false"
            ></button>
          </div>
          <div class="modal-body">
            <input
              type="text"
              class="form-control mb-3"
              placeholder="Introduzca el CIU de la UCO"
              v-model="busquedaUco"
            />
            <ul class="list-group">
              <li
                v-for="uco in ucosFiltradas"
                :key="uco.id"
                class="list-group-item"
                @click="seleccionarUco(uco)"
              >
                {{ uco.nombreUco }}&nbsp; (CIU: {{ uco.ciu }})
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div
      v-if="mostrarModalConfirmacion"
      class="modal fade show"
      tabindex="-1"
      style="display: block; background-color: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Confirmar envío</h5>
            <button
              type="button"
              class="btn-close"
              @click="cancelarEnvio"
            ></button>
          </div>
          <div class="modal-body">
            <p>
              Las fechas de inicio y fin no están en el mismo año natural. Si
              desea continuar, se crearán dos solicitudes independientes
              transcurriendo cada una durante su propio año natural. ¿Desea
              continuar?
            </p>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              @click="cancelarEnvio"
            >
              Cancelar
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="enviarDosSolicitudes"
            >
              Continuar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
