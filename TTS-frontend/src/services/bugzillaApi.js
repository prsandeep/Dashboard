import axios from 'axios';

// Use environment variables if available or fallback to localhost
const API_BASE_URL = 'http://10.226.25.31:8080';

// Define DEBUG variable - this is needed for conditional logging
const DEBUG = process.env.NODE_ENV !== 'production';

// Create axios instance with timeout for Bugzilla/Superset API
const bugzillaApiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 15000, // 15 second timeout for dashboard operations
});

// Attach JWT token to every request if present
bugzillaApiClient.interceptors.request.use(
  (config) => {
    if (DEBUG) console.log(`🚀 Bugzilla Request: ${config.method.toUpperCase()} ${config.url}`);
    
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    if (DEBUG) console.error('❌ Bugzilla Request error:', error);
    return Promise.reject(error);
  }
);

// Add response interceptor
bugzillaApiClient.interceptors.response.use(
  (response) => {
    if (DEBUG) console.log(`✅ Bugzilla Response: ${response.config.method.toUpperCase()} ${response.config.url}`, response.status);
    return response;
  },
  async (error) => {
    // Enhanced error logging for Bugzilla API
    if (DEBUG) {
      console.error('❌ Bugzilla Response error:', {
        status: error.response?.status,
        statusText: error.response?.statusText,
        url: error.config?.url,
        method: error.config?.method?.toUpperCase(),
        message: error.message
      });
    }

    // Network errors (server not available)
    if (error.code === 'ERR_NETWORK') {
      console.error(`Network error: Could not connect to Bugzilla API at ${API_BASE_URL}. Please check if the server is running.`);
      return Promise.reject({
        ...error,
        userMessage: `Cannot connect to the Bugzilla dashboard server. Please ensure it's running and accessible.`
      });
    }

    const originalRequest = error.config;

    // If error is 401 and we haven't already tried to refresh
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = localStorage.getItem('refreshToken');
      if (!refreshToken) {
        // No refresh token available, logout
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(error);
      }

      try {
        // Try to refresh the token
        const response = await axios.post(`${API_BASE_URL}/api/auth/refresh-token`, {
          refreshToken: refreshToken
        });

        // Save the new access token
        const { accessToken } = response.data;
        localStorage.setItem('accessToken', accessToken);

        // Update the authorization header and retry
        originalRequest.headers['Authorization'] = `Bearer ${accessToken}`;
        return bugzillaApiClient(originalRequest);
      } catch (refreshError) {
        // Refresh failed, redirect to login
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    // Handle other common status codes
    if (error.response) {
      switch (error.response.status) {
        case 403:
          return Promise.reject({
            ...error,
            userMessage: 'You do not have permission to access this Bugzilla dashboard.'
          });
        case 404:
          return Promise.reject({
            ...error,
            userMessage: 'The requested Bugzilla dashboard was not found.'
          });
        case 500:
          return Promise.reject({
            ...error,
            userMessage: 'Bugzilla dashboard server error. Please try again later or contact support.'
          });
        default:
          return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  }
);

// Bugzilla Dashboard API
export const bugzillaApi = {
  // Get guest token for Superset dashboard embedding
  getGuestToken: (dashboardId) => {
    return bugzillaApiClient.post('/api/superset/guest-token', {
      dashboardId: dashboardId,
    });
  },

};

// Export the axios client instance if needed for custom requests
export { bugzillaApiClient };

export default bugzillaApi;