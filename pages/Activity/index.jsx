import { useEffect, useState } from "react";

import { Box, Typography } from "@mui/material";

import { usePatient } from "../../context/PatientContext";

import { getPatientEvents, markEventsAsRead } from "../../services/eventService";

import EventCard from "../../components/ui/cards/EventCard";

export default function Activity() {

    const { selectedPatient } = usePatient();
    const [events, setEvents] = useState([]);

    async function loadEvents() {

        if (!selectedPatient?.id) {
            return;
        }

        try {
            const data =
                await getPatientEvents(
                    selectedPatient.id
                );
            await markEventsAsRead(
                selectedPatient.id
            );
            window.dispatchEvent(
                new Event("notificationsUpdated")
            );
            setEvents(data);

        } catch (error) {

            console.error(error);
        }
    }

    useEffect(() => {

        loadEvents();

    }, [selectedPatient]);

    return (

        <Box p={4}>

            <Typography
                variant="h4"
                sx={{
                    fontWeight: 700,
                    mb: 4
                }}
            >
                Atividade do grupo
            </Typography>

            <Box
                display="flex"
                flexDirection="column"
                gap={2}
            >
                {events.length === 0 ? (
                    <Typography
                        variant="h6"
                        color="text.secondary"
                        align="center"
                        sx={{ mt: 2 }}
                    >
                        Sem atividades
                    </Typography>
                ) : (
                    events.map((event) => (
                        <EventCard
                            key={event.id}
                            event={event}
                        />
                    ))
                )}
            </Box>
        </Box>
    );
}