import { useEffect, useRef, useState } from "react";
import {
    Alert, Box, Button, Checkbox, Chip, CircularProgress, Divider, FormControl,
    FormControlLabel, FormGroup, FormLabel, MenuItem, Paper, Radio, RadioGroup,
    Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import DownloadOutlinedIcon from "@mui/icons-material/DownloadOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import DescriptionOutlinedIcon from "@mui/icons-material/DescriptionOutlined";
import { CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import dayjs from "dayjs";
import { useI18n } from "../../src/i18n";
import { previewReport, downloadReport } from "../../services/reportService";
import DatePickerUI from "./DatePicker";

const categories = ["SINAIS", "SINTOMAS", "HABITOS"];
const presets = ["7", "30", "all", "custom"];

function initialFilters() {
    return {
        formato: "PDF", categorias: ["SINAIS", "SINTOMAS"],
        dataInicio: dayjs().subtract(6, "day").format("YYYY-MM-DD"),
        dataFim: dayjs().format("YYYY-MM-DD"),
    };
}

function validDate(value) {
    return !value || (/^\d{4}-\d{2}-\d{2}$/.test(value)
        && dayjs(value).isValid() && dayjs(value).format("YYYY-MM-DD") === value
        && Number(value.slice(0, 4)) >= 1);
}

function chartPoints(points) {
    const result = [];
    points.forEach((point, index) => {
        const previous = points[index - 1];
        if (previous && dayjs(point.data).diff(dayjs(previous.data), "day") > 1) {
            result.push({ timestamp: dayjs(previous.data).add(1, "day").valueOf(), media: null });
        }
        result.push({ timestamp: dayjs(point.data).valueOf(), media: point.media });
    });
    return result;
}

function ReportPreview({ report }) {
    const { t, formatDate, formatDateTime, formatNumber } = useI18n();
    const theme = useTheme();
    const tr = (key) => t(`reports.export.${key}`);
    const number = (value) => value == null ? "—" : formatNumber(value, { maximumFractionDigits: 1 });
    const indicators = report.resumo?.sinaisVitais || [];
    const chartIndicators = indicators.filter((item) => item.evolucaoDiaria?.length);
    const [metric, setMetric] = useState("fcBpm");
    const active = chartIndicators.find((item) => item.chave === metric) || chartIndicators[0];
    const habits = report.resumo?.habitos;
    const selected = report.categorias || [];
    const period = !report.dataInicio && !report.dataFim ? tr("presets.all")
        : `${report.dataInicio ? formatDate(report.dataInicio) : tr("noStart")} — ${report.dataFim ? formatDate(report.dataFim) : tr("noEnd")}`;

    return (
        <Box sx={{ mt: 3 }} aria-live="polite">
            <Divider sx={{ mb: 3 }} />
            <Typography variant="h6" sx={{ fontWeight: 800 }}>{tr("previewTitle")}</Typography>
            <Typography sx={{ fontWeight: 700, mt: 1 }}>{report.paciente?.nome}</Typography>
            <Typography variant="body2" color="text.secondary">{period}</Typography>
            <Typography variant="body2" color="text.secondary">
                {tr("issuedAt")}: {formatDateTime(report.dataEmissao)}
            </Typography>
            {report.semRegistros ? <Alert severity="info" sx={{ mt: 2 }}>{tr("empty")}</Alert> : (
                <>
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{tr("previewHint")}</Typography>
                    {selected.includes("SINAIS") && (
                        <Box sx={{ mt: 3 }}>
                            <Typography sx={{ fontWeight: 800, mb: 1 }}>{tr("categories.SINAIS")}</Typography>
                            <TableContainer tabIndex={0} aria-label={tr("categories.SINAIS")}>
                                <Table size="small" sx={{ minWidth: 560 }}>
                                    <TableHead><TableRow>
                                        {["indicator", "mean", "min", "max", "count"].map((key, index) => (
                                            <TableCell key={key} align={index ? "right" : "left"}>{tr(key)}</TableCell>
                                        ))}
                                    </TableRow></TableHead>
                                    <TableBody>{indicators.map((item) => (
                                        <TableRow key={item.chave}>
                                            <TableCell component="th" scope="row">{tr(`metrics.${item.chave}`)} ({item.unidade})</TableCell>
                                            <TableCell align="right">{number(item.media)}</TableCell>
                                            <TableCell align="right">{number(item.minimo)}</TableCell>
                                            <TableCell align="right">{number(item.maximo)}</TableCell>
                                            <TableCell align="right">{number(item.quantidade)}</TableCell>
                                        </TableRow>
                                    ))}</TableBody>
                                </Table>
                            </TableContainer>
                            {active && (
                                <Box sx={{ mt: 3 }}>
                                    <TextField select size="small" label={tr("evolution")} value={active.chave}
                                        onChange={(event) => setMetric(event.target.value)} sx={{ width: { xs: "100%", sm: 300 } }}>
                                        {chartIndicators.map((item) => <MenuItem key={item.chave} value={item.chave}>{tr(`metrics.${item.chave}`)}</MenuItem>)}
                                    </TextField>
                                    <Typography variant="caption" display="block" color="text.secondary" sx={{ my: 1 }}>
                                        {tr("dailyMeans")} ({active.unidade})
                                    </Typography>
                                    <Box sx={{ height: 250, width: "100%", minWidth: 0 }}>
                                        <ResponsiveContainer width="100%" height="100%">
                                            <LineChart data={chartPoints(active.evolucaoDiaria)} margin={{ top: 10, right: 20, bottom: 5, left: 0 }} accessibilityLayer>
                                                <CartesianGrid stroke={theme.palette.divider} strokeDasharray="3 3" />
                                                <XAxis dataKey="timestamp" type="number" scale="time" domain={["dataMin", "dataMax"]}
                                                    tickFormatter={(value) => formatDate(new Date(value), { year: undefined })} minTickGap={35} />
                                                <YAxis domain={["auto", "auto"]} tickFormatter={number} width={50} />
                                                <Tooltip labelFormatter={(value) => formatDate(new Date(value))}
                                                    formatter={(value) => [`${number(value)} ${active.unidade}`, tr("mean")]}
                                                    contentStyle={{ backgroundColor: theme.palette.background.paper, borderColor: theme.palette.divider, color: theme.palette.text.primary }} />
                                                <Line type="linear" dataKey="media" stroke={theme.palette.primary.main} strokeWidth={2}
                                                    dot={{ r: 3 }} connectNulls={false} isAnimationActive={false} />
                                            </LineChart>
                                        </ResponsiveContainer>
                                    </Box>
                                </Box>
                            )}
                        </Box>
                    )}
                    {selected.includes("SINTOMAS") && (
                        <Box sx={{ mt: 3 }}>
                            <Typography sx={{ fontWeight: 800, mb: 1 }}>{tr("categories.SINTOMAS")}</Typography>
                            {!report.sintomas?.length ? <Typography color="text.secondary">{tr("emptyCategory")}</Typography> : (
                                <TableContainer tabIndex={0} aria-label={tr("categories.SINTOMAS")} sx={{ maxHeight: 300 }}>
                                    <Table size="small" stickyHeader>
                                        <TableHead><TableRow>
                                            <TableCell>{tr("occurrence")}</TableCell><TableCell>{tr("symptom")}</TableCell><TableCell align="right">{tr("pain")}</TableCell>
                                        </TableRow></TableHead>
                                        <TableBody>{report.sintomas.map((item) => <TableRow key={item.id}>
                                            <TableCell sx={{ whiteSpace: "nowrap" }}>{item.dataReferencia ? formatDate(item.dataReferencia) : "—"}</TableCell>
                                            <TableCell sx={{ overflowWrap: "anywhere", whiteSpace: "pre-wrap" }}>{item.sintoma}</TableCell>
                                            <TableCell align="right">{item.intensidadeDor == null ? "—" : `${item.intensidadeDor}/10`}</TableCell>
                                        </TableRow>)}</TableBody>
                                    </Table>
                                </TableContainer>
                            )}
                        </Box>
                    )}
                    {selected.includes("HABITOS") && habits && (
                        <Box sx={{ mt: 3 }}>
                            <Typography sx={{ fontWeight: 800, mb: 1 }}>{tr("categories.HABITOS")}</Typography>
                            <Stack direction={{ xs: "column", sm: "row" }} spacing={3}>
                                <Box><Typography>{tr("sleep")}: <strong>{number(habits.mediaHorasSono)} h</strong></Typography>
                                    <Typography variant="body2" color="text.secondary">{tr("recordedDays")}: {number(habits.diasComSono)}</Typography></Box>
                                <Box><Typography>{tr("exercise")}: <strong>{number(habits.totalMinutosExercicio)} min</strong></Typography>
                                    <Typography variant="body2" color="text.secondary">{tr("recordedDays")}: {number(habits.diasComExercicio)}</Typography></Box>
                            </Stack>
                            <Typography variant="caption" color="text.secondary">{tr("habitsHint")}</Typography>
                        </Box>
                    )}
                    <Typography variant="caption" display="block" color="text.secondary" sx={{ mt: 2 }}>{tr("missingHint")}</Typography>
                </>
            )}
        </Box>
    );
}

export default function ReportExport({ cpf }) {
    const { t } = useI18n();
    const theme = useTheme();
    const tr = (key) => t(`reports.export.${key}`);
    const [filters, setFilters] = useState(initialFilters);
    const [preset, setPreset] = useState("7");
    const [preview, setPreview] = useState(null);
    const [busy, setBusy] = useState(null);
    const [notice, setNotice] = useState(null);
    const request = useRef(null);
    useEffect(() => () => request.current?.abort(), []);

    const invalidDates = !validDate(filters.dataInicio) || !validDate(filters.dataFim)
        || (filters.dataInicio && filters.dataFim && filters.dataInicio > filters.dataFim);
    const invalid = !filters.categorias.length || invalidDates;

    function updateFilters(values) {
        setFilters((current) => ({ ...current, ...values }));
        setPreview(null);
        setNotice(null);
    }

    function changePreset(value) {
        setPreset(value);
        if (value === "all") updateFilters({ dataInicio: null, dataFim: null });
        else if (value !== "custom") updateFilters({
            dataInicio: dayjs().subtract(Number(value) - 1, "day").format("YYYY-MM-DD"),
            dataFim: dayjs().format("YYYY-MM-DD"),
        });
    }

    async function run(action) {
        if (!cpf || invalid || request.current) return;
        const controller = new AbortController();
        request.current = controller;
        setBusy(action);
        setNotice(null);
        if (action === "preview") setPreview(null);
        try {
            if (action === "preview") {
                const data = await previewReport(cpf, filters, controller.signal);
                if (!controller.signal.aborted) setPreview(data);
            } else {
                const { blob, filename } = await downloadReport(cpf, filters, controller.signal);
                if (controller.signal.aborted) return;
                const url = URL.createObjectURL(blob);
                const link = document.createElement("a");
                link.href = url;
                link.download = filename;
                document.body.appendChild(link);
                try { link.click(); } finally {
                    link.remove();
                    setTimeout(() => URL.revokeObjectURL(url), 60000);
                }
                setNotice({ severity: "success", key: "downloadStarted" });
            }
        } catch (error) {
            if (controller.signal.aborted) return;
            const status = error.response?.status;
            if (status === 403 || status === 404 || status === 422) setPreview(null);
            const key = status === 403 ? "forbidden" : status === 404 ? "patientMissing"
                : status === 422 ? "empty" : status === 400 ? "invalidFilters" : "requestError";
            setNotice({ severity: status === 422 ? "info" : "error", key });
        } finally {
            if (!controller.signal.aborted) setBusy(null);
            if (request.current === controller) request.current = null;
        }
    }

    return (
        <Paper component="section" aria-labelledby="report-export-title" elevation={0}
            sx={{ p: { xs: 2, md: 3 }, borderRadius: 3, border: "1px solid", borderColor: theme.vitta.border,
                boxShadow: theme.vitta.shadow, minWidth: 0, overflow: "hidden" }}>
            <Stack direction="row" spacing={1.5} alignItems="center" sx={{ mb: 2 }}>
                <DescriptionOutlinedIcon color="primary" />
                <Box><Typography id="report-export-title" variant="h6" sx={{ fontWeight: 800 }}>{tr("title")}</Typography>
                    <Typography variant="body2" color="text.secondary">{tr("description")}</Typography></Box>
            </Stack>
            {!cpf ? <Alert severity="info">{tr("selectPatient")}</Alert> : (
                <>
                    <Box component="fieldset" disabled={!!busy} sx={{ border: 0, p: 0, m: 0, minWidth: 0 }}>
                        <Typography sx={{ fontWeight: 700, mb: 1 }}>{tr("period")}</Typography>
                        <Stack direction="row" useFlexGap flexWrap="wrap" gap={1}>
                            {presets.map((item) => <Chip key={item} label={tr(`presets.${item}`)} disabled={!!busy}
                                color={preset === item ? "primary" : "default"} variant={preset === item ? "filled" : "outlined"}
                                onClick={() => changePreset(item)} aria-pressed={preset === item} />)}
                        </Stack>
                        <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" }, gap: 2, mt: 2 }}>
                            <DatePickerUI label={tr("start")} value={filters.dataInicio} disabled={!!busy || preset === "all"}
                                error={!!invalidDates} onChange={(value) => { setPreset("custom"); updateFilters({ dataInicio: value }); }} />
                            <DatePickerUI label={tr("end")} value={filters.dataFim} disabled={!!busy || preset === "all"}
                                error={!!invalidDates} onChange={(value) => { setPreset("custom"); updateFilters({ dataFim: value }); }} />
                        </Box>
                        <Typography variant="caption" color={invalidDates ? "error" : "text.secondary"}>{tr(invalidDates ? "invalidDates" : "datesHint")}</Typography>
                        <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", md: "1fr 1fr" }, gap: 3, mt: 3 }}>
                            <FormControl component="fieldset" error={!filters.categorias.length} disabled={!!busy}>
                                <FormLabel component="legend">{tr("include")}</FormLabel>
                                <FormGroup row>{categories.map((category) => <FormControlLabel key={category} label={tr(`categories.${category}`)}
                                    control={<Checkbox checked={filters.categorias.includes(category)} onChange={(event) => updateFilters({
                                        categorias: event.target.checked ? [...filters.categorias, category] : filters.categorias.filter((item) => item !== category),
                                    })} />} />)}</FormGroup>
                                {!filters.categorias.length && <Typography variant="caption" color="error">{tr("chooseCategory")}</Typography>}
                            </FormControl>
                            <FormControl component="fieldset" disabled={!!busy}>
                                <FormLabel component="legend">{tr("format")}</FormLabel>
                                <RadioGroup row value={filters.formato} onChange={(event) => updateFilters({ formato: event.target.value })}>
                                    <FormControlLabel value="PDF" control={<Radio />} label="PDF" />
                                    <FormControlLabel value="CSV" control={<Radio />} label="CSV" />
                                </RadioGroup>
                                <Typography variant="caption" color="text.secondary">{tr(filters.formato === "PDF" ? "pdfHint" : "csvHint")}</Typography>
                            </FormControl>
                        </Box>
                    </Box>
                    {notice && <Alert severity={notice.severity} sx={{ mt: 2 }}>{tr(notice.key)}</Alert>}
                    <Stack direction={{ xs: "column", sm: "row" }} justifyContent="flex-end" spacing={1.5} sx={{ mt: 3 }}>
                        <Button variant="outlined" disabled={!!busy || !!invalid} onClick={() => run("preview")}
                            startIcon={busy === "preview" ? <CircularProgress size={18} color="inherit" /> : <VisibilityOutlinedIcon />}>
                            {tr(busy === "preview" ? "loadingPreview" : "preview")}
                        </Button>
                        <Button variant="contained" disabled={!!busy || !!invalid || preview?.semRegistros} onClick={() => run("download")}
                            startIcon={busy === "download" ? <CircularProgress size={18} color="inherit" /> : <DownloadOutlinedIcon />}>
                            {tr(busy === "download" ? "generating" : "download")}
                        </Button>
                    </Stack>
                    {preview && <ReportPreview report={preview} />}
                </>
            )}
        </Paper>
    );
}
