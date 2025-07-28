import { createSlice } from '@reduxjs/toolkit'

const authSlice = createSlice({
    name: 'auth',
    initialState : {
        user: JSON.parse(localStorage.getItem('user')) || null,
        token: localStorage.getItem('token') || null,
        keycloakId: localStorage.getItem('keycloakId') | null,
        userDetailsId: localStorage.getItem('userDetailsId') | null
    },
    reducers: {
        setCredentials: (state, action) => {
            state.user = action.payload.user;
            state.token = action.payload.token;
            state.keycloakId = action.payload.user.sub;

            localStorage.setItem('token', action.payload.token);
            localStorage.setItem('user', JSON.stringify(action.payload.user));
            localStorage.setItem('keycloakId', action.payload.user.sub);
        },
        logout: (state) => {
            state.user = null;
            state.token = null;
            state.keycloakId = null;
            state.userDetailsId = null;

            localStorage.removeItem('token');
            localStorage.removeItem('user');
            localStorage.removeItem('keycloakId');
            localStorage.removeItem('userDetailsId');
        },
    },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;