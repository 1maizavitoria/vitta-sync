import { Box, Chip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { useSearchParams } from "react-router-dom";

import Perfil from "../../components/ui/Perfil";
import EmergencyContactsCard from "../../components/ui/cards/EmergencyContactsCard";
import { useI18n } from "../../src/i18n";

export default function Reports() {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();
    const [searchParams] = useSearchParams();
    const view = searchParams.get("view") || "patient";
    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const showEmergencyContacts = view === "patient" || userType === "paciente";
    const isProfileView = view === "profile";
    const isPatientUser = userType === "paciente";

    const chipLabel = isProfileView
        ? t("reports.chips.profile")
        : isPatientUser
            ? t("reports.chips.myData")
            : t("reports.chips.selectedPatient");

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
                        {t("reports.title")}
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
                        {t("reports.description")}
                    </Typography>
                </Box>

                <Chip
                    label={chipLabel}
                    sx={{
                        maxWidth: "100%",
                        fontWeight: 800,
                        color: "primary.dark",
                        bgcolor: isDark ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.12)",
                        border: "1px solid",
                        borderColor: vitta.borderStrong,
                        "& .MuiChip-label": {
                            overflow: "hidden",
                            textOverflow: "ellipsis"
                        }
                    }}
                />
            </Box>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: {
                        xs: "minmax(0, 1fr)",
                        lg: showEmergencyContacts
                            ? "minmax(0, 1.3fr) minmax(320px, 0.7fr)"
                            : "minmax(0, 1fr)",
                    },
                    gap: { xs: 2, md: 3 },
                    alignItems: "start",
                    minWidth: 0
                }}
            >
                <Perfil view={view} />
                {showEmergencyContacts && <EmergencyContactsCard />}
            </Box>
        </Box>
    );
}
