import {Box, Button, FormControl, InputLabel, MenuItem, Select, TextField, Typography, Card} from '@mui/material';
import React, {useState} from 'react';
import {addActivity} from '../services/api';

const ActivityForm = ({onActivityAdded}) => {
    const userDetailsId = localStorage.getItem('userDetailsId') || '';
    const [activity, setActivity] = useState({
        userId: userDetailsId,
        type: '',
        duration: '',
        caloriesBurned: ''
    });
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Simple validation
        if (!activity.type || !activity.duration || !activity.caloriesBurned) {
            setError('Please fill in all fields.');
            return;
        }

        try {
            setSubmitting(true);
            await addActivity(activity);
            onActivityAdded?.(); // safe check
            setActivity({
                userId: userDetailsId,
                type: '',
                duration: '',
                caloriesBurned: ''
            });
            setError('');
        } catch (error) {
            console.error(error);
            setError('Failed to add activity. Please try again.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Card variant="outlined">
        <Box component="form" onSubmit={handleSubmit} sx={{mb: 4, marginY: 3, marginX: 0, padding: 1}}>
            {error && (
                <Typography color="error" sx={{mb: 2}}>
                    {error}
                </Typography>
            )}
            <FormControl fullWidth sx={{mb: 2}}>
                <InputLabel id="activity-type-label">Activi ty Type</InputLabel>
                <Select
                    labelId="activity-type-label"
                    value={activity.type}
                    label="Activity Type"
                    onChange={(e) => setActivity({...activity, type: e.target.value})}
                >
                    <MenuItem value="RUNNING">Running</MenuItem>
                    <MenuItem value="WALKING">Walking</MenuItem>
                    <MenuItem value="CYCLING">Cycling</MenuItem>
                    <MenuItem value="SWIMMING">Swimming</MenuItem>
                    <MenuItem value="YOGA">Yoga</MenuItem>
                    <MenuItem value="WEIGHT_TRAINING">Weight Training</MenuItem>
                    <MenuItem value="OTHER">Other</MenuItem>
                </Select>
            </FormControl>

            <TextField
                fullWidth
                label="Duration (Minutes)"
                type="number"
                sx={{mb: 2}}
                value={activity.duration}
                onChange={(e) =>
                    setActivity({...activity, duration: e.target.value})
                }
                inputProps={{min: 1}}
            />

            <TextField
                fullWidth
                label="Calories Burned"
                type="number"
                sx={{mb: 2}}
                value={activity.caloriesBurned}
                onChange={(e) =>
                    setActivity({...activity, caloriesBurned: e.target.value})
                }
                inputProps={{min: 1}}
            />

            <Button
                type="submit"
                variant="contained"
                disabled={submitting}
                fullWidth
            >
                {submitting ? 'Adding...' : 'Add Activity'}
            </Button>
        </Box>
        </Card>
    );
};

export default ActivityForm;
