/// <reference types="vite/client" />
import axios from "axios";

function resolveApiBaseUrl() {
  const envBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim();
  if (envBaseUrl) {
    return envBaseUrl.replace(/\/+$/, "");
  }

  if (typeof window !== "undefined" && (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1")) {
    return "http://localhost:8080";
  }

  return "";
}

const api = axios.create({
  baseURL: resolveApiBaseUrl()
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("wishlist-token");
  if (token) {
    config.headers = config.headers ?? {};
    if (typeof config.headers === "object") {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

export default api;
