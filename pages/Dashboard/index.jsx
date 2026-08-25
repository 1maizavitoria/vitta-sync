import {
    Alert,
    Box,
    Chip,
    FormControl,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Skeleton,
    ToggleButton,
    ToggleButtonGroup,
    Typography
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { useEffect, useMemo, useState } from "react";

import DashboardChart from "../../components/ui/DashboardChart";
import ClinicalStability from "../../components/ui/ClinicalStability";
import { usePatient } from "../../context/PatientContext";
import { getDashboard } from "../../services/dashboardService";
import { useI18n } from "../../src/i18n";

const periods = [7, 30, 90];
const categoryCodes = [
    "todas",
    "pressao",
    "frequencia_cardiaca",
    "frequencia_respiratoria",
    "temperatura",
    "saturacao",
    "peso",
    "sono",
    "exercicio"
];

function toApiDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
}

function getDateRange(days) {
    const end = new Date();
    const start = new Date();
    start.setDate(end.getDate() - (days - 1));
    return { inicio: toApiDate(start), fim: toApiDate(end) };
}

function getLatestValue(category) {
    if (category.codigo === "pressao") {
        const systolic = category.series
            .find((serie) => serie.codigo === "sistolica")
            ?.pontos.at(-1)?.valor;
        const diastolic = category.series
            .find((serie) => serie.codigo === "diastolica")
            ?.pontos.at(-1)?.valor;

        if (systolic != null && diastolic != null) {
            return `${systolic}/${diastolic} ${category.unidade}`;
        }
    }

    const points = category.series.flatMap((serie) =>
        serie.pontos.map((point) => ({ ...point, serie: serie.codigo }))
    );

    const lastPoint = points.sort(
        (first, second) => new Date(second.data) - new Date(first.data)
    )[0];

    return lastPoint ? `${lastPoint.valor} ${category.unidade}` : null;
}

export default function Dashboard() {
    const theme = useTheme();
    const { selectedPatient } = usePatient();
    const { t, language } = useI18n();
    const [period, setPeriod] = useState(7);
    const [categoryFilter, setCategoryFilter] = useState("todas");
    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (!selectedPatient?.cpf) {
            setDashboard(null);
            return;
        }

        let active = true;

        async function loadDashboard() {
            const range = getDateRange(period);
            setLoading(true);
            setError(false);

            try {
                const data = await getDashboard({
                    cpf: selectedPatient.cpf,
                    ...range,
                    categorias: categoryFilter === "todas" ? undefined : categoryFilter
                });

                if (active) {
                    setDashboard(data);
                }
            } catch (requestError) {
                console.error(requestError);
                if (active) {
                    setDashboard(null);
                    setError(true);
                }
            } finally {
                if (active) {
                    setLoading(false);
                }
            }
        }

        loadDashboard();

        return () => {
            active = false;
        };
    }, [selectedPatient?.cpf, period, categoryFilter]);

    const seriesNames = useMemo(() => ({
        sistolica: t("dashboard.series.sistolica"),
        diastolica: t("dashboard.series.diastolica"),
        frequencia_cardiaca: t("dashboard.categories.frequencia_cardiaca"),
        frequencia_respiratoria: t("dashboard.categories.frequencia_respiratoria"),
        temperatura: t("dashboard.categories.temperatura"),
        saturacao: t("dashboard.categories.saturacao"),
        peso: t("dashboard.categories.peso"),
        sono: t("dashboard.categories.sono"),
        exercicio: t("dashboard.categories.exercicio")
    }), [t]);

    const categories = dashboard?.categorias || [];
    const clinicalStability = dashboard?.estabilidadeClinica || [];

    return (
        <Box
            sx={{
                minHeight: "100vh",
                px: { xs: 2, md: 4 },
                py: { xs: 3, md: 4 },
                background: theme.vitta.pageBackground,
                color: "text.primary"
            }}
        >
            <Box
                sx={{
                    mb: 3,
                    p: { xs: 2.5, md: 3 },
                    borderRadius: 3,
                    background: theme.vitta.panelBackground,
                    border: "1px solid",
                    borderColor: theme.vitta.border,
                    boxShadow: theme.vitta.shadow,
                    display: "flex",
                    alignItems: { xs: "flex-start", md: "center" },
                    justifyContent: "space-between",
                    flexDirection: { xs: "column", md: "row" },
                    gap: 2
                }}
            >
                <Box>
                    <Typography variant="h4" sx={{ fontWeight: 800, fontSize: { xs: "1.6rem", md: "2.125rem" } }}>
                        {t("dashboard.title")}
                    </Typography>
                    <Typography color="text.secondary" sx={{ mt: 1 }}>
                        {t("dashboard.description")}
                    </Typography>
                </Box>

                <Chip
                    label={`${t("dashboard.patient")}: ${selectedPatient?.nome || t("dashboard.noPatient")}`}
                    sx={{
                        maxWidth: "100%",
                        fontWeight: 800,
                        color: "primary.dark",
                        bgcolor: theme.palette.mode === "dark" ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.12)",
                        border: "1px solid",
                        borderColor: theme.vitta.borderStrong
                    }}
                />
            </Box>

            {!selectedPatient ? (
                <Alert severity="info">{t("dashboard.selectPatient")}</Alert>
            ) : (
                <>
                    <Paper
                        sx={{
                            p: 2,
                            mb: 3,
                            borderRadius: 3,
                            border: "1px solid",
                            borderColor: theme.vitta.border,
                            boxShadow: theme.vitta.shadow,
                            display: "flex",
                            alignItems: { xs: "stretch", md: "center" },
                            justifyContent: "space-between",
                            flexDirection: { xs: "column", md: "row" },
                            gap: 2
                        }}
                    >
                        <ToggleButtonGroup
                            exclusive
                            value={period}
                            onChange={(_, value) => value && setPeriod(value)}
                            size="small"
                            sx={{ flexWrap: "wrap" }}
                        >
                            {periods.map((days) => (
                                <ToggleButton key={days} value={days} sx={{ fontWeight: 700 }}>
                                    {t("dashboard.lastDays").replace("{days}", days)}
                                </ToggleButton>
                            ))}
                        </ToggleButtonGroup>

                        <FormControl size="small" sx={{ minWidth: { xs: "100%", md: 260 } }}>
                            <InputLabel>{t("dashboard.category")}</InputLabel>
                            <Select
                                value={categoryFilter}
                                label={t("dashboard.category")}
                                onChange={(event) => setCategoryFilter(event.target.value)}
                            >
                                {categoryCodes.map((code) => (
                                    <MenuItem key={code} value={code}>
                                        {t(`dashboard.categories.${code}`)}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Paper>

                    {error && <Alert severity="error" sx={{ mb: 3 }}>{t("dashboard.loadError")}</Alert>}

                    {loading ? (
                        <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", md: "repeat(3, 1fr)" }, gap: 2 }}>
                            {[1, 2, 3, 4, 5, 6].map((item) => (
                                <Skeleton key={item} variant="rounded" height={150} />
                            ))}
                        </Box>
                    ) : !error && (
                        <>
                            <ClinicalStability
                                items={clinicalStability}
                                period={period}
                                t={t}
                            />

                            <Box
                                sx={{
                                    display: "grid",
                                    gridTemplateColumns: { xs: "1fr", sm: "repeat(2, 1fr)", lg: "repeat(4, 1fr)" },
                                    gap: 2,
                                    mb: 3
                                }}
                            >
                                {categories.map((category) => {
                                    const latestValue = getLatestValue(category);
                                    return (
                                        <Paper
                                            key={category.codigo}
                                            sx={{
                                                p: 2.5,
                                                borderRadius: 3,
                                                border: "1px solid",
                                                borderColor: theme.vitta.border,
                                                boxShadow: theme.vitta.shadow
                                            }}
                                        >
                                            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                                                {t(`dashboard.categories.${category.codigo}`)}
                                            </Typography>
                                            <Typography variant="h5" sx={{ fontWeight: 800 }}>
                                                {latestValue || "—"}
                                            </Typography>
                                            <Typography variant="caption" color="text.secondary">
                                                {t("dashboard.latestValue")}
                                            </Typography>
                                        </Paper>
                                    );
                                })}
                            </Box>

                            <Box sx={{ display: "grid", gridTemplateColumns: { xs: "minmax(0, 1fr)", xl: "repeat(2, minmax(0, 1fr))" }, gap: 3 }}>
                                {categories.map((category) => (
                                    <DashboardChart
                                        key={category.codigo}
                                        category={category}
                                        title={t(`dashboard.categories.${category.codigo}`)}
                                        seriesNames={seriesNames}
                                        locale={language}
                                        emptyText={t("dashboard.emptyPeriod")}
                                    />
                                ))}
                            </Box>
                        </>
                    )}
                </>
            )}
        </Box>
    );
}
