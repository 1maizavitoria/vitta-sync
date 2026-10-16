import { Box, Paper, Stack, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import { useThemeMode } from "../../src/theme/ThemeModeProvider";

function hasValue(value) {
    return value !== null && value !== undefined;
}

function SummaryCard({ title, children }) {
    const theme = useTheme();

    return (
        <Paper
            component="article"
            variant="outlined"
            sx={{
                p: 2,
                borderRadius: 2.5,
                borderColor: theme.vitta.border,
                backgroundColor: theme.vitta.panelBackground,
                minWidth: 0
            }}
        >
            <Typography variant="subtitle1" sx={{ fontWeight: 800, mb: 1.25 }}>
                {title}
            </Typography>
            <Stack spacing={0.75}>{children}</Stack>
        </Paper>
    );
}

function SummaryValue({ label, value }) {
    return (
        <Typography variant="body2" sx={{ overflowWrap: "anywhere" }}>
            <Box component="span" sx={{ fontWeight: 800 }}>{label}: </Box>
            {value}
        </Typography>
    );
}

function getEventLabel(type, t) {
    const translationKey = `activity.events.${type}.title`;
    const translated = t(translationKey);

    if (translated !== translationKey) return translated;

    const readable = String(type || "")
        .replaceAll("_", " ")
        .toLocaleLowerCase();

    return readable ? readable.charAt(0).toLocaleUpperCase() + readable.slice(1) : "—";
}

export default function DashboardAggregatedSummary({
    dailyVitals = [],
    dailyHabits = [],
    eventsByType = [],
    formatDate,
    formatMeasurement,
    formatNumber,
    t
}) {
    const theme = useTheme();
    const { accessibilityMode } = useThemeMode();
    const gridSx = {
        display: "grid",
        gridTemplateColumns: { xs: "minmax(0, 1fr)", md: "repeat(2, minmax(0, 1fr))", xl: "repeat(3, minmax(0, 1fr))" },
        gap: 2
    };

    return (
        <Paper
            component="section"
            sx={{
                p: { xs: 2, md: 3 },
                borderRadius: 3,
                border: "1px solid",
                borderColor: theme.vitta.border,
                boxShadow: theme.vitta.shadow,
                backgroundColor: "background.paper"
            }}
        >
            <Typography variant="h5" sx={{ fontWeight: 800 }}>
                {t("dashboard.aggregatedSummary.title")}
            </Typography>
            <Typography color="text.secondary" sx={{ mt: 0.5, mb: 3, fontSize: accessibilityMode ? "1rem" : undefined }}>
                {t("dashboard.aggregatedSummary.description")}
            </Typography>

            <Stack spacing={3.5}>
                <Box component="section" aria-labelledby="daily-vitals-summary-title">
                    <Typography id="daily-vitals-summary-title" variant="h6" sx={{ fontWeight: 800, mb: 1.5 }}>
                        {t("dashboard.aggregatedSummary.vitalsTitle")}
                    </Typography>
                    {dailyVitals.length === 0 ? (
                        <Typography color="text.secondary">{t("dashboard.aggregatedSummary.empty")}</Typography>
                    ) : (
                        <Box sx={gridSx}>
                            {dailyVitals.map((item, index) => (
                                <SummaryCard key={`${item.dia || "vital"}-${index}`} title={formatDate(item.dia)}>
                                    <SummaryValue label={t("dashboard.aggregatedSummary.measurements")} value={formatNumber(item.medicoes || 0)} />
                                    {hasValue(item.mediaFcBpm) && <SummaryValue label={t("dashboard.aggregatedSummary.heartRate")} value={formatMeasurement(item.mediaFcBpm, "bpm")} />}
                                    {hasValue(item.mediaFrRpm) && <SummaryValue label={t("dashboard.aggregatedSummary.respiratoryRate")} value={formatMeasurement(item.mediaFrRpm, "rpm")} />}
                                    {hasValue(item.mediaPaSistolica) && hasValue(item.mediaPaDiastolica) && (
                                        <SummaryValue label={t("dashboard.aggregatedSummary.pressure")} value={`${formatNumber(item.mediaPaSistolica)}/${formatNumber(item.mediaPaDiastolica)} mmHg`} />
                                    )}
                                    {hasValue(item.mediaTempCelcius) && <SummaryValue label={t("dashboard.aggregatedSummary.temperature")} value={formatMeasurement(item.mediaTempCelcius, "°C")} />}
                                    {hasValue(item.mediaSpo2Porcento) && <SummaryValue label={t("dashboard.aggregatedSummary.saturation")} value={formatMeasurement(item.mediaSpo2Porcento, "%")} />}
                                    {hasValue(item.mediaPeso) && <SummaryValue label={t("dashboard.aggregatedSummary.weight")} value={formatMeasurement(item.mediaPeso, "kg")} />}
                                </SummaryCard>
                            ))}
                        </Box>
                    )}
                </Box>

                <Box component="section" aria-labelledby="daily-habits-summary-title">
                    <Typography id="daily-habits-summary-title" variant="h6" sx={{ fontWeight: 800, mb: 1.5 }}>
                        {t("dashboard.aggregatedSummary.habitsTitle")}
                    </Typography>
                    {dailyHabits.length === 0 ? (
                        <Typography color="text.secondary">{t("dashboard.aggregatedSummary.empty")}</Typography>
                    ) : (
                        <Box sx={gridSx}>
                            {dailyHabits.map((item, index) => (
                                <SummaryCard key={`${item.dia || "habit"}-${index}`} title={formatDate(item.dia)}>
                                    <SummaryValue label={t("dashboard.aggregatedSummary.records")} value={formatNumber(item.registros || 0)} />
                                    {hasValue(item.mediaHorasSono) && <SummaryValue label={t("dashboard.aggregatedSummary.sleep")} value={`${formatNumber(item.mediaHorasSono)} ${t("dashboard.timeline.units.hours")}`} />}
                                    {hasValue(item.totalMinutosExercicio) && <SummaryValue label={t("dashboard.aggregatedSummary.exercise")} value={`${formatNumber(item.totalMinutosExercicio)} ${t("dashboard.timeline.units.minutes")}`} />}
                                </SummaryCard>
                            ))}
                        </Box>
                    )}
                </Box>

                <Box component="section" aria-labelledby="events-by-type-summary-title">
                    <Typography id="events-by-type-summary-title" variant="h6" sx={{ fontWeight: 800, mb: 1.5 }}>
                        {t("dashboard.aggregatedSummary.eventsTitle")}
                    </Typography>
                    {eventsByType.length === 0 ? (
                        <Typography color="text.secondary">{t("dashboard.aggregatedSummary.empty")}</Typography>
                    ) : (
                        <Box sx={gridSx}>
                            {eventsByType.map((item, index) => (
                                <SummaryCard key={`${item.tipo || "event"}-${index}`} title={getEventLabel(item.tipo, t)}>
                                    <SummaryValue label={t("dashboard.aggregatedSummary.occurrences")} value={formatNumber(item.quantidade || 0)} />
                                </SummaryCard>
                            ))}
                        </Box>
                    )}
                </Box>
            </Stack>
        </Paper>
    );
}
