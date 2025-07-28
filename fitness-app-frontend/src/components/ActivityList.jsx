import React, { useEffect, useState } from 'react';
import { Card, CardContent, Typography, CircularProgress } from '@mui/material';
import Grid2 from '@mui/material/Unstable_Grid2';
import { useNavigate } from 'react-router';
import { getActivitiesByUserDetailsId } from '../services/api';

const ActivityList = () => {
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchActivities = async () => {
            try {
                const userDetailsId = localStorage.getItem('userDetailsId');
                if (!userDetailsId) {
                    console.warn('UserDetailsId not found in localStorage');
                    return;
                }

                const response = await getActivitiesByUserDetailsId(userDetailsId);
                setActivities(response.data || []);
            } catch (error) {
                console.error('Failed to fetch activities:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchActivities();
    }, []);

    if (loading) {
        return (
            <Grid2 container justifyContent="center" alignItems="center" sx={{ minHeight: '30vh' }}>
                <CircularProgress />
            </Grid2>
        );
    }

    if (activities.length === 0) {
        return <Typography>No activities found.</Typography>;
    }

    return (
        <Grid2 container spacing={2} sx={{mb: 4, marginY: 0}}>
            {activities.map((activity) => (
                <Grid2 key={activity.id} xs={12} sm={6} md={4}>
                    <Card
                        sx={{ cursor: 'pointer', transition: '0.6s', '&:hover': { boxShadow: 6 } }}
                        onClick={() => navigate(`/activities/${activity.id}`)}
                    >
                        <CardContent>
                            <Typography variant='h6'>{activity.type}</Typography>
                            <Typography variant='body2'>Duration: {activity.duration} mins</Typography>
                            <Typography variant='body2'>Calories: {activity.caloriesBurned}</Typography>
                            <Typography variant='caption' color='text.secondary'>
                                {new Date(activity.createdAt).toLocaleDateString()}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid2>
            ))}
        </Grid2>
    );
};

export default ActivityList;
