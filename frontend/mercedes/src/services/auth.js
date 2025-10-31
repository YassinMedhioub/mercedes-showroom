
import axios from 'axios';
import { api } from './api'; // FIXED: Import api instance from api.js

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

const authApi = axios.create({
    baseURL: `${API_BASE}/api/auth`,
    timeout: 15000,
})



export const loginUser = async (credentials) => {
    try {
        const response = await api.post('/auth/login', credentials);  // Or your endpoint, e.g., '/api/auth/login'
        console.log('🟢 FULL LOGIN RESPONSE:', response.data);

        const backendData = response.data;

        // Detect role from backend
        let detectedRole = 'USER';  // Default
        if (backendData.roles && backendData.roles.length > 0) {
            detectedRole = backendData.roles[0];  // e.g., 'USER'
            console.log('🟢 RAW BACKEND ROLE:', detectedRole);
        } else if (backendData.role) {
            detectedRole = backendData.role;
        }
        console.log('🟢 FINAL DETECTED ROLE:', detectedRole);

        // Return full data (token + role/fullName)
        return {
            token: backendData.token,  // Ensure token is included
            role: detectedRole,
            fullName: backendData.fullName || credentials.username,  // Fallback
            roles: backendData.roles || []
        };
    } catch (error) {
        console.error('❌ Login API error:', error);
        throw error;
    }
};

export async function verifyUser() {
    try {
        const token = localStorage.getItem('authToken')
        if (!token) return null

        const response = await authApi.get('/verify', {
            headers: { Authorization: `Bearer ${token}` }
        })

        return response.data
    } catch (error) {
        console.error('User verification failed:', error)
        clearAuth()
        return null
    }
}

export function setAuth({ role, fullName, token }) {
    console.log('🔍 SETAUTH CALLED:', { role, fullName, hasToken: !!token })
    try {
        localStorage.setItem('isLoggedIn', 'true')
        if (role) localStorage.setItem('role', role)
        if (fullName) localStorage.setItem('adminName', fullName)
        if (token) localStorage.setItem('authToken', token)

        console.log('🟢 SETAUTH SUCCESS - LOCALSTORAGE UPDATED')
        console.log('🔍 FINAL LOCALSTORAGE STATE:', {
            isLoggedIn: localStorage.getItem('isLoggedIn'),
            role: localStorage.getItem('role'),
            adminName: localStorage.getItem('adminName'),
            authToken: localStorage.getItem('authToken') ? 'PRESENT' : 'MISSING'
        })
    } catch (error) {
        console.error('🔴 SETAUTH FAILED:', error)
        throw error
    }
}

export function clearAuth() {
    try {
        localStorage.removeItem('isLoggedIn')
        localStorage.removeItem('role')
        localStorage.removeItem('adminName')
        localStorage.removeItem('authToken')
        localStorage.removeItem('username')
    } catch (error) {
        console.error('Failed to clear auth:', error)
        throw error
    }
}