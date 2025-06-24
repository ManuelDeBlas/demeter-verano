import axios from "axios";

function llamadaAPI(method, body, path) {
  let config = {
    method: method ?? "get",
    maxBodyLength: Infinity,
    url: path,
    headers: {},
  };
  if (body) {
    (config.data = body), (config.headers["content-Type"] = "application/json");
  }
  return axios.request(config);
}

export function get(url) {
  return llamadaAPI("get", null, url);
}

export function post(data, url) {
  return llamadaAPI("post", data, url);
}

export function deleteEntidad(url) {
  return llamadaAPI("delete", null, url);
}

export function put(objeto, url) {
  return llamadaAPI("put", objeto, url);
}

export function patch(url, data) {
  return llamadaAPI("patch", data, url);
}
