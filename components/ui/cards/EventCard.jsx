import { Box, Chip, Typography } from "@mui/material";

export default function EventCard({ event }) {

    function getPriorityColor(priority) {

        switch (priority) {

            case "critico":
                return "#d32f2f";

            case "alta":
                return "#ed6c02";

            default:
                return "#1976d2";
        }
    }

    return (

        <Box
            sx={{
                backgroundColor: "#fff",
                borderRadius: "24px",
                padding: 3,
                boxShadow:
                    "0px 4px 15px rgba(0,0,0,0.08)"
            }}
        >

            <Box
                display="flex"
                justifyContent="space-between"
                alignItems="center"
                mb={1}
            >

                <Typography
                    sx={{
                        fontWeight: 700
                    }}
                >
                    {event.titulo}
                </Typography>

                <Chip
                    label={event.prioridade}
                    size="small"
                    sx={{
                        backgroundColor:
                            getPriorityColor(
                                event.prioridade
                            ),

                        color: "#fff",
                        fontWeight: 600
                    }}
                />

            </Box>

            <Typography
                sx={{
                    color: "#555",
                    mb: 2
                }}
            >
                {event.descricao}
            </Typography>

            <Typography
                variant="body2"
                sx={{
                    color: "#666",
                    fontWeight: 600,
                    mb: 1
                }}
            >
                {event.usuarioNome}
                {" • "}
                {event.usuarioTipo}
            </Typography>

            <Typography
                variant="body2"
                sx={{
                    color: "#888"
                }}
            >
                {new Date(event.criadoEm)
                    .toLocaleString("pt-BR")}
            </Typography>

        </Box>
    );
}