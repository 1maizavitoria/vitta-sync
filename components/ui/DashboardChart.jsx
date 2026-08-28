import { Box, Paper, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import {
    Area,
    AreaChart,
    CartesianGrid,
    Legend,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";

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
    formatMeasurement,
    formatNumber,
    emptyText
}) {
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const chartData = mergeSeries(category.series);

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
                <Typography variant="body2" color="text.secondary">
                    {category.unidade}
                </Typography>
            </Box>

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
                <Box sx={{ width: "100%", height: 300, minWidth: 0 }}>
                    <ResponsiveContainer width="100%" height="100%">
                        <AreaChart data={chartData} margin={{ top: 10, right: 12, left: -12, bottom: 4 }}>
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
                                    formatMeasurement(value, category.unidade),
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
        </Paper>
    );
}
