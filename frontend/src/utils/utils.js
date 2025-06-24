import { useExpedientesStore } from "@/stores/expedientes";
import { useSolicitudesStore } from "@/stores/solicitudes";
import { usePresupuestosSecresStore } from "@/stores/presupuestos-secres";
import { useCostesPorDiaStore } from "@/stores/costes-por-dia";
import { useReservistasStore } from "@/stores/reservistas";

export async function cargarTodaLaApi() {
  await Promise.all([
    useExpedientesStore().cargarElementos(),
    useSolicitudesStore().cargarElementos(),
    useReservistasStore().cargarElementos(),
    usePresupuestosSecresStore().cargarElementos(),
    useCostesPorDiaStore().cargarElementos(),
  ]);
  for (let solicitud of useSolicitudesStore().elementos) {
    await useSolicitudesStore().cargarReservistaEnSolicitud(solicitud);
  }
  for (let reservista of useReservistasStore().elementos) {
    await useReservistasStore().crearListadoSolicitudes(reservista);
  }
  for (let expediente of useExpedientesStore().elementos) {
    await useExpedientesStore().cargarSolicitudesEnExpediente(expediente);
  }
}

export function getNombreDAO(tipoSolicitud) {
  const tiposSolicitudes = {
    PS: "prestaciones-servicios-unidad",
    FC: "formaciones-continuadas",
    EX: "activaciones-ampliadas",
  };

  return tiposSolicitudes[tipoSolicitud];
}

export function getId(url) {
  const urlATrozos = url.split("/");

  return parseInt(urlATrozos[urlATrozos.length - 1]);
}

export function formatearAtributoEnElFrontend(atributo) {
  const nombresFront = {
    PS: "Prestación servicio unidad",
    FC: "Formación continuada",
    EX: "Activación ampliada",
    PENDIENTE_EVALUACION: "Pendiente de evaluación",
    ACEPTADA_PENDIENTE_PUBLICACION: "Aceptada, pendiente de publicación",
    ACEPTADA_PUBLICADA: "Aceptada y publicada",
    RECHAZADA: "Rechazada",
    ABIERTO: "Abierto",
    ENVIADO_AL_BOD: "Enviado al BOD",
    PUBLICADO: "Publicado",
  };

  return nombresFront[atributo];
}

export function formatearFecha(fecha) {
  const fechaObj = new Date(fecha);
  const opciones = { year: "numeric", month: "2-digit", day: "2-digit" };

  return fechaObj.toLocaleDateString("es-ES", opciones);
}

export function formatearCentimosAEuros(centimos) {
  const euros = (centimos / 100).toFixed(2);
  const [entero, decimales] = euros.split(".");
  const enteroConPuntos = entero.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

  return `${enteroConPuntos},${decimales} €`;
}
