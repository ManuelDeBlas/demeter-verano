import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
  { path: "/", redirect: "/listado/solicitudes" },
  {
    path: "/listado/solicitudes",
    name: "ListadoSolicitudesView",
    component: () => import("@/views/listados/ListadoSolicitudesView.vue"),
  },
  {
    path: "/listado/reservistas",
    name: "ListadoReservistasView",
    component: () => import("@/views/listados/ListadoReservistasView.vue"),
  },
  {
    path: "/listado/expedientes",
    name: "ListadoExpedientesView",
    component: () => import("@/views/listados/ListadoExpedientesView.vue"),
  },
  {
    path: "/listado/presupuestos-secres",
    name: "ListadoPresupuestosSecresView",
    component: () => import("@/views/listados/ListadoPresupuestosSecresView.vue"),
  },
  {
    path: "/listado/costes-por-dia",
    name: "ListadoCostesPorDiaView",
    component: () => import("@/views/listados/ListadoCostesPorDiaView.vue"),
  },
  {
    path: "/formulario/reservista",
    name: "FormularioReservistaView",
    component: () => import("@/views/formularios/FormularioReservistaView.vue"),
  },
  {
    path: "/formulario/solicitud",
    name: "FormularioSolicitudView",
    component: () => import("@/views/formularios/FormularioSolicitudView.vue"),
  },
  {
    path: "/formulario/expediente",
    name: "FormularioExpedienteView",
    component: () => import("@/views/formularios/FormularioExpedienteView.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/views/NotFoundView.vue"),
    name: "notfound",
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
