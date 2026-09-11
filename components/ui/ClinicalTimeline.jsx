import {
    Alert,
    Box,
    Chip,
    Paper,
    Skeleton,
    Stack,
    ToggleButton,
    ToggleButtonGroup,
    Typography
} from "@mui/material";
import BedtimeOutlinedIcon from "@mui/icons-material/BedtimeOutlined";
import HealingOutlinedIcon from "@mui/icons-material/HealingOutlined";
import MonitorHeartOutlinedIcon from "@mui/icons-material/MonitorHeartOutlined";
import { useTheme } from "@mui/material/styles";
import { useEffect, useMemo, useState } from "react";

import { getClinicalTimeline } from "../../services/timelineService";

const filters = ["TODOS", "SINAL_VITAL", "HABITO", "SINTOMA"];

function getItemIcon(type) {
    if (type === "HABITO") return <BedtimeOutlinedIcon fontSize="small" />;
    if (type === "SINTOMA") return <HealingOutlinedIcon fontSize="small" />;
    return <MonitorHeartOutlinedIcon fontSize="small" />;
}

function getItemColor(type) {
    if (type === "HABITO") return "info";
    if (type === "SINTOMA") return "warning";
    return "success";
}

function getItemValues(item, t, formatMeasurement, formatNumber) {
    const data = item.dados || {};

    if (item.tipo === "SINAL_VITAL") {
        return [
            data.paSistolica != null && data.paDiastolica != null
                ? `${t("dashboard.timeline.fields.pressure")}: ${formatNumber(data.paSistolica)}/${formatNumber(data.paDiastolica)} mmHg`
                : null,
            data.fcBpm != null
                ? `${t("dashboard.timeline.fields.heartRate")}: ${formatMeasurement(data.fcBpm, "bpm")}`
                : null,
            data.frRpm != null
                ? `${t("dashboard.timeline.fields.respiratoryRate")}: ${formatMeasurement(data.frRpm, "rpm")}`
                : null,
            data.tempCelcius != null
                ? `${t("dashboard.timeline.fields.temperature")}: ${formatMeasurement(data.tempCelcius, "°C")}`
                : null,
            data.spo2Porcento != null
                ? `${t("dashboard.timeline.fields.oxygenSaturation")}: ${formatMeasurement(data.spo2Porcento, "%")}`
                : null,
            data.peso != null
                ? `${t("dashboard.timeline.fields.weight")}: ${formatMeasurement(data.peso, "kg")}`
                : null
        ].filter(Boolean);
    }

    if (item.tipo === "HABITO") {
        return [
            data.horasSono != null
                ? `${t("dashboard.timeline.fields.sleep")}: ${formatMeasurement(data.horasSono, t("dashboard.timeline.units.hours"))}`
                : null,
            data.minutosExercicio != null
                ? `${t("dashboard.timeline.fields.exercise")}: ${formatMeasurement(data.minutosExercicio, t("dashboard.timeline.units.minutes"))}`
                : null
        ].filter(Boolean);
    }

    return [
        data.sintoma || t("dashboard.timeline.fields.unreportedSymptom"),
        data.intensidadeDor != null
            ? `${t("dashboard.timeline.fields.painIntensity")}: ${formatNumber(data.intensidadeDor)}/10`
            : null
    ].filter(Boolean);
}

function formatTime(value, locale) {
    if (!value) return null;
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return null;

    return new Intl.DateTimeFormat(locale, {
        hour: "2-digit",
        minute: "2-digit"
    }).format(date);
}

export default function ClinicalTimeline({
    cpf,
    inicio,
    fim,
    formatDate,
    formatMeasurement,
    formatNumber,
    locale,
    t
}) {
    const theme = useTheme();
    const [items, setItems] = useState([]);
    const [filter, setFilter] = useState("TODOS");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (!cpf) {
            setItems([]);
            return;
        }

        let active = true;

        async function loadTimeline() {
            setLoading(true);
            setError(false);

            try {
                const data = await getClinicalTimeline({ cpf, inicio, fim });
                if (active) setItems(data?.itens || []);
            } catch (requestError) {
                console.error(requestError);
                if (active) {
                    setItems([]);
                    setError(true);
                }
            } finally {
                if (active) setLoading(false);
            }
        }

        loadTimeline();

        return () => {
            active = false;
        };
    }, [cpf, inicio, fim]);

    const groups = useMemo(() => {
        const filteredItems = filter === "TODOS"
            ? items
            : items.filter((item) => item.tipo === filter);

        return filteredItems.reduce((result, item) => {
            const date = item.dataReferencia || item.dataRegistro?.slice(0, 10);
            if (!date) return result;

            if (!result[date]) result[date] = [];
            result[date].push(item);
            return result;
        }, {});
    }, [items, filter]);

    const groupedDates = Object.keys(groups).sort((first, second) => second.localeCompare(first));

    return (
        <Paper
            component="section"
            sx={{
                mt: 3,
                p: { xs: 2, md: 3 },
                borderRadius: 3,
                border: "1px solid",
                borderColor: theme.vitta.border,
                boxShadow: theme.vitta.shadow
            }}
        >
            <Box
                sx={{
                    display: "flex",
                    alignItems: { xs: "stretch", md: "center" },
                    justifyContent: "space-between",
                    flexDirection: { xs: "column", md: "row" },
                    gap: 2,
                    mb: 3
                }}
            >
                <Box>
                    <Typography variant="h5" sx={{ fontWeight: 800 }}>
                        {t("dashboard.timeline.title")}
                    </Typography>
                    <Typography color="text.secondary" sx={{ mt: 0.5 }}>
                        {t("dashboard.timeline.description")}
                    </Typography>
                </Box>

                <ToggleButtonGroup
                    exclusive
                    value={filter}
                    onChange={(_, value) => value && setFilter(value)}
                    size="small"
                    sx={{ flexWrap: "wrap" }}
                >
                    {filters.map((type) => (
                        <ToggleButton key={type} value={type} sx={{ fontWeight: 700 }}>
                            {t(`dashboard.timeline.filters.${type.toLowerCase()}`)}
                        </ToggleButton>
                    ))}
                </ToggleButtonGroup>
            </Box>

            {error && <Alert severity="error">{t("dashboard.timeline.loadError")}</Alert>}

            {loading ? (
                <Stack spacing={2}>
                    {[1, 2, 3].map((item) => (
                        <Skeleton key={item} variant="rounded" height={100} />
                    ))}
                </Stack>
            ) : !error && groupedDates.length === 0 ? (
                <Alert severity="info">{t("dashboard.timeline.empty")}</Alert>
            ) : !error && (
                <Stack spacing={3}>
                    {groupedDates.map((date) => (
                        <Box key={date}>
                            <Typography variant="subtitle1" sx={{ fontWeight: 800, mb: 1.5 }}>
                                {formatDate(date)}
                            </Typography>

                            <Box sx={{ ml: 1.25, pl: 2.5, borderLeft: "2px solid", borderColor: theme.vitta.borderStrong }}>
                                <Stack spacing={1.5}>
                                    {groups[date].map((item) => {
                                        const values = getItemValues(item, t, formatMeasurement, formatNumber);
                                        const time = formatTime(item.dataRegistro, locale);

                                        return (
                                            <Box
                                                key={`${item.tipo}-${item.id}`}
                                                sx={{
                                                    position: "relative",
                                                    p: 2,
                                                    borderRadius: 2.5,
                                                    bgcolor: theme.vitta.panelBackground,
                                                    border: "1px solid",
                                                    borderColor: theme.vitta.border,
                                                    "&::before": {
                                                        content: '""',
                                                        position: "absolute",
                                                        left: -31,
                                                        top: 22,
                                                        width: 12,
                                                        height: 12,
                                                        borderRadius: "50%",
                                                        bgcolor: "primary.main",
                                                        border: "3px solid",
                                                        borderColor: "background.paper"
                                                    }
                                                }}
                                            >
                                                <Stack
                                                    direction={{ xs: "column", sm: "row" }}
                                                    alignItems={{ xs: "flex-start", sm: "center" }}
                                                    justifyContent="space-between"
                                                    spacing={1}
                                                    sx={{ mb: 1 }}
                                                >
                                                    <Chip
                                                        size="small"
                                                        color={getItemColor(item.tipo)}
                                                        icon={getItemIcon(item.tipo)}
                                                        label={t(`dashboard.timeline.types.${item.tipo.toLowerCase()}`)}
                                                        sx={{ fontWeight: 700 }}
                                                    />
                                                    {time && (
                                                        <Typography variant="caption" color="text.secondary">
                                                            {time}
                                                        </Typography>
                                                    )}
                                                </Stack>

                                                <Stack spacing={0.5}>
                                                    {values.map((value) => (
                                                        <Typography key={value} variant="body2">
                                                            {value}
                                                        </Typography>
                                                    ))}
                                                </Stack>
                                            </Box>
                                        );
                                    })}
                                </Stack>
                            </Box>
                        </Box>
                    ))}
                </Stack>
            )}
        </Paper>
    );
}
