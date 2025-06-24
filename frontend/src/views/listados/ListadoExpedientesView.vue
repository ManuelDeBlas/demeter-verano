<script>
  import { ESTADOS_EXPEDIENTE } from "@/constants/app";
  import ExpedienteEnListadoExpedientes from "@/components/componentes-en-lista/ExpedienteEnListadoExpedientes.vue";
  import { useExpedientesStore } from "@/stores/expedientes";

  export default {
    components: {
      ExpedienteEnListadoExpedientes,
    },
    data() {
      return {
        seleccionFiltro: "",
        ESTADOS_EXPEDIENTE: ESTADOS_EXPEDIENTE,
        paginaActual: 1,
        registrosPorPagina: 10,
      };
    },
    computed: {
      expedientes() {
        return useExpedientesStore().elementos;
      },
      expedientesFiltrados() {
        let expedientesFiltrados = this.expedientes;
        if (this.seleccionFiltro) {
          expedientesFiltrados = this.expedientes.filter(
            (elemento) => elemento.estado === this.seleccionFiltro
          );
        }
        return expedientesFiltrados;
      },
      totalPaginas() {
        return (
          Math.ceil(
            this.expedientesFiltrados.length / this.registrosPorPagina
          ) || 1
        );
      },
      inicio() {
        return (this.paginaActual - 1) * this.registrosPorPagina;
      },
      fin() {
        return Math.min(
          this.paginaActual * this.registrosPorPagina,
          this.expedientesFiltrados.length
        );
      },
      solicitudesPaginadas() {
        return this.expedientesFiltrados.slice(this.inicio, this.fin);
      },
    },
    methods: {
      anhadirElemento() {
        useExpedientesStore().elementoAbierto = null; // Vacía el store para añadir un nuevo elemento
        this.$router.push({ path: "/formulario/expediente" });
      },
      irAPagina(pagina) {
        if (pagina >= 1 && pagina <= this.totalPaginas) {
          this.paginaActual = pagina;
        }
      },
      cambiarRegistrosPorPagina() {
        this.paginaActual = 1;
      },
    },
  };
</script>

<template>
  <div class="container">
    <h1 class="titulo py-4">Lista de expedientes</h1>
    <div class="d-flex align-items-center gap-2 mb-4">
      <button type="button" class="btn btn-warning" @click="anhadirElemento">
        Nuevo Expediente
      </button>
      <label class="mb-0 fw-bold">Filtrar:</label>
      <select v-model="seleccionFiltro" class="form-select w-auto">
        <option value="">Todas</option>
        <option
          v-for="estado in ESTADOS_EXPEDIENTE"
          :key="estado"
          :value="estado"
        >
          {{ estado }}
        </option>
      </select>
    </div>
    <ul>
      <div
        v-for="expediente in expedientesFiltrados"
        :key="expediente._links.self.href"
        class="mb-3"
      >
        <expediente-en-listado-expedientes
          :expediente="expediente"
        ></expediente-en-listado-expedientes>
      </div>
    </ul>
    <div class="row">
      <div class="col-lg-12">
        <div class="card">
          <div class="card-body">
            <nav>
              <div class="d-flex justify-content-between align-items-center">
                Registros por página:
                <select
                  class="table_selector"
                  aria-label="Selector de registros por página"
                  v-model="registrosPorPagina"
                  @change="cambiarRegistrosPorPagina"
                >
                  <option disabled>Registro por página</option>
                  <option :value="5">5</option>
                  <option :value="10">10</option>
                  <option :value="20">20</option>
                  <option :value="30">30</option>
                </select>

                <div class="d-flex justify-content-end align-items-center">
                  <p class="mx-3">
                    Mostrando el intervalo {{ inicio + 1 }} - {{ fin }} de
                    {{ expedientesFiltrados.length }} registros.
                  </p>
                  <ul class="pagination justify-content-end">
                    <li
                      class="page-item dnone-d"
                      :class="{ disabled: paginaActual === 1 }"
                    >
                      <a
                        class="page-link"
                        href="#"
                        @click.prevent="irAPagina(paginaActual - 1)"
                      >
                        <span aria-hidden="true"
                          ><i class="bi bi-arrow-left me-2"></i>Anterior</span
                        >
                      </a>
                    </li>
                    <li
                      class="page-item dblock-m"
                      :class="{ disabled: paginaActual === 1 }"
                    ></li>

                    <li
                      v-for="pagina in totalPaginas"
                      :key="pagina"
                      class="page-item"
                      :class="{ active: paginaActual === pagina }"
                    >
                      <a
                        class="page-link"
                        href="#"
                        @click.prevent="irAPagina(pagina)"
                        >{{ pagina }}</a
                      >
                    </li>

                    <li
                      class="page-item dnone-d"
                      :class="{ disabled: paginaActual === totalPaginas }"
                    >
                      <a
                        class="page-link"
                        href="#"
                        @click.prevent="irAPagina(paginaActual + 1)"
                      >
                        <span aria-hidden="true"
                          >Siguiente<i class="bi bi-arrow-right gap-2"></i
                        ></span>
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
