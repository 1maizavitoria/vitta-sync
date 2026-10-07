import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import {
    Area,
    AreaChart,
    CartesianGrid,
    Legend,
    ReferenceArea,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";

import { useI18n } from "../../src/i18n";
import { useThemeMode } from "../../src/theme/ThemeModeProvider";

const chartColors = ["#3b82f6", "#22d3ee", "#8b5cf6", "#f59e0b", "#ec4899", "#6366f1"];

function mergeSeries(series) {
    const pointsByDate = new Map();

    series.forEach((item) => {
        item.pontos.forEach((point) => {
            const current = pointsByDate.get(point.data) || { data: point.data };
            current[item.codigo] = point.valor;
            pointsByDate.set(point.data, current);
        });
    });

    return Array.from(pointsByDate.values()).sort(
        (first, second) => new Date(first.data) - new Date(second.data)
    );
}

export default function DashboardChart({
    category,
    title,
    seriesNames,
    formatDate,
    formatNumber,
    emptyText,
    baselines = []
}) {
    const theme = useTheme();
    const { t, convertMeasurement, formatUnit } = useI18n();
    const { accessibilityMode } = useThemeMode();
    const isDark = theme.palette.mode === "dark";
    const displayUnit = formatUnit(category.unidade);
    const baselineBands = category.series.flatMap((serie, index) => {
        const signal = { sistolica: "pressao_sistolica", diastolica: "pressao_diastolica" }[serie.codigo] || serie.codigo;
        const baseline = baselines.find((item) => item.sinal === signal);
        if (baseline?.situacao !== "formada" || baseline.limiteInferior == null || baseline.limiteSuperior == null) return [];
        return [{
            code: serie.codigo,
            lower: convertMeasurement(baseline.limiteInferior, category.unidade),
            upper: convertMeasurement(baseline.limiteSuperior, category.unidade),
            color: chartColors[index % chartColors.length]
        }];
    });
    const chartData = mergeSeries(category.series).map((point) => {
        const convertedPoint = { ...point };

        category.series.forEach((serie) => {
            if (convertedPoint[serie.codigo] != null) {
                convertedPoint[serie.codigo] = convertMeasurement(convertedPoint[serie.codigo], category.unidade);
            }
        });

        return convertedPoint;
    });
    const textualSummary = category.series.map((serie) => {
        const values = chartData
            .map((point) => point[serie.codigo])
            .filter((value) => value != null && Number.isFinite(Number(value)))
            .map(Number);

        return {
            code: serie.codigo,
            count: values.length,
            minimum: values.length ? Math.min(...values) : null,
            maximum: values.length ? Math.max(...values) : null,
            latest: values.length ? values.at(-1) : null
        };
    });
    const summaryValue = (value) => value == null
        ? "—"
        : `${formatNumber(value)}${displayUnit ? ` ${displayUnit}` : ""}`;

    return (
        <Paper
            sx={{
                p: { xs: 2, md: 3 },
                borderRadius: 3,
                border: "1px solid",
                borderColor: theme.vitta.border,
                boxShadow: theme.vitta.shadow,
                minWidth: 0,
                backgroundColor: "background.paper"
            }}
        >
            <Box sx={{ mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 800 }}>
                    {title}
                </Typography>
                <Typography variant={accessibilityMode ? "body1" : "body2"} color="text.secondary">
                    {displayUnit}
                </Typography>
            </Box>

            {baselineBands.length > 0 && (
                <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1.5, mb: 2 }}>
                    {baselineBands.map((band) => (
                        <Box key={band.code} sx={{ display: "flex", alignItems: "center", gap: 0.75 }}>
                            <Box aria-hidden="true" sx={{ width: 14, height: 14, flexShrink: 0, border: `1px dashed ${band.color}`, bgcolor: `${band.color}26` }} />
                            <Typography variant="caption" color="text.secondary">
                                {t("dashboard.baseline.range")} · {seriesNames[band.code] || band.code}: {formatNumber(band.lower, { maximumFractionDigits: 2 })}–{formatNumber(band.upper, { maximumFractionDigits: 2 })} {displayUnit}
                            </Typography>
                        </Box>
                    ))}
                </Box>
            )}

            {chartData.length === 0 ? (
                <Box
                    sx={{
                        height: 280,
                        display: "grid",
                        placeItems: "center",
                        color: "text.secondary",
                        textAlign: "center"
                    }}
                >
                    <Typography>{emptyText}</Typography>
                </Box>
            ) : (
                <Box sx={{ width: "100%", height: 300, minWidth: 0 }} aria-label={`${title}. ${displayUnit}`}>
                    <ResponsiveContainer width="100%" height="100%">
                        <AreaChart data={chartData} margin={{ top: 10, right: 12, left: -12, bottom: 4 }} accessibilityLayer>
                            <defs>
                                {category.series.map((serie, index) => {
                                    const color = chartColors[index % chartColors.length];

                                    return (
                                        <linearGradient
                                            key={serie.codigo}
                                            id={`dashboardGradient-${category.codigo}-${serie.codigo}`}
                                            x1="0"
                                            y1="0"
                                            x2="0"
                                            y2="1"
                                        >
                                            <stop offset="5%" stopColor={color} stopOpacity={isDark ? 0.32 : 0.26} />
                                            <stop offset="95%" stopColor={color} stopOpacity={0.03} />
                                        </linearGradient>
                                    );
                                })}
                            </defs>

                            <CartesianGrid
                                strokeDasharray="2 3"
                                stroke={isDark ? "rgba(148, 163, 184, 0.16)" : "rgba(15, 23, 42, 0.12)"}
                                vertical
                            />
                            <XAxis
                                dataKey="data"
                                tickFormatter={(value) => formatDate(value, {
                                    day: "2-digit",
                                    month: "2-digit",
                                    year: undefined
                                })}
                                stroke={isDark ? "rgba(226, 232, 240, 0.5)" : "rgba(71, 85, 105, 0.68)"}
                                tick={{ fontSize: 12, fontWeight: 700 }}
                                axisLine={{ stroke: isDark ? "rgba(148, 163, 184, 0.18)" : "rgba(15, 23, 42, 0.14)" }}
                                tickLine={false}
                            />
                            <YAxis
                                stroke={isDark ? "rgba(226, 232, 240, 0.5)" : "rgba(71, 85, 105, 0.68)"}
                                tick={{ fontSize: 12, fontWeight: 700 }}
                                domain={["auto", "auto"]}
                                axisLine={false}
                                tickLine={false}
                                tickFormatter={(value) => formatNumber(value)}
                            />
                            <Tooltip
                                labelFormatter={(value) => formatDate(value, {
                                    day: "2-digit",
                                    month: "long",
                                    year: "numeric"
                                })}
                                formatter={(value, name) => [
                                    displayUnit ? `${formatNumber(value)} ${displayUnit}` : formatNumber(value),
                                    seriesNames[name] || name
                                ]}
                                contentStyle={{
                                    background: theme.palette.background.paper,
                                    border: `1px solid ${isDark ? "rgba(148, 163, 184, 0.22)" : "rgba(15, 23, 42, 0.12)"}`,
                                    borderRadius: 8,
                                    color: theme.palette.text.primary,
                                    boxShadow: theme.vitta.shadow
                                }}
                                labelStyle={{
                                    color: theme.palette.text.primary,
                                    fontWeight: 800
                                }}
                            />
                            <Legend
                                formatter={(value) => seriesNames[value] || value}
                                iconType="circle"
                                wrapperStyle={{
                                    paddingTop: 12,
                                    fontWeight: 700
                                }}
                            />
                            {baselineBands.map((band) => (
                                <ReferenceArea
                                    key={`baseline-${band.code}`}
                                    y1={band.lower}
                                    y2={band.upper}
                                    ifOverflow="extendDomain"
                                    fill={band.color}
                                    fillOpacity={isDark ? 0.12 : 0.08}
                                    stroke={band.color}
                                    strokeOpacity={0.5}
                                    strokeDasharray="4 4"
                                />
                            ))}
                            {category.series.map((serie, index) => (
                                <Area
                                    key={serie.codigo}
                                    type="monotone"
                                    dataKey={serie.codigo}
                                    stroke={chartColors[index % chartColors.length]}
                                    fill={`url(#dashboardGradient-${category.codigo}-${serie.codigo})`}
                                    strokeWidth={3}
                                    dot={{
                                        r: 3,
                                        strokeWidth: 2,
                                        fill: theme.palette.background.paper,
                                        stroke: chartColors[index % chartColors.length]
                                    }}
                                    activeDot={{
                                        r: 6,
                                        strokeWidth: 2,
                                        fill: theme.palette.background.paper,
                                        stroke: chartColors[index % chartColors.length]
                                    }}
                                    connectNulls
                                />
                            ))}
                        </AreaChart>
                    </ResponsiveContainer>
                </Box>
            )}

            {accessibilityMode && chartData.length > 0 && (
                <Box component="section" aria-label={t("dashboard.chartSummary.title")} sx={{ mt: 3 }}>
                    <Typography sx={{ fontWeight: 800, mb: 1 }}>
                        {t("dashboard.chartSummary.title")}
                    </Typography>
                    <TableContainer sx={{ overflowX: "auto" }}>
                        <Table size="small" aria-label={`${t("dashboard.chartSummary.title")}: ${title}`}>
                            <TableHead>
                                <TableRow>
                                    <TableCell>{t("dashboard.chartSummary.series")}</TableCell>
                                    <TableCell align="right">{t("dashboard.chartSummary.records")}</TableCell>
                                    <TableCell align="right">{t("dashboard.chartSummary.minimum")}</TableCell>
                                    <TableCell align="right">{t("dashboard.chartSummary.maximum")}</TableCell>
                                    <TableCell align="right">{t("dashboard.chartSummary.latest")}</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {textualSummary.map((summary) => (
                                    <TableRow key={summary.code}>
                                        <TableCell component="th" scope="row">{seriesNames[summary.code] || summary.code}</TableCell>
                                        <TableCell align="right">{summary.count}</TableCell>
                                        <TableCell align="right">{summaryValue(summary.minimum)}</TableCell>
                                        <TableCell align="right">{summaryValue(summary.maximum)}</TableCell>
                                        <TableCell align="right">{summaryValue(summary.latest)}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            )}
        </Paper>
    );
}
