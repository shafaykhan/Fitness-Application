import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL:API_URL
});

api.interceptors.request.use((config) => {
        const keycloakId = localStorage.getItem('keycloakId');
        const token = localStorage.getItem('token');

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        if (keycloakId) {
            config.headers['X-Keycloak-Id'] = keycloakId;
        }
        return config;
    }
);

// USER DETAILS
export const fetchUserDetailsByKeycloakId = (keycloakId) => api.get(`/users/by-keycloak/${keycloakId}`);

// ACTIVITY
export const getActivitiesByUserDetailsId = (userDetailsId) => api.get(`/activities/by-user/${userDetailsId}`);
export const addActivity = (activity) => api.post('/activities', activity);

// AI-RECOMMENDATIONS
export const getRecommendationDetailsByActivityId = (id) => api.get(`/ai-recommendations/by-activity/${id}`);