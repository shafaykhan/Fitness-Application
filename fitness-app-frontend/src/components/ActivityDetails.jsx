import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { getRecommendationDetailsByActivityId } from '../services/api';
import { Box, Card, CardContent, Divider, Typography } from '@mui/material';

const ActivityDetails = () => {
    const { id } = useParams();
    const [activity, setActivity] = useState(null);
    const [recommendation, setRecommendation] = useState(null);

    useEffect(() => {
        const fetchActivityDetail = async () => {
            try {
                const response = await getRecommendationDetailsByActivityId(id);
                setActivity(response.data);
                setRecommendation(response.data.recommendation);
            } catch (error) {
                console.error('Error fetching activity details:', error);
            }
        };

        fetchActivityDetail();
    }, [id]);

    if (!activity) return <Typography>Loading...</Typography>;

    return (
        <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
            <Card sx={{ mb: 2 }}>
                <CardContent>
                    <Typography variant="h5" gutterBottom>Activity Details</Typography>
                    <Typography>Type: {activity.type}</Typography>
                    <Typography>Duration: {activity.duration} minutes</Typography>
                    <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
                    <Typography>Date: {activity.createdAt && new Date(activity.createdAt).toLocaleString()}</Typography>
                </CardContent>
            </Card>

            {recommendation && (
                <Card>
                    <CardContent>
                        <Typography variant="h5" gutterBottom>AI Recommendation</Typography>

                        <Typography variant="h6">Analysis</Typography>
                        <Typography paragraph>{activity.recommendation}</Typography>

                        <Divider sx={{ my: 2 }} />

                        {activity.improvements?.length > 0 && (
                            <>
                                <Typography variant="h6">Improvements</Typography>
                                {activity.improvements.map((improvement, index) => (
                                    <Typography key={index} paragraph>• {improvement}</Typography>
                                ))}
                                <Divider sx={{ my: 2 }} />
                            </>
                        )}

                        {activity.suggestions?.length > 0 && (
                            <>
                                <Typography variant="h6">Suggestions</Typography>
                                {activity.suggestions.map((suggestion, index) => (
                                    <Typography key={index} paragraph>• {suggestion}</Typography>
                                ))}
                                <Divider sx={{ my: 2 }} />
                            </>
                        )}

                        {activity.safety?.length > 0 && (
                            <>
                                <Typography variant="h6">Safety Guidelines</Typography>
                                {activity.safety.map((safety, index) => (
                                    <Typography key={index} paragraph>• {safety}</Typography>
                                ))}
                            </>
                        )}
                    </CardContent>
                </Card>
            )}
        </Box>
    );
};

export default ActivityDetails;
