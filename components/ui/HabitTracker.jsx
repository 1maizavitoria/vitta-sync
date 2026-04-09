import { Box, Grid, Typography } from "@mui/material";
import HabitCard from "./HabitCard";
import BedtimeIcon from "@mui/icons-material/Bedtime";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import RestaurantIcon from "@mui/icons-material/Restaurant";

export function HabitTracker() {

    return (
        <Box>
            <Typography variant="h5" fontWeight="bold" sx={{ mb: 3, mt: 5 }}>
                Hábitos de Estilo de Vida
            </Typography>

            <Grid container spacing={3}>
                <Grid item xs={12} md={4}>
                    <HabitCard
                        icon={<BedtimeIcon />}
                        title="Sono"
                        description="Registre as horas dormidas"
                        isDone={true}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <HabitCard
                        icon={<FitnessCenterIcon />}
                        title="Exercício"
                        description="Registre atividades físicas"
                        isDone={true}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <HabitCard
                        icon={<RestaurantIcon />}
                        title="Alimentação"
                        description="Registre suas refeições"
                        isDone={true}
                    />
                </Grid>
            </Grid>
        </Box>
    )
}