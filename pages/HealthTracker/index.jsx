import {
    Box,
    Chip,
    Tab,
    Tabs,
    Typography
} from "@mui/material";
import { useTheme } from "@mui/material/styles";

import AssignmentOutlinedIcon from "@mui/icons-material/AssignmentOutlined";
import DirectionsWalkIcon from "@mui/icons-material/DirectionsWalk";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";

import { useState } from "react";

import { HabitTracker } from "../../components/ui/HabitTracker";
import { SymptomTracker } from "../../components/ui/SymptomTracker";
import { VitalTracker } from "../../components/ui/VitalTracker";
import ReminderCard from "../../components/ui/cards/RemindCard";
import { usePatient } from "../../context/PatientContext";
import { useI18n } from "../../src/i18n";

export default function HealthTreacker() {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { selectedPatient } = usePatient();
    const { t } = useI18n();
    const userType =
        localStorage
            .getItem("tipo")
            ?.toLowerCase();

    const [tab, setTab] = useState(0);

    const tabs = [
        {
            label: t("healthTracker.tabs.vitals"),
            icon: <FavoriteBorderIcon />,
            component: <VitalTracker />
        },
        {
            label: t("healthTracker.tabs.habits"),
            icon: <DirectionsWalkIcon />,
            component: <HabitTracker />
        },
        {
            label: t("healthTracker.tabs.symptoms"),
            icon: <AssignmentOutlinedIcon />,
            component: <SymptomTracker />
        }
    ];

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
                        {t("healthTracker.title")}
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
                        {t("healthTracker.description")}
                    </Typography>
                </Box>

                {userType !== "paciente" && (
                    <Chip
                        label={`${t("healthTracker.selectedPatient")}: ${selectedPatient?.nome || t("healthTracker.noPatient")}`}
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
                )}
            </Box>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: {
                        xs: "minmax(0, 1fr)",
                        lg: "minmax(0, 1fr) 340px",
                    },
                    gap: { xs: 2, md: 3 },
                    alignItems: "start",
                    minWidth: 0
                }}
            >
                <Box sx={{ minWidth: 0 }}>
                    <Tabs
                        value={tab}
                        onChange={(event, newValue) => setTab(newValue)}
                        variant="scrollable"
                        scrollButtons="auto"
                        allowScrollButtonsMobile
                        sx={{
                            mb: 1,
                            p: 0.5,
                            borderRadius: 3,
                            background: "background.paper",
                            border: "1px solid",
                            borderColor: vitta.border,
                            boxShadow: vitta.shadow,

                            "& .MuiTabs-indicator": {
                                display: "none",
                            },
                        }}
                    >
                        {tabs.map((item) => (
                            <Tab
                                key={item.label}
                                icon={item.icon}
                                iconPosition="start"
                                label={item.label}
                                sx={{
                                    borderRadius: 2,
                                    minHeight: 52,
                                    minWidth: { xs: 132, sm: 170 },
                                    textTransform: "none",
                                    fontWeight: 800,
                                    mx: 0.5,
                                    color: "text.secondary",
                                    transition: "all .2s ease",

                                    "&.Mui-selected": {
                                        color: "text.primary",
                                        background: isDark
                                            ? "linear-gradient(135deg, rgba(34, 197, 94, 0.18) 0%, rgba(14, 165, 233, 0.14) 100%)"
                                            : "linear-gradient(135deg, #dcfce7 0%, #e0f2fe 100%)",
                                        boxShadow: vitta.shadow,
                                    },
                                }}
                            />
                        ))}
                    </Tabs>

                    <Box
                        sx={{
                            mt: -1,
                            p: { xs: 2, md: 3 },
                            border: "1px solid",
                            borderColor: vitta.border,
                            borderRadius: 3,
                            background: "background.paper",
                            boxShadow: vitta.shadow,
                            minWidth: 0
                        }}
                    >
                        {tabs[tab].component}
                    </Box>
                </Box>

                <ReminderCard />
            </Box>
        </Box>
    );
}
