import { useCallback, useEffect, useState } from "react";
import {
    Box, Card, CardActions, CardContent, Chip, CircularProgress,
    IconButton, LinearProgress, MenuItem, Stack, Tooltip, Typography
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import FlagOutlinedIcon from "@mui/icons-material/FlagOutlined";
import RemoveRoundedIcon from "@mui/icons-material/RemoveRounded";
import { useTheme } from "@mui/material/styles";
import dayjs from "dayjs";

import ButtonUI from "../../components/ui/Button";
import DatePickerUI from "../../components/ui/DatePicker";
import DialogUI from "../../components/ui/Dialog";
import InputUI from "../../components/ui/Input";
import { usePatient } from "../../context/PatientContext";
import { useAlert } from "../../hooks/useAlert";
import { useI18n } from "../../src/i18n";
import { createGoal, deleteGoal, getGoals, updateGoal, updateGoalValue } from "../../services/goalService";

const indicatorConfig = {
    peso: { tipoDado: "sinais_vitais", unidade: "kg", direcao: "reduzir" },
    horas_sono: { tipoDado: "habitos", unidade: "h", direcao: "aumentar" },
    minutos_exercicio: { tipoDado: "habitos", unidade: "min", direcao: "aumentar" },
    personalizado: { tipoDado: "personalizado", unidade: "", direcao: "aumentar" }
};
const emptyForm = { nome: "", indicador: "personalizado", direcao: "aumentar", unidade: "", valorInicial: "", valorAtual: "", valorAlvo: "", dataLimite: "" };

export default function Goals() {
    const theme = useTheme();
    const { t, formatDate, formatMeasurement, formatPercent } = useI18n();
    const { showAlert } = useAlert();
    const { selectedPatient } = usePatient();
    const isDoctor = localStorage.getItem("tipo")?.toLowerCase() === "saude";
    const [goals, setGoals] = useState([]);
    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);
    const [open, setOpen] = useState(false);
    const [editing, setEditing] = useState(null);
    const [form, setForm] = useState(emptyForm);
    const [manualGoal, setManualGoal] = useState(null);
    const [manualValue, setManualValue] = useState("");

    const cpf = selectedPatient?.cpf;
    const canSubmit = form.nome.trim() && form.indicador && Number(form.valorAlvo) > 0 && form.dataLimite;

    const loadGoals = useCallback(async () => {
        if (!cpf) return setGoals([]);
        try {
            setLoading(true);
            setGoals(await getGoals(cpf));
        } catch (error) {
            console.error(error);
            showAlert("error", t("goals.alerts.loadError"));
        } finally {
            setLoading(false);
        }
    }, [cpf, showAlert, t]);

    useEffect(() => { loadGoals(); }, [loadGoals]);

    function openCreate() {
        setEditing(null);
        setForm(emptyForm);
        setOpen(true);
    }

    function openEdit(goal) {
        setEditing(goal);
        setForm({
            nome: goal.nome,
            indicador: goal.indicador || "personalizado",
            direcao: goal.direcao || "aumentar",
            unidade: goal.unidade || "",
            valorInicial: String(goal.valorInicial ?? ""),
            valorAtual: String(goal.valorAtual ?? ""),
            valorAlvo: String(goal.valorAlvo ?? ""),
            dataLimite: goal.dataLimite ?? ""
        });
        setOpen(true);
    }

    async function saveGoal() {
        if (!canSubmit) return;
        const config = indicatorConfig[form.indicador];
        const payload = {
            ...form,
            tipoDado: config.tipoDado,
            unidade: form.indicador === "personalizado" ? form.unidade.trim() : config.unidade,
            nome: form.nome.trim(),
            valorAlvo: Number(form.valorAlvo),
            valorInicial: form.valorInicial === "" ? null : Number(form.valorInicial),
            valorAtual: form.valorAtual === "" ? null : Number(form.valorAtual)
        };
        try {
            setSaving(true);
            if (editing) await updateGoal(editing.id, cpf, payload);
            else await createGoal(cpf, payload);
            setOpen(false);
            showAlert("success", t(editing ? "goals.alerts.updateSuccess" : "goals.alerts.createSuccess"));
            await loadGoals();
        } catch (error) {
            console.error(error);
            showAlert("error", t("goals.alerts.saveError"));
        } finally {
            setSaving(false);
        }
    }

    async function handleDelete(goal) {
        if (!window.confirm(t("goals.deleteConfirm"))) return;
        try {
            await deleteGoal(goal.id, cpf);
            showAlert("success", t("goals.alerts.deleteSuccess"));
            await loadGoals();
        } catch (error) {
            console.error(error);
            showAlert("error", t("goals.alerts.actionError"));
        }
    }

    async function saveManualValue() {
        if (manualValue === "" || Number.isNaN(Number(manualValue))) return;

        const currentValue = Number(manualGoal.valorAtual) || 0;
        const informedValue = Math.max(0, Number(manualValue));
        const newValue = currentValue + informedValue;

        try {
            setSaving(true);
            await updateGoalValue(manualGoal.id, cpf, newValue);
            setManualGoal(null);
            showAlert("success", t("goals.alerts.valueSuccess"));
            await loadGoals();
        } catch (error) {
            console.error(error);
            showAlert("error", t("goals.alerts.actionError"));
        } finally {
            setSaving(false);
        }
    }

    function adjustManualValue(amount) {
        const currentValue = Number(manualValue) || 0;
        setManualValue(String(Math.max(0, currentValue + amount)));
    }

    const statusLabel = (status) => t(`goals.status.${status || "em_andamento"}`);
    const typeLabel = (type) => t(`goals.types.${type || "personalizado"}`);
    const indicatorLabel = (indicator) => t(`goals.indicators.${indicator || "personalizado"}`);
    const manualCurrentValue = Number(manualGoal?.valorAtual) || 0;
    const manualInformedValue = Math.max(0, Number(manualValue) || 0);
    const manualNewValue = manualCurrentValue + manualInformedValue;
    const actionButtonSx = {
        edit: {
            color: "secondary.main",
            bgcolor: theme.palette.mode === "dark" ? "rgba(14, 165, 233, 0.12)" : "rgba(15, 118, 110, 0.08)",
            border: "1px solid",
            borderColor: theme.palette.mode === "dark" ? "rgba(14, 165, 233, 0.22)" : "rgba(15, 118, 110, 0.16)"
        },
        delete: {
            color: "error.main",
            bgcolor: "rgba(220, 38, 38, 0.08)",
            border: "1px solid rgba(220, 38, 38, 0.14)"
        }
    };

    return (
        <Box sx={{ minHeight: "100vh", p: { xs: 2, md: 4 }, background: theme.vitta.pageBackground }}>
            <Box sx={{ p: { xs: 2.5, md: 3 }, mb: 3, borderRadius: 3, bgcolor: "background.paper", border: "1px solid", borderColor: theme.vitta.border, boxShadow: theme.vitta.shadow, display: "flex", justifyContent: "space-between", alignItems: { xs: "stretch", sm: "center" }, gap: 2, flexDirection: { xs: "column", sm: "row" } }}>
                <Box>
                    <Typography variant="h4" fontWeight={800}>{t("goals.title")}</Typography>
                    <Typography color="text.secondary" mt={1}>{t("goals.description")}</Typography>
                    {selectedPatient?.nome && <Chip size="small" sx={{ mt: 1.5 }} label={`${t("goals.patient")}: ${selectedPatient.nome}`} />}
                </Box>
                <ButtonUI startIcon={<AddIcon />} onClick={openCreate} disabled={!cpf}>{t("goals.new")}</ButtonUI>
            </Box>

            {!cpf ? (
                <Typography color="text.secondary">{t("goals.selectPatient")}</Typography>
            ) : loading ? (
                <Box display="flex" justifyContent="center" py={8}><CircularProgress /></Box>
            ) : goals.length === 0 ? (
                <Box textAlign="center" py={8}><FlagOutlinedIcon sx={{ fontSize: 52, color: "text.disabled" }} /><Typography variant="h6">{t("goals.empty")}</Typography><Typography color="text.secondary">{t("goals.emptyHint")}</Typography></Box>
            ) : (
                <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", md: "repeat(2, minmax(0, 1fr))", xl: "repeat(3, minmax(0, 1fr))" }, gap: 2.5 }}>
                    {goals.map((goal) => {
                        const rawProgress = Math.max(0, Number(goal.progresso) || 0);
                        const progress = Math.min(rawProgress, 100);
                        const done = goal.status?.startsWith("concluido");
                        const failed = goal.status === "nao_atingida";
                        const terminal = done || failed;
                        const limitGoal = goal.indicador === "personalizado" && goal.direcao === "reduzir";
                        const exceeded = limitGoal && Number(goal.valorAtual) > Number(goal.valorAlvo);
                        return <Card key={goal.id} sx={{ borderRadius: 3, border: "1px solid", borderColor: theme.vitta.border, boxShadow: theme.vitta.shadow }}>
                            <CardContent>
                                <Stack direction="row" justifyContent="space-between" gap={1} alignItems="flex-start">
                                    <Typography variant="h6" fontWeight={800}>{goal.nome}</Typography>
                                    <Chip size="small" color={failed ? "error" : done ? "success" : "primary"} label={statusLabel(goal.status)} />
                                </Stack>
                                <Typography color="text.secondary" mt={1}>{indicatorLabel(goal.indicador)} · {typeLabel(goal.tipoDado)}</Typography>
                                <Stack direction="row" justifyContent="space-between" mt={3}><Typography variant="body2">{t(limitGoal ? "goals.limitUsed" : "goals.progress")}</Typography><Typography variant="body2" fontWeight={800}>{formatPercent(rawProgress)}%</Typography></Stack>
                                <LinearProgress color={exceeded || failed ? "error" : done ? "success" : "primary"} variant="determinate" value={progress} sx={{ mt: 1, height: 9, borderRadius: 9 }} />
                                {exceeded && <Typography variant="body2" color="error.main" fontWeight={700} mt={1}>{t("goals.limitExceeded")}: {formatMeasurement(Number(goal.valorAtual) - Number(goal.valorAlvo), goal.unidade, { maximumFractionDigits: 1 })}</Typography>}
                                <Stack direction="row" justifyContent="space-between" mt={2}><Typography color="text.secondary">{t("goals.current")}</Typography><Typography fontWeight={700}>{goal.valorAtual == null ? "—" : formatMeasurement(goal.valorAtual, goal.unidade)}</Typography></Stack>
                                <Stack direction="row" justifyContent="space-between"><Typography color="text.secondary">{t("goals.target")}</Typography><Typography fontWeight={700}>{formatMeasurement(goal.valorAlvo, goal.unidade)}</Typography></Stack>
                                <Stack direction="row" justifyContent="space-between"><Typography color="text.secondary">{t("goals.deadline")}</Typography><Typography>{goal.dataLimite ? formatDate(goal.dataLimite) : "—"}</Typography></Stack>
                            </CardContent>
                            <CardActions sx={{ px: 2, pb: 2, gap: 1, flexWrap: "wrap" }}>
                                {!isDoctor && !terminal && goal.indicador === "personalizado" && <ButtonUI sx={{ py: 0.75, px: 1.5 }} onClick={() => { setManualGoal(goal); setManualValue("0"); }}>{t("goals.updateValue")}</ButtonUI>}
                                <Tooltip title={t("goals.edit")}><IconButton aria-label={t("goals.edit")} sx={actionButtonSx.edit} onClick={() => openEdit(goal)}><EditOutlinedIcon fontSize="small" /></IconButton></Tooltip>
                                {!isDoctor && <Tooltip title={t("goals.delete")}><IconButton aria-label={t("goals.delete")} sx={actionButtonSx.delete} onClick={() => handleDelete(goal)}><DeleteOutlineIcon fontSize="small" /></IconButton></Tooltip>}
                            </CardActions>
                        </Card>;
                    })}
                </Box>
            )}

            <DialogUI
                open={open}
                onClose={() => setOpen(false)}
                disabledClose={saving}
                title={t(editing ? "goals.form.editTitle" : "goals.form.createTitle")}
                onConfirm={saveGoal} disabledConfirm={!canSubmit || saving}
                confirmText={saving ? t("goals.form.saving") : t("goals.form.save")}
                cancelText={t("goals.form.cancel")}
            >

                <InputUI
                    label={t("goals.form.name")}
                    value={form.nome}
                    onChange={(e) => setForm({ ...form, nome: e.target.value })}
                    limit={150}
                />

                <InputUI
                    select label={t("goals.form.indicator")}
                    value={form.indicador}
                    onChange={(e) => {
                        const indicador = e.target.value;
                        const config = indicatorConfig[indicador];
                        setForm({ ...form, indicador, direcao: config.direcao, unidade: config.unidade, valorInicial: "", valorAtual: "" });
                    }}
                >
                    {Object.keys(indicatorConfig).map(indicator => <MenuItem key={indicator} value={indicator}>{indicatorLabel(indicator)}</MenuItem>)}
                </InputUI>

                <InputUI
                    select
                    label={t("goals.form.direction")}
                    value={form.direcao} onChange={(e) => setForm({ ...form, direcao: e.target.value })}
                >
                    <MenuItem
                        value="aumentar"
                    >
                        {t(form.indicador === "personalizado" ? "goals.directions.reachMinimum" : "goals.directions.aumentar")}
                    </MenuItem>
                    <MenuItem
                        value="reduzir"
                    >
                        {t(form.indicador === "personalizado" ? "goals.directions.doNotExceed" : "goals.directions.reduzir")}
                    </MenuItem>
                </InputUI>

                {form.indicador === "personalizado" &&
                    <InputUI
                        label={t("goals.form.unit")}
                        value={form.unidade}
                        onChange={(e) => setForm({ ...form, unidade: e.target.value })}
                        limit={30} />}

                <InputUI
                    type="number"
                    label={t("goals.form.target")}
                    value={form.valorAlvo}
                    onChange={(e) => setForm({ ...form, valorAlvo: e.target.value })}
                    slotProps={{ htmlInput: { min: 0, step: "any" } }}
                />
                <DatePickerUI
                    label={t("goals.form.deadline")}
                    value={form.dataLimite}
                    onChange={(value) => setForm({ ...form, dataLimite: value || "" })}
                    minDate={dayjs().startOf("day")}
                    dateLimit={dayjs().add(50, "year")}
                />
            </DialogUI>

            <DialogUI
                open={Boolean(manualGoal)}
                onClose={() => setManualGoal(null)}
                disabledClose={saving}
                title={t("goals.manualTitle")}
                onConfirm={saveManualValue}
                disabledConfirm={saving || manualValue === "" || Number(manualValue) <= 0}
                confirmText={t("goals.form.save")}
                cancelText={t("goals.form.cancel")}>

                <Stack spacing={0.75} sx={{ mt: 1, mb: 1.5 }}>
                    <Stack direction="row" justifyContent="space-between" gap={2}>
                        <Typography color="text.secondary">{t("goals.current")}</Typography>
                        <Typography fontWeight={700}>{formatMeasurement(manualCurrentValue, manualGoal?.unidade)}</Typography>
                    </Stack>
                    <Stack direction="row" justifyContent="space-between" gap={2}>
                        <Typography color="text.secondary">{t("goals.manualNewValue")}</Typography>
                        <Typography color="primary.main" fontWeight={800}>{formatMeasurement(manualNewValue, manualGoal?.unidade)}</Typography>
                    </Stack>
                </Stack>

                <Stack direction="row" alignItems="center" spacing={1.5} sx={{ mt: 5 }}>
                    <IconButton
                        aria-label={t("goals.directions.reduzir")}
                        onClick={() => adjustManualValue(-1)}
                        disabled={saving || Number(manualValue) <= 0}
                        sx={{
                            color: "error.main",
                            bgcolor: "rgba(220, 38, 38, 0.08)",
                            border: "1px solid rgba(220, 38, 38, 0.14)",
                            "&:hover": { bgcolor: "rgba(220, 38, 38, 0.14)" }
                        }}
                    >
                        <RemoveRoundedIcon />
                    </IconButton>

                    <InputUI
                        autoFocus
                        type="number"
                        label={t("goals.manualAmount")}
                        value={manualValue}
                        onChange={(e) => setManualValue(e.target.value === "" ? "" : String(Math.max(0, Number(e.target.value))))}
                        slotProps={{ htmlInput: { min: 0, step: 1 } }}
                        sx={{ flex: 1, mt: 0, mb: 0 }}
                    />

                    <IconButton
                        aria-label={t("goals.directions.aumentar")}
                        onClick={() => adjustManualValue(1)}
                        disabled={saving}
                        sx={{
                            color: "#ffffff",
                            bgcolor: "primary.main",
                            border: "1px solid",
                            borderColor: theme.vitta.borderStrong,
                            "&:hover": { bgcolor: "primary.dark" }
                        }}
                    >
                        <AddRoundedIcon />
                    </IconButton>
                </Stack>
            </DialogUI>
        </Box>
    );
}
