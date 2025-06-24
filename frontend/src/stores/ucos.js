import { defineStore } from "pinia";
import ucosJSON from "@/assets/ucos.json";

export const useUcosStore = defineStore("ucos", {
  state: () => ({
    ucos: ucosJSON._embedded.ucos.map((uco) => ({
      ...uco,
    })),
  }),
});