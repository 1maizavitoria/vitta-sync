import { useCallback, useEffect, useState } from "react";

import { Box, Chip, Skeleton, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import NotificationsActiveOutlinedIcon from "@mui/icons-material/NotificationsActiveOutlined";

import EventCard from "../../components/ui/cards/EventCard";
import { usePatient } from "../../context/PatientContext";
import { useI18n } from "../../src/i18n";
import { getPatientEvents, markEventsAsRead } from "../../services/eventService";

export default function Activity() {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { selectedPatient } = usePatient();
    const { t } = useI18n();
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(false);

    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const isPatientUser = userType === "paciente";

    const headerDescription = isPatientUser
        ? t("activity.descriptions.patient")
        : t("activity.descriptions.linkedPatient");

    const chipLabel = isPatientUser
        ? t("activity.chips.myActivities")
        : selectedPatient?.nome || t("activity.chips.noPatientSelected");

    const loadEvents = useCallback(async () => {
        if (!selectedPatient?.id) {
            setEvents([]);
            return;
        }

        try {
            setLoading(true);

            const data = await getPatientEvents(selectedPatient.id);

            await markEventsAsRead(selectedPatient.id);

            window.dispatchEvent(
                new Event("notificationsUpdated")
            );

            setEvents(data);
        } catch (error) {
            console.error("Erro ao carregar atividades:", error);
            setEvents([]);
        } finally {
            setLoading(false);
        }
    }, [selectedPatient?.id]);

    useEffect(() => {
        loadEvents();
    }, [loadEvents]);

    return (
        <Box
            sx={{
                minHeight: "100vh",
                px: { xs: 2, md: 4 },
                py: { xs: 3, md: 4 },
                overflowX: "hidden",
                bgcolor: "background.default",
                background: vitta.pageBackground,
                color: "text.primary",
            }}
        >
            <Box
                sx={{
                    mb: 3,
                    p: { xs: 2.5, md: 3 },
                    borderRadius: 3,
                    background: vitta.panelBackground,
                    border: "1px solid",
                    borderColor: vitta.border,
                    boxShadow: vitta.shadow,
                    display: "flex",
                    alignItems: { xs: "flex-start", md: "center" },
                    justifyContent: "space-between",
                    gap: 2,
                    flexDirection: { xs: "column", md: "row" },
                    minWidth: 0
                }}
            >
                <Box sx={{ minWidth: 0 }}>
                    <Typography
                        variant="h4"
                        sx={{
                            fontWeight: 800,
                            color: "text.primary",
                            letterSpacing: 0,
                            overflowWrap: "anywhere",
                            fontSize: { xs: "1.6rem", md: "2.125rem" }
                        }}
                    >
                        {t("activity.title")}
                    </Typography>

                    <Typography
                        variant="body1"
                        sx={{
                            color: "text.secondary",
                            mt: 1,
                            maxWidth: 680,
                            overflowWrap: "anywhere"
                        }}
                    >
                        {headerDescription}
                    </Typography>
                </Box>

                <Chip
                    icon={<NotificationsActiveOutlinedIcon />}
                    label={chipLabel}
                    sx={{
                        maxWidth: "100%",
                        fontWeight: 800,
                        color: selectedPatient?.id || isPatientUser ? "primary.dark" : "text.secondary",
                        bgcolor: selectedPatient?.id || isPatientUser
                            ? isDark ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.12)"
                            : isDark ? "rgba(148, 163, 184, 0.12)" : "rgba(100, 116, 139, 0.1)",
                        border: "1px solid",
                        borderColor: selectedPatient?.id || isPatientUser ? vitta.borderStrong : "divider",
                        "& .MuiChip-icon": {
                            color: "inherit"
                        },
                        "& .MuiChip-label": {
                            overflow: "hidden",
                            textOverflow: "ellipsis"
                        }
                    }}
                />
            </Box>

            <Box
                sx={{
                    p: { xs: 2, md: 3 },
                    border: "1px solid",
                    borderColor: vitta.border,
                    borderRadius: 3,
                    background: "background.paper",
                    boxShadow: vitta.shadow,
                    minWidth: 0
                }}
            >
                <Box
                    display="flex"
                    alignItems="center"
                    justifyContent="space-between"
                    gap={2}
                    mb={2.5}
                    sx={{ minWidth: 0 }}
                >
                    <Box sx={{ minWidth: 0 }}>
                        <Typography
                            sx={{
                                fontWeight: 800,
                                color: "text.primary",
                                fontSize: "1.2rem",
                                overflowWrap: "anywhere"
                            }}
                        >
                            {t("activity.history")}
                        </Typography>

                        <Typography
                            sx={{
                                color: "text.secondary",
                                fontSize: "0.9rem",
                                mt: 0.25,
                                overflowWrap: "anywhere"
                            }}
                        >
                            {t("activity.historyDescription")}
                        </Typography>
                    </Box>
                </Box>

                {loading ? (
                    <Box display="flex" flexDirection="column" gap={2}>
                        {[1, 2, 3].map((item) => (
                            <Skeleton
                                key={item}
                                variant="rounded"
                                height={118}
                                sx={{ borderRadius: 3 }}
                            />
                        ))}
                    </Box>
                ) : events.length === 0 ? (
                    <Box
                        sx={{
                            width: "100%",
                            py: 5,
                            px: 2,
                            textAlign: "center",
                            borderRadius: 3,
                            border: "1px dashed",
                            borderColor: vitta.borderStrong,
                            bgcolor: isDark ? "rgba(220, 252, 231, 0.06)" : "rgba(240, 253, 244, 0.42)"
                        }}
                    >
                        <Typography
                            sx={{
                                fontWeight: 800,
                                color: "text.primary",
                                mb: 1
                            }}
                        >
                            {t("activity.emptyTitle")}
                        </Typography>

                        <Typography color="text.secondary">
                            {isPatientUser
                                ? t("activity.emptyDescriptionPatient")
                                : t("activity.emptyDescriptionLinkedPatient")}
                        </Typography>
                    </Box>
                ) : (
                    <Box display="flex" flexDirection="column" gap={2}>
                        {events.map((event) => (
                            <EventCard
                                key={event.id}
                                event={event}
                            />
                        ))}
                    </Box>
                )}
            </Box>
        </Box>
    );
}
