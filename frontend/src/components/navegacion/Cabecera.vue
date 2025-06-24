<script>
  export default {
    name: "Cabecera",
    data() {
      return {
        isMenuLateralAbierto: false,
      };
    },
    methods: {
      menuLateral() {
        this.isMenuLateralAbierto = !this.isMenuLateralAbierto;
      },
      cerrarMenu() {
        this.isMenuLateralAbierto = false;
      },
    },
  };
</script>

<template>
  <nav class="navbar navbar-light colorPrincipal">
    <div class="d-flex align-items-center">
      <div class="d-flex align-items-center ms-2 colorAmarillo logo-ministerio">
        <button class="btn me-2" @click="menuLateral">
          <span class="navbar-toggler-icon"></span>
        </button>
        <img
          src="../../assets/logo-minisdef.jpeg"
          alt="Logo-minisdef"
          class="logo-img me-2"
        />
      </div>
      <div class="d-flex align-items-center ms-2">
        <img
          src="../../assets/logo_tierra_color.svg"
          alt="Logo-demeter"
          class="logo-img me-2"
        />
        <router-link class="nav-link" to="/inicio">Demeter</router-link>
      </div>
    </div>
  </nav>

  <!-- Menú desplegable -->
  <div
    class="offcanvas offcanvas-custom"
    :class="{ show: isMenuLateralAbierto }"
    tabindex="-1"
  >
    <div class="offcanvas-header">
      <h5 class="offcanvas-title">Menú</h5>
      <button type="button" class="btn-close" @click="menuLateral"></button>
    </div>
    <ul class="list-unstyled offcanvas-body">
      <li>
        <router-link
          class="nav-link"
          to="/listado/solicitudes"
          @click="cerrarMenu"
        >
          Solicitudes
        </router-link>
      </li>
      <li>
        <router-link
          class="nav-link"
          to="/listado/expedientes"
          @click="cerrarMenu"
        >
          Expedientes
        </router-link>
      </li>
      <li>
        <router-link
          class="nav-link"
          to="/listado/reservistas"
          @click="cerrarMenu"
        >
          Reservistas
        </router-link>
      </li>
      <li>
        <router-link
          class="nav-link"
          to="/listado/presupuestos-secres"
          @click="cerrarMenu"
        >
          Presupuestos SECRES
        </router-link>
      </li>
      <li>
        <router-link
          class="nav-link"
          to="/listado/costes-por-dia"
          @click="cerrarMenu"
        >
          Costes por día
        </router-link>
      </li>
    </ul>
  </div>
</template>

<style scoped>
  .logo-ministerio {
    height: 100px;
    width: auto;
  }

  .logo-img1 {
    height: 80px;
    width: auto;
  }

  .logo-img {
    height: 100px;
    width: auto;
  }

  .offcanvas-custom {
    position: fixed;
    top: 110px;
    left: 0;
    height: calc(100% - 120px);
    width: 250px;
    background-color: #393621;
    color: #fff;
    transform: translateX(-100%);
    transition: transform 0.3s;
    z-index: 1045;
    overflow-y: auto;
  }

  .offcanvas-custom.show {
    transform: translateX(0);
  }
  .search-wrapper {
    position: relative;
    display: flex;
    align-items: center;
  }

  .search-input {
    position: absolute;
    right: 100%;
    width: 0;
    opacity: 0;
    pointer-events: none;
    transition: width 0.3s ease, opacity 0.3s ease;
  }

  .search-input-visible {
    width: 200px;
    opacity: 1;
    pointer-events: auto;
  }
</style>
