import { Box, Paper, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import {
    CartesianGrid,
    Legend,
    Line,
    LineChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";

const lineColors = ["#16a34a", "#0ea5e9", "#d97706", "#7c3aed"];

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

export default function DashboardChart({ category, title, seriesNames, locale, emptyText }) {
    const theme = useTheme();
    const chartData = mergeSeries(category.series);
    const dateFormatter = new Intl.DateTimeFormat(locale, {
        day: "2-digit",
        month: "2-digit"
    });
    const fullDateFormatter = new Intl.DateTimeFormat(locale, {
        day: "2-digit",
        month: "long",
        year: "numeric"
    });

    return (
        <Paper
            sx={{
                p: { xs: 2, md: 3 },
                borderRadius: 3,
                border: "1px solid",
                borderColor: theme.vitta.border,
                boxShadow: theme.vitta.shadow,
                minWidth: 0
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
                        <LineChart data={chartData} margin={{ top: 10, right: 12, left: -12, bottom: 4 }}>
                            <CartesianGrid strokeDasharray="4 4" stroke={theme.palette.divider} />
                            <XAxis
                                dataKey="data"
                                tickFormatter={(value) => dateFormatter.format(new Date(value))}
                                stroke={theme.palette.text.secondary}
                                tick={{ fontSize: 12 }}
                            />
                            <YAxis
                                stroke={theme.palette.text.secondary}
                                tick={{ fontSize: 12 }}
                                domain={["auto", "auto"]}
                            />
                            <Tooltip
                                labelFormatter={(value) => fullDateFormatter.format(new Date(value))}
                                formatter={(value, name) => [
                                    `${value} ${category.unidade}`,
                                    seriesNames[name] || name
                                ]}
                                contentStyle={{
                                    background: theme.palette.background.paper,
                                    border: `1px solid ${theme.vitta.borderStrong}`,
                                    borderRadius: 8,
                                    color: theme.palette.text.primary
                                }}
                            />
                            <Legend formatter={(value) => seriesNames[value] || value} />
                            {category.series.map((serie, index) => (
                                <Line
                                    key={serie.codigo}
                                    type="monotone"
                                    dataKey={serie.codigo}
                                    stroke={lineColors[index % lineColors.length]}
                                    strokeWidth={3}
                                    dot={{ r: 3 }}
                                    activeDot={{ r: 6 }}
                                    connectNulls
                                />
                            ))}
                        </LineChart>
                    </ResponsiveContainer>
                </Box>
            )}
        </Paper>
    );
}
