import { useState } from "react";

import { Box, Chip, Tab, Tabs, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import CloudUploadOutlinedIcon from "@mui/icons-material/CloudUploadOutlined";
import FolderOutlinedIcon from "@mui/icons-material/FolderOutlined";

import { usePatient } from "../../context/PatientContext";
import { useI18n } from "../../src/i18n";
import HealthHub from "../HealthHub";
import SharedDocuments from "../SharedDocuments";

export default function Documents() {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { selectedPatient } = usePatient();
    const { t } = useI18n();

    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const cpfUsuario =
        localStorage.getItem("cpf") ||
        localStorage.getItem("CPF");

    const isPatientUser = userType === "paciente";
    const hasLinkedPatientSelected =
        selectedPatient &&
        selectedPatient.cpf !== cpfUsuario;
    const canViewDocuments =
        isPatientUser ||
        hasLinkedPatientSelected;

    const tabs = [
        canViewDocuments && {
            label: isPatientUser ? t("documents.tabs.myDocuments") : t("documents.tabs.patientDocuments"),
            value: "documents",
            icon: <FolderOutlinedIcon />
        },

        userType === "saude" && {
            label: t("documents.tabs.upload"),
            value: "upload",
            icon: <CloudUploadOutlinedIcon />
        }
    ].filter(Boolean);

    const [tab, setTab] = useState("");
    const activeTab =
        tabs.some((item) => item.value === tab)
            ? tab
            : tabs[0]?.value || "";

    const headerDescription = isPatientUser
        ? t("documents.descriptions.patient")
        : t("documents.descriptions.linkedPatient");

    const statusLabel = isPatientUser
        ? t("documents.status.myDocuments")
        : hasLinkedPatientSelected
            ? selectedPatient.nome
            : t("documents.status.noPatientSelected");

    const needsPatientSelection =
        !isPatientUser &&
        !hasLinkedPatientSelected;

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
                        {t("documents.title")}
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
                    label={statusLabel}
                    sx={{
                        maxWidth: "100%",
                        fontWeight: 800,
                        color: needsPatientSelection ? "text.secondary" : "primary.dark",
                        bgcolor: needsPatientSelection
                            ? isDark ? "rgba(148, 163, 184, 0.12)" : "rgba(100, 116, 139, 0.1)"
                            : isDark ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.12)",
                        border: "1px solid",
                        borderColor: needsPatientSelection ? "divider" : vitta.borderStrong,
                        "& .MuiChip-label": {
                            overflow: "hidden",
                            textOverflow: "ellipsis"
                        }
                    }}
                />
            </Box>

            {tabs.length > 0 ? (
                <>
                    <Tabs
                        value={activeTab}
                        onChange={(_, value) => setTab(value)}
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
                                key={item.value}
                                value={item.value}
                                icon={item.icon}
                                iconPosition="start"
                                label={item.label}
                                sx={{
                                    borderRadius: 2,
                                    minHeight: 52,
                                    minWidth: { xs: 184, sm: 220 },
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
                        {activeTab === "documents" && canViewDocuments && <SharedDocuments />}
                        {activeTab === "upload" && userType === "saude" && <HealthHub />}
                    </Box>
                </>
            ) : (
                <Box
                    sx={{
                        p: { xs: 3, md: 4 },
                        borderRadius: 3,
                        background: "background.paper",
                        border: "1px solid",
                        borderColor: vitta.border,
                        boxShadow: vitta.shadow,
                        textAlign: "center"
                    }}
                >
                    <Typography
                        sx={{
                            fontWeight: 800,
                            color: "text.primary",
                            mb: 1
                        }}
                    >
                        {t("documents.emptyState.selectPatientTitle")}
                    </Typography>

                    <Typography color="text.secondary">
                        {t("documents.emptyState.selectPatientDescription")}
                    </Typography>
                </Box>
            )}
        </Box>
    );
}
