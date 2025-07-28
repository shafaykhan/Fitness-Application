import {Box, Button, Typography, Card} from "@mui/material";
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "react-oauth2-code-pkce";
import {useDispatch} from "react-redux";
import {BrowserRouter as Router, Navigate, Route, Routes, useLocation} from "react-router";
import {setCredentials} from "./store/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetails from "./components/ActivityDetails";
import {fetchUserDetailsByKeycloakId} from "./services/api.js";

const ActivitiesPage = () => {
    return (<Box sx={{p: 2, border: '1px dashed grey'}}>
        <ActivityForm onActivityAdded={() => window.location.reload()}/>
        <ActivityList/>
    </Box>);
}


function App() {
    const {token, tokenData, logIn, logOut, isAuthenticated} = useContext(AuthContext);
    const dispatch = useDispatch();
    const [authReady, setAuthReady] = useState(false);

    useEffect(() => {
        const fetchAndStoreUser = async () => {
            try {
                const keycloakId = tokenData?.sub;
                if (!keycloakId) return;

                const res = await fetchUserDetailsByKeycloakId(keycloakId); // API call
                const user = res.data;

                localStorage.setItem("userDetailsId", user.id);
                console.log("User data fetched and stored in session:", user);
            } catch (error) {
                console.error("Failed to fetch user by Keycloak ID:", error);
            }
        };

        if (token) {
            dispatch(setCredentials({ token, user: tokenData }));
            setAuthReady(true);

            fetchAndStoreUser(); // 👈 Call API here
        }
    }, [token, tokenData, dispatch]);

    return (
        <Router>
            {!token ? (
                <Box
                    sx={{
                        height: "100vh",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        justifyContent: "center",
                        textAlign: "center",
                    }}
                >
                    <Typography variant="h4" gutterBottom>
                        Welcome to the Fitness Tracker App
                    </Typography>
                    <Typography variant="subtitle1" sx={{mb: 3}}>
                        Please login to access your activities
                    </Typography>
                    <Button variant="contained" color="primary" size="large" onClick={() => {
                        logIn();
                    }}>
                        LOGIN
                    </Button>
                </Box>
            ) : (
                // <div>
                //   <pre>{JSON.stringify(tokenData, null, 2)}</pre>
                //   <pre>{JSON.stringify(token, null, 2)}</pre>
                // </div>


                <Box sx={{p: 3, border: '1px dashed grey'}}>
                    <Button variant="contained" color="secondary" style={{ float: 'right', margin: 20 }} onClick={logOut}>
                        Logout
                    </Button>
                    <Routes>
                        <Route path="/activities" element={<ActivitiesPage/>}/>
                        <Route path="/activities/:id" element={<ActivityDetails/>}/>

                        <Route path="/" element={token ? <Navigate to="/activities" replace/> :
                            <div>Welcome! Please Login.</div>}/>
                    </Routes>
                </Box>
            )}
        </Router>
    )
}

export default App