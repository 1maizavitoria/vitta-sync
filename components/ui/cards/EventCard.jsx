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

function parseMetadata(metadata) {
    if (!metadata) {
        return {};
    }

    if (typeof metadata === "string") {
        try {
            return JSON.parse(metadata);
        } catch {
            return {};
        }
    }

    return metadata;
}

function getTranslation(t, path) {
    const value = t(path);

    return value === path ? "" : value;
}

function interpolate(template, values) {
    return template.replace(/\{(\w+)\}/g, (_, key) => values[key] ?? "");
}

function formatEventText(event, t) {
    const eventType = event.tipoEvento || event.eventType || event.tipo;

    if (!eventType) {
        return {
            title: event.titulo,
            description: event.descricao
        };
    }

    const metadata = parseMetadata(event.metadata);
    const values = {
        userName: event.usuarioNome || metadata.userName || "",
        patientName:
            metadata.patientName ||
            metadata.nomePaciente ||
            event.pacienteNome ||
            "",
        documentName:
            metadata.documentName ||
            metadata.nomeArquivo ||
            metadata.fileName ||
            "",
    };

    const titleTemplate = getTranslation(t, `activity.events.${eventType}.title`);
    const descriptionTemplate = getTranslation(t, `activity.events.${eventType}.description`);

    return {
        title: titleTemplate ? interpolate(titleTemplate, values) : event.titulo,
        description: descriptionTemplate ? interpolate(descriptionTemplate, values) : event.descricao
    };
}

export default function EventCard({ event }) {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t, formatDateTime } = useI18n();
    const priorityStyle = getPriorityStyle(event.prioridade, isDark);
    const priorityLabel =
        t(`activity.priorities.${event.prioridade}`) ||
        t("activity.priorities.normal");
    const userTypeLabel =
        t(`userTypes.${event.usuarioTipo}`) ||
        event.usuarioTipo;
    const eventText = formatEventText(event, t);

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
                            {eventText.title}
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
                {eventText.description}
            </Typography>

            <Typography
                variant="body2"
                sx={{
                    color: "text.secondary",
                    fontWeight: 700
                }}
            >
                {formatDateTime(event.criadoEm)}
            </Typography>
        </Box>
    );
}
