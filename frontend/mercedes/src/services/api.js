import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000,
})

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('authToken')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }

    // Add detailed logging
    console.log('🔗 API REQUEST:', {
        method: config.method?.toUpperCase(),
        url: config.url,
        fullUrl: `${config.baseURL}${config.url}`,
        headers: {
            Authorization: token ? `Bearer ${token.substring(0, 20)}...` : 'NO TOKEN',
            'Content-Type': config.headers['Content-Type'] || 'application/json'
        },
        params: config.params
    })

    return config
})

api.interceptors.response.use(
    (response) => {
        console.log('✅ API RESPONSE:', {
            url: response.config.url,
            status: response.status,
            statusText: response.statusText
        })
        return response
    },
    (error) => {
        console.error('❌ API ERROR:', {
            url: error.config?.url,
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
            message: error.message
        })

        if (error.response?.status === 401) {
            console.log('🔐 TOKEN EXPIRED - REDIRECTING TO LOGIN')
            localStorage.removeItem('authToken')
            localStorage.removeItem('isLoggedIn')
            localStorage.removeItem('role')
            localStorage.removeItem('adminName')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

// Car Management
export async function fetchCars(params = {}) {
    try {
        const response = await api.get('/cars', { params })
        return response.data
    } catch (error) {
        console.error('Failed to fetch cars:', error)
        throw error
    }
}

export async function createCar(carData) {
    try {
        const response = await api.post('/cars', carData)
        return response.data
    } catch (error) {
        console.error('Failed to create car:', error)
        throw error
    }
}

export async function updateCar(id, carData) {
    try {
        const response = await api.put(`/cars/${id}`, carData)
        return response.data
    } catch (error) {
        console.error('Failed to update car:', error)
        throw error
    }
}

export async function deleteCar(id) {
    try {
        const response = await api.delete(`/cars/${id}`)
        return response.data
    } catch (error) {
        console.error('Failed to delete car:', error)
        throw error
    }
}

export async function fetchCarById(id) {
    try {
        const response = await api.get(`/cars/${id}`)
        return response.data
    } catch (error) {
        console.error(`Failed to fetch car ${id}:`, error)
        throw error
    }
}

export async function fetchCarDescription(payload) {
    try {
        const response = await api.post('/ai/describe-car', payload)
        return response.data
    } catch (error) {
        console.error('Failed to fetch car description:', error)
        throw error
    }
}

export async function fetchCarModels() {
    try {
        const response = await api.get('/car-models')
        return response.data
    } catch (error) {
        console.error('Failed to fetch car models:', error)
        // Return fallback models if API fails
        return ['Classe S', 'Classe A', 'GLE', 'AMG GT', 'Classe C', 'GLA']
    }
}

export async function submitTestDrive(payload) {
    try {
        const response = await api.post('/test-drives', payload)
        return response.data
    } catch (error) {
        console.error('Failed to submit test drive:', error)
        throw error
    }
}

// User Management
export async function fetchUsers(params = {}) {
    try {
        const response = await api.get('/users', { params })
        const data = response.data

        // Handle different response structures
        if (data.content && data.totalElements) {
            // Spring Boot Page<T> response
            return {
                users: data.content,
                total: data.totalElements
            }
        } else if (data.items && data.total) {
            // Custom paginated response
            return {
                users: data.items,
                total: data.total
            }
        } else {
            // Simple array response
            return {
                users: Array.isArray(data) ? data : [],
                total: data.length || 0
            }
        }
    } catch (error) {
        console.error('Failed to fetch users:', error)
        throw error
    }
}

export async function createUser(userData) {
    try {
        const response = await api.post('/users', userData)
        return response.data
    } catch (error) {
        console.error('Failed to create user:', error)
        throw error
    }
}

export async function updateUser(id, userData) {
    try {
        const response = await api.put(`/users/${id}`, userData)
        return response.data
    } catch (error) {
        console.error('Failed to update user:', error)
        throw error
    }
}

export async function deleteUser(id) {
    try {
        const response = await api.delete(`/users/${id}`)
        return response.data
    } catch (error) {
        console.error('Failed to delete user:', error)
        throw error
    }
}

export async function toggleUserStatus(id) {
    try {
        const response = await api.patch(`/users/${id}/toggle`)
        return response.data
    } catch (error) {
        console.error('Failed to toggle user status:', error)
        throw error
    }
}

export async function resetUserPassword(id, passwordData) {
    try {
        const response = await api.post(`/users/${id}/reset-password`, passwordData)
        return response.data
    } catch (error) {
        console.error('Failed to reset user password:', error)
        throw error
    }
}

// Advisor Meeting
export async function submitAdvisorMeeting(meetingData) {
    try {
        const response = await api.post('/advisor-meetings', meetingData)
        return response.data
    } catch (error) {
        console.error('Failed to submit advisor meeting:', error)
        throw error
    }
}

// Special Offers
export async function fetchSpecialOffers() {
    try {
        const response = await api.get('/offers')
        return response.data
    } catch (error) {
        console.error('Failed to fetch special offers:', error)
        throw error
    }
}

// Statistics
export async function fetchStatistics() {
    try {
        const response = await api.get('/stats')
        return response.data
    } catch (error) {
        console.error('Failed to fetch statistics:', error)
        throw error
    }
}