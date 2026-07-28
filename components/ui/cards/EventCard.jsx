import { Box, Chip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import NotificationsActiveOutlinedIcon from "@mui/icons-material/NotificationsActiveOutlined";
import PriorityHighIcon from "@mui/icons-material/PriorityHigh";
import { useI18n } from "../../../src/i18n";

function getPriorityStyle(priority, isDark) {
    switch (priority) {
        case "critico":
            return {
                color: isDark ? "#fca5a5" : "#b91c1c",
                background: isDark ? "rgba(248, 113, 113, 0.14)" : "rgba(220, 38, 38, 0.1)",
                border: isDark ? "rgba(248, 113, 113, 0.24)" : "rgba(220, 38, 38, 0.22)",
                icon: <ErrorOutlineIcon fontSize="small" />
            };

        case "alta":
            return {
                color: isDark ? "#fdba74" : "#c2410c",
                background: isDark ? "rgba(251, 146, 60, 0.14)" : "rgba(234, 88, 12, 0.1)",
                border: isDark ? "rgba(251, 146, 60, 0.24)" : "rgba(234, 88, 12, 0.22)",
                icon: <PriorityHighIcon fontSize="small" />
            };

        default:
            return {
                color: isDark ? "#7dd3fc" : "#0f766e",
                background: isDark ? "rgba(14, 165, 233, 0.12)" : "rgba(15, 118, 110, 0.1)",
                border: isDark ? "rgba(14, 165, 233, 0.22)" : "rgba(15, 118, 110, 0.18)",
                icon: <NotificationsActiveOutlinedIcon fontSize="small" />
            };
    }
}

export default function EventCard({ event }) {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();
    const priorityStyle = getPriorityStyle(event.prioridade, isDark);
    const priorityLabel =
        t(`activity.priorities.${event.prioridade}`) ||
        t("activity.priorities.normal");
    const userTypeLabel =
        t(`userTypes.${event.usuarioTipo}`) ||
        event.usuarioTipo;

    return (
        <Box
            sx={{
                backgroundColor: "background.paper",
                borderRadius: 3,
                p: { xs: 2, md: 2.5 },
                border: `1px solid ${priorityStyle.border}`,
                boxShadow: vitta.shadow,
                transition: "transform .2s ease, box-shadow .2s ease, border-color .2s ease",
                minWidth: 0,

                "&:hover": {
                    transform: "translateY(-2px)",
                    boxShadow: vitta.shadow
                }
            }}
        >
            <Box
                display="flex"
                justifyContent="space-between"
                alignItems="flex-start"
                gap={2}
                mb={1.5}
                sx={{ minWidth: 0 }}
            >
                <Box display="flex" gap={1.5} sx={{ minWidth: 0 }}>
                    <Box
                        sx={{
                            width: 42,
                            height: 42,
                            borderRadius: 2,
                            background: priorityStyle.background,
                            color: priorityStyle.color,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            flex: "0 0 auto"
                        }}
                    >
                        {priorityStyle.icon}
                    </Box>

                    <Box sx={{ minWidth: 0 }}>
                        <Typography
                            sx={{
                                fontWeight: 800,
                                color: "text.primary",
                                overflowWrap: "anywhere"
                            }}
                        >
                            {event.titulo}
                        </Typography>

                        <Typography
                            variant="body2"
                            sx={{
                                color: "text.secondary",
                                fontWeight: 700,
                                mt: 0.35,
                                overflowWrap: "anywhere"
                            }}
                        >
                            {event.usuarioNome}
                            {" • "}
                            {userTypeLabel}
                        </Typography>
                    </Box>
                </Box>

                <Chip
                    label={priorityLabel}
                    size="small"
                    sx={{
                        bgcolor: priorityStyle.background,
                        color: priorityStyle.color,
                        border: `1px solid ${priorityStyle.border}`,
                        fontWeight: 800,
                        flex: "0 0 auto"
                    }}
                />
            </Box>

            <Typography
                sx={{
                    color: "text.secondary",
                    mb: 2,
                    overflowWrap: "anywhere"
                }}
            >
                {event.descricao}
            </Typography>

            <Typography
                variant="body2"
                sx={{
                    color: "text.secondary",
                    fontWeight: 700
                }}
            >
                {new Date(event.criadoEm).toLocaleString("pt-BR")}
            </Typography>
        </Box>
    );
}
