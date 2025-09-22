
import axios from 'axios'

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

const authApi = axios.create({
    baseURL: `${API_BASE}/api/auth`,
    timeout: 15000,
})

export async function loginUser(credentials) {
    try {
        const response = await authApi.post('/login', credentials, {
            headers: { 'Content-Type': 'application/json' }
        })

        const data = response.data
        console.log("🟢 FULL LOGIN RESPONSE:", data)

        // Normalize role detection
        let backendRole = null
        if (Array.isArray(data?.roles) && data.roles.length > 0) {
            backendRole = data.roles[0]
        } else if (typeof data?.role === 'string') {
            backendRole = data.role
        }

        console.log("🟢 RAW BACKEND ROLE:", backendRole)

        // More flexible role detection
        let role = 'USER'
        if (backendRole && backendRole.toUpperCase().includes('ADMIN')) {
            role = 'ADMIN'
        }

        console.log("🟢 FINAL DETECTED ROLE:", role)

        const token = data?.token
        const resolvedName = (
            data?.name ||
            data?.fullName ||
            data?.user?.name ||
            data?.username ||
            credentials.username
        ).trim()

        // Save authentication state
        localStorage.setItem('isLoggedIn', 'true')
        localStorage.setItem('role', role)
        localStorage.setItem('adminName', resolvedName)

        if (token) {
            localStorage.setItem('authToken', token)
        }

        // Return user data for navigation
        return { role, fullName: resolvedName }

    } catch (error) {
        console.error("🔴 LOGIN ERROR:", error)
        if (error.response?.status === 401) {
            throw new Error('Invalid credentials')
        }
        throw error
    }
}

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