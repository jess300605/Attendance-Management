import axios from "axios"

const API_URL = "http://localhost:8080/api"

const api = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
})

// Add a response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const { response } = error
    if (response) {
      // Handle specific error status codes
      switch (response.status) {
        case 401:
          // Handle unauthorized
          console.error("Unauthorized access")
          break
        case 403:
          // Handle forbidden
          console.error("Forbidden access")
          break
        case 404:
          // Handle not found
          console.error("Resource not found")
          break
        case 500:
          // Handle server error
          console.error("Server error")
          break
        default:
          // Handle other errors
          console.error("API error:", response.data)
      }
    } else {
      // Handle network errors
      console.error("Network error")
    }
    return Promise.reject(error)
  },
)

export default api

