import {
    Alert,
    Box,
    Button,
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
import { useMemo } from "react";
import { usePatient } from "../../context/PatientContext";
import { useI18n } from "../../src/i18n";
import { useThemeMode } from "../../src/theme/ThemeModeProvider";
import { createLocalDashboardPreferencesRepository } from "../../src/features/dashboard/infrastructure/localDashboardPreferencesRepository";
import { useDashboardController } from "../../src/features/dashboard/presentation/useDashboardController";

import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import DashboardAggregatedSummary from "../../components/ui/DashboardAggregatedSummary";
import DashboardChart from "../../components/ui/DashboardChart";
import ClinicalStability from "../../components/ui/ClinicalStability";
import ClinicalTimeline from "../../components/ui/ClinicalTimeline";
import DatePickerUI from "../../components/ui/DatePicker";
import PersonalBaseline from "../../components/ui/PersonalBaseline";
import DashboardCustomizationDialog from "../../src/features/dashboard/presentation/DashboardCustomizationDialog";

const periods = [7, 30, 90, "custom"];
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

function getDashboardErrorKey(errorType) {
    const errorKeys = {
        network: "dashboard.errors.network",
        invalidRequest: "dashboard.errors.invalidRequest",
        forbidden: "dashboard.errors.forbidden",
        notFound: "dashboard.errors.notFound",
        server: "dashboard.errors.server"
    };

    return errorKeys[errorType] || "dashboard.loadError";
}

function getLatestValue(category, formatMeasurement, formatNumber) {
    if (category.codigo === "pressao") {
        const systolic = category.series
            .find((serie) => serie.codigo === "sistolica")
            ?.pontos.at(-1)?.valor;
        const diastolic = category.series
            .find((serie) => serie.codigo === "diastolica")
            ?.pontos.at(-1)?.valor;

        if (systolic != null && diastolic != null) {
            return `${formatNumber(systolic)}/${formatNumber(diastolic)} ${category.unidade}`;
        }
    }

    const points = category.series.flatMap((serie) =>
        serie.pontos.map((point) => ({ ...point, serie: serie.codigo }))
    );

    const lastPoint = points.sort(
        (first, second) => new Date(second.data) - new Date(first.data)
    )[0];

    return lastPoint ? formatMeasurement(lastPoint.valor, category.unidade) : null;
}

export default function Dashboard() {
    const theme = useTheme();
    const { selectedPatient } = usePatient();
    const { t, formatDate, formatMeasurement, formatNumber, locale } = useI18n();
    const { accessibilityMode } = useThemeMode();
    const loggedUserCpf = localStorage.getItem("CPF") || localStorage.getItem("cpf");
    const preferencesRepository = useMemo(
        () => createLocalDashboardPreferencesRepository(loggedUserCpf),
        [loggedUserCpf]
    );
    const {
        dashboard,
        loading,
        error,
        errorType,
        clinicalTimeline,
        dashboardPreferences,
        period,
        periodDays,
        setPeriod,
        customDateRange,
        updateCustomDateRange,
        dateRangeValid,
        categoryFilter,
        setCategoryFilter
    } = useDashboardController({
        cpf: selectedPatient?.cpf,
        preferencesRepository
    });
    const dashboardErrorKey = getDashboardErrorKey(errorType);

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
    const baselines = dashboard?.linhasBase || [];
    const dailyVitals = dashboard?.resumoDiario || [];
    const dailyHabits = dashboard?.resumoHabitos || [];
    const eventsByType = dashboard?.eventosPorTipo || [];
    const widgetRegistry = {
        stability: (
            <ClinicalStability
                items={clinicalStability}
                period={periodDays}
                t={t}
                formatNumber={formatNumber}
            />
        ),
        baseline: <PersonalBaseline items={baselines} categoryFilter={categoryFilter} />,
        latestValues: (
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: { xs: "1fr", sm: "repeat(2, 1fr)", lg: "repeat(4, 1fr)" },
                    gap: 2,
                    mb: 3
                }}
            >
                {categories.map((category) => {
                    const latestValue = getLatestValue(category, formatMeasurement, formatNumber);
                    return (
                        <Paper
                            key={category.codigo}
                            sx={{
                                p: 2.5,
                                borderRadius: 3,
                                border: "1px solid",
                                borderColor: theme.vitta.border,
                                boxShadow: theme.vitta.shadow,
                                minHeight: accessibilityMode ? 150 : undefined
                            }}
                        >
                            <Typography variant={accessibilityMode ? "body1" : "body2"} color="text.secondary" sx={{ mb: 1 }}>
                                {t(`dashboard.categories.${category.codigo}`)}
                            </Typography>
                            <Typography variant="h5" sx={{ fontWeight: 800 }}>
                                {latestValue || "—"}
                            </Typography>
                            <Typography variant={accessibilityMode ? "body2" : "caption"} color="text.secondary">
                                {t("dashboard.latestValue")}
                            </Typography>
                        </Paper>
                    );
                })}
            </Box>
        ),
        charts: (
            <Box sx={{ display: "grid", gridTemplateColumns: { xs: "minmax(0, 1fr)", xl: "repeat(2, minmax(0, 1fr))" }, gap: 3 }}>
                {categories.map((category) => (
                    <DashboardChart
                        key={category.codigo}
                        category={category}
                        baselines={baselines}
                        title={t(`dashboard.categories.${category.codigo}`)}
                        seriesNames={seriesNames}
                        formatDate={formatDate}
                        formatMeasurement={formatMeasurement}
                        formatNumber={formatNumber}
                        emptyText={t("dashboard.emptyPeriod")}
                    />
                ))}
            </Box>
        ),
        aggregatedSummary: (
            <DashboardAggregatedSummary
                dailyVitals={dailyVitals}
                dailyHabits={dailyHabits}
                eventsByType={eventsByType}
                formatDate={formatDate}
                formatMeasurement={formatMeasurement}
                formatNumber={formatNumber}
                t={t}
            />
        ),
        timeline: (
            <ClinicalTimeline
                items={clinicalTimeline.items}
                loading={clinicalTimeline.loading}
                errorType={clinicalTimeline.errorType}
                formatDate={formatDate}
                formatMeasurement={formatMeasurement}
                formatNumber={formatNumber}
                locale={locale}
                t={t}
            />
        )
    };

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
                        borderColor: theme.vitta.borderStrong,
                        height: accessibilityMode ? "auto" : undefined,
                        minHeight: accessibilityMode ? 40 : undefined,
                        "& .MuiChip-label": {
                            whiteSpace: accessibilityMode ? "normal" : "nowrap",
                            overflowWrap: "anywhere"
                        }
                    }}
                />
            </Box>

            {selectedPatient && accessibilityMode && (
                <Alert severity="info" sx={{ mb: 3, fontSize: "1rem" }}>
                    {t("dashboard.accessibilityHint")}
                </Alert>
            )}

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
                            flexWrap: "wrap",
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
                            size={accessibilityMode ? "medium" : "small"}
                            sx={{
                                flexWrap: "wrap",
                                "& .MuiToggleButton-root": accessibilityMode
                                    ? { minHeight: 48, px: 2, fontSize: "1rem" }
                                    : undefined,
                                "& .MuiToggleButton-root:focus-visible": {
                                    outline: "3px solid",
                                    outlineColor: "secondary.main",
                                    outlineOffset: 2
                                }
                            }}
                        >
                            {periods.map((days) => (
                                <ToggleButton key={days} value={days} sx={{ fontWeight: 700 }}>
                                    {days === "custom"
                                        ? t("dashboard.customPeriod")
                                        : t("dashboard.lastDays").replace("{days}", days)}
                                </ToggleButton>
                            ))}
                        </ToggleButtonGroup>

                        <Box display="flex" flexDirection={{ xs: "column", sm: "row" }} gap={1.5} sx={{ minWidth: { md: 420 } }}>
                            <FormControl size={accessibilityMode ? "medium" : "small"} sx={{ minWidth: { xs: "100%", md: 260 }, flex: 1 }}>
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
                            <Button
                                variant="outlined"
                                startIcon={<SettingsOutlinedIcon />}
                                onClick={dashboardPreferences.openCustomization}
                                disabled={dashboardPreferences.loadingPreferences}
                                sx={{
                                    minHeight: accessibilityMode ? 48 : 40,
                                    px: 2,
                                    fontWeight: 800,
                                    textTransform: "none",
                                    whiteSpace: "nowrap",
                                    "&:focus-visible": {
                                        outline: "3px solid",
                                        outlineColor: "secondary.main",
                                        outlineOffset: 2
                                    }
                                }}
                            >
                                {t("dashboard.customization.open")}
                            </Button>
                        </Box>

                        {period === "custom" && (
                            <Box sx={{ width: "100%" }}>
                                <Box
                                    sx={{
                                        display: "grid",
                                        gridTemplateColumns: { xs: "minmax(0, 1fr)", sm: "repeat(2, minmax(0, 1fr))" },
                                        gap: 2
                                    }}
                                >
                                    <DatePickerUI
                                        label={t("dashboard.startDate")}
                                        value={customDateRange.inicio}
                                        error={!dateRangeValid}
                                        onChange={(value) => updateCustomDateRange("inicio", value)}
                                    />
                                    <DatePickerUI
                                        label={t("dashboard.endDate")}
                                        value={customDateRange.fim}
                                        error={!dateRangeValid}
                                        onChange={(value) => updateCustomDateRange("fim", value)}
                                    />
                                </Box>
                                {!dateRangeValid && (
                                    <Typography color="error" variant={accessibilityMode ? "body2" : "caption"}>
                                        {t("dashboard.invalidDateRange")}
                                    </Typography>
                                )}
                            </Box>
                        )}
                    </Paper>

                    {error && <Alert severity="error" sx={{ mb: 3 }}>{t(dashboardErrorKey)}</Alert>}

                    {loading && (
                        <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", md: "repeat(3, 1fr)" }, gap: 2 }}>
                            {[1, 2, 3, 4, 5, 6].map((item) => (
                                <Skeleton key={item} variant="rounded" height={150} />
                            ))}
                        </Box>
                    )}

                    <Box
                        component="section"
                        aria-label={t("dashboard.customization.currentLayout")}
                        sx={{
                            display: "flex",
                            flexDirection: "column",
                            gap: 3,
                            "& > [data-dashboard-widget] > *": {
                                mt: "0 !important",
                                mb: "0 !important"
                            }
                        }}
                    >
                        {dashboardPreferences.layout.widgets
                            .filter((widget) => widget.visible)
                            .map((widget) => {
                                if (!dateRangeValid) return null;
                                if (widget.id !== "timeline" && (loading || error)) return null;
                                if (widget.id === "baseline" && ["sono", "exercicio"].includes(categoryFilter)) return null;

                                return (
                                    <Box key={widget.id} data-dashboard-widget={widget.id}>
                                        {widgetRegistry[widget.id]}
                                    </Box>
                                );
                            })}
                    </Box>

                    <DashboardCustomizationDialog
                        open={dashboardPreferences.open}
                        draft={dashboardPreferences.draft}
                        saving={dashboardPreferences.savingPreferences}
                        error={dashboardPreferences.preferenceError}
                        accessibilityMode={accessibilityMode}
                        t={t}
                        onClose={dashboardPreferences.cancelCustomization}
                        onSave={dashboardPreferences.saveCustomization}
                        onRestore={dashboardPreferences.restoreDefault}
                        onToggle={dashboardPreferences.toggleWidget}
                        onMove={dashboardPreferences.moveWidget}
                    />
                </>
            )}
        </Box>
    );
}
