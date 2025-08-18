// src/lib/api.js
import axios from 'axios'

export const API = axios.create({
    baseURL: (import.meta.env.VITE_API_BASE || '/api').replace(/\/+$/, ''), // '/api'
})

// Attach Authorization automatically if a JWT is present
API.interceptors.request.use((cfg) => {
    const tok = localStorage.getItem('authToken')
    if (tok) cfg.headers.Authorization = `Bearer ${tok}`
    return cfg
})
