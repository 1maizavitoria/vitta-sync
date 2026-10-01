import { Alert, Box, Chip, LinearProgress, Paper, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { useI18n } from "../../src/i18n";

const signals = {
    peso: { unit: "kg", label: "dashboard.categories.peso" },
    frequencia_cardiaca: { unit: "bpm", label: "dashboard.categories.frequencia_cardiaca" },
    frequencia_respiratoria: { unit: "rpm", label: "dashboard.categories.frequencia_respiratoria" },
    pressao_sistolica: { unit: "mmHg", label: "dashboard.series.sistolica" },
    pressao_diastolica: { unit: "mmHg", label: "dashboard.series.diastolica" },
    temperatura: { unit: "°C", label: "dashboard.categories.temperatura" },
    saturacao: { unit: "%", label: "dashboard.categories.saturacao" }
};

export default function PersonalBaseline({ items = [], categoryFilter = "todas" }) {
    const theme = useTheme();
    const { t, formatDate, formatMeasurement } = useI18n();
    const visibleItems = items.filter((item) => signals[item.sinal] && (
        categoryFilter === "todas" || item.sinal === categoryFilter ||
        (categoryFilter === "pressao" && item.sinal.startsWith("pressao_"))
    ));

    if (categoryFilter === "sono" || categoryFilter === "exercicio") return null;

    return (
        <Box component="section" aria-labelledby="personal-baseline-title" sx={{ mb: 3 }}>
            <Typography id="personal-baseline-title" variant="h6" sx={{ fontWeight: 800 }}>
                {t("dashboard.baseline.title")}
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5, mb: 2 }}>
                {t("dashboard.baseline.description")}
            </Typography>
            {visibleItems.length === 0 ? (
                <Alert severity="info">{t("dashboard.baseline.unavailable")}</Alert>
            ) : (
                <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", md: "repeat(2, minmax(0, 1fr))", xl: "repeat(3, minmax(0, 1fr))" }, gap: 2 }}>
                    {visibleItems.map((item) => {
                        const signal = signals[item.sinal];
                        const forming = item.situacao === "em_formacao";
                        const noVariation = item.situacao === "sem_variacao";
                        const days = Math.max(0, item.diasRegistrados ?? 0);
                        const required = item.diasNecessarios ?? 14;
                        const progress = required > 0 ? Math.min(100, days / required * 100) : 0;
                        const measurement = (value) => value == null ? "—" : formatMeasurement(value, signal.unit, { maximumFractionDigits: 2 });
                        const comparison = ["abaixo", "dentro", "acima"].includes(item.comparacao) ? item.comparacao : null;

                        return (
                            <Paper key={item.sinal} sx={{ p: 2.5, borderRadius: 3, minWidth: 0, border: "1px solid", borderColor: theme.vitta.border, boxShadow: theme.vitta.shadow }}>
                                <Box sx={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: 1, mb: 2 }}>
                                    <Typography component="h3" variant="subtitle1" sx={{ fontWeight: 800, flexGrow: 1 }}>{t(signal.label)}</Typography>
                                    <Chip size="small" variant="outlined" label={t(`dashboard.baseline.status.${item.situacao}`)} />
                                </Box>
                                {forming ? (
                                    <>
                                        <Typography variant="body2" sx={{ mb: 1 }}>
                                            {t("dashboard.baseline.progress").replace("{days}", days).replace("{required}", required)}
                                        </Typography>
                                        <LinearProgress variant="determinate" value={progress} aria-label={`${t(signal.label)}: ${days}/${required}`} sx={{ height: 8, borderRadius: 4, mb: 1.5 }} />
                                        <Typography variant="body2" color="text.secondary">{t("dashboard.baseline.formationHint")}</Typography>
                                    </>
                                ) : (
                                    <>
                                        <Typography variant="body2">{t("dashboard.baseline.mean")}: {measurement(item.media)}</Typography>
                                        {noVariation ? (
                                            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{t("dashboard.baseline.noVariation")}</Typography>
                                        ) : (
                                            <Typography sx={{ fontWeight: 700, mt: 1 }}>
                                                {t("dashboard.baseline.range")}: {measurement(item.limiteInferior)} – {measurement(item.limiteSuperior)}
                                            </Typography>
                                        )}
                                        {item.dataInicio && item.dataFim && (
                                            <Typography variant="caption" color="text.secondary" sx={{ display: "block", mt: 1 }}>
                                                {t("dashboard.baseline.period")}: {formatDate(item.dataInicio)} – {formatDate(item.dataFim)}
                                            </Typography>
                                        )}
                                    </>
                                )}
                                <Box sx={{ mt: 2, pt: 1.5, borderTop: "1px solid", borderColor: "divider" }}>
                                    <Typography variant="body2">{t("dashboard.baseline.latest")}: {measurement(item.ultimoValor)}</Typography>
                                    {item.dataUltimoValor && (
                                        <Typography variant="caption" color="text.secondary">
                                            {formatDate(item.dataUltimoValor, { hour: "2-digit", minute: "2-digit" })}
                                        </Typography>
                                    )}
                                    {!forming && !noVariation && (
                                        comparison ? (
                                            <Alert severity={comparison === "dentro" ? "info" : "warning"} sx={{ mt: 1.5 }}>
                                                {t(`dashboard.baseline.comparison.${comparison}`)}
                                            </Alert>
                                        ) : (
                                            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{t("dashboard.baseline.waiting")}</Typography>
                                        )
                                    )}
                                </Box>
                            </Paper>
                        );
                    })}
                </Box>
            )}
        </Box>
    );
}
