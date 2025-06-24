<script>
  import Cabecera from "@/components/navegacion/Cabecera.vue";
  import ProgressSpinner from "primevue/progressspinner";
  import Footer from "@/components/navegacion/Footer.vue";
  import { cargarTodaLaApi } from "@/utils/utils";

  export default {
    components: { Cabecera, ProgressSpinner, Footer },
    data() {
      return {
        cargandoFrontend: true,
      };
    },
    async mounted() {
      await cargarTodaLaApi();
      this.cargandoFrontend = false;
    },
  };
</script>

<template>
  <div class="app-container">
    <Cabecera />
    <main>
      <ProgressSpinner
        v-if="cargandoFrontend"
        style="display: block; margin: 4rem auto;"
        class="spinnerColor"
      />
      <router-view v-else />
    </main>
    <Footer />
  </div>
</template>

<style>
  .app-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  .fl {
    display: flex;
    flex-direction: row;
  }

  main {
    flex: 1;
  }

  .layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  footer {
    background-color: #f8f9fa;
    color: #333;
  }

  .colorPrincipal {
    color: #fff;
    background-color: #393621;
  }
  .colorPrincipal:hover {
    color: #7c732f;
  }
  .colorSecundario {
    background-color: #c4bd86;
  }
  .colorSecundario:hover {
    color: #979163;
  }
  .spinnerColor {
    color: #c4bd86;
  }
  .fuenteEtiqueta {
    font: 1em sans-serif;
  }
  .colorFuenteFooter {
    color: #fff;
  }

  .componente-en-lista {
    background-color: #f9faf8;
    border-radius: 0.5rem;
    padding: 1rem;
    margin-bottom: 1rem;
  }

  .otrosFuente {
    color: #000;
  }
  .fondofooter {
    background-color: #393621;
  }
  .fondo {
    background-color: #393621;
  }
  .colorAmarillo {
    background-color: #ffdd00;
  }

  .btn-info {
    background-color: #94bb71 !important;
  }

  .btn-primary {
    background-color: #3a6f45 !important;
  }
</style>
