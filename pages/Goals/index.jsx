import { useCallback, useEffect, useState } from "react";
import {
    Box, Button, Card, CardActions, CardContent, Chip, CircularProgress,
    Dialog, DialogActions, DialogContent, DialogTitle, LinearProgress,
    MenuItem, Stack, TextField, Typography
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import CheckCircleOutlineIcon from "@mui/icons-material/CheckCircleOutline";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import FlagOutlinedIcon from "@mui/icons-material/FlagOutlined";
import { useTheme } from "@mui/material/styles";

import { usePatient } from "../../context/PatientContext";
import { useAlert } from "../../hooks/useAlert";
import { useI18n } from "../../src/i18n";
import { completeGoal, createGoal, deleteGoal, getGoals, updateGoal } from "../../services/goalService";

const emptyForm = { nome: "", tipoDado: "personalizado", valorAlvo: "", dataLimite: "" };

export default function Goals() {
    const theme = useTheme();
    const { t } = useI18n();
    const { showAlert } = useAlert();
    const { selectedPatient } = usePatient();
    const [goals, setGoals] = useState([]);
    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);
    const [open, setOpen] = useState(false);
    const [editing, setEditing] = useState(null);
    const [form, setForm] = useState(emptyForm);

    const cpf = selectedPatient?.cpf;
    const canSubmit = form.nome.trim() && form.tipoDado && Number(form.valorAlvo) > 0 && form.dataLimite;

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
            tipoDado: goal.tipoDado,
            valorAlvo: String(goal.valorAlvo ?? ""),
            dataLimite: goal.dataLimite ?? ""
        });
        setOpen(true);
    }

    async function saveGoal() {
        if (!canSubmit) return;
        const payload = { ...form, nome: form.nome.trim(), valorAlvo: Number(form.valorAlvo) };
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

    async function handleComplete(goal) {
        try {
            await completeGoal(goal.id, cpf);
            showAlert("success", t("goals.alerts.completeSuccess"));
            await loadGoals();
        } catch (error) {
            console.error(error);
            showAlert("error", t("goals.alerts.actionError"));
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

    const statusLabel = (status) => t(`goals.status.${status || "em_andamento"}`);
    const typeLabel = (type) => t(`goals.types.${type || "personalizado"}`);

    return (
        <Box sx={{ minHeight: "100vh", p: { xs: 2, md: 4 }, background: theme.vitta.pageBackground }}>
            <Box sx={{ p: { xs: 2.5, md: 3 }, mb: 3, borderRadius: 3, bgcolor: "background.paper", border: "1px solid", borderColor: theme.vitta.border, boxShadow: theme.vitta.shadow, display: "flex", justifyContent: "space-between", alignItems: { xs: "stretch", sm: "center" }, gap: 2, flexDirection: { xs: "column", sm: "row" } }}>
                <Box>
                    <Typography variant="h4" fontWeight={800}>{t("goals.title")}</Typography>
                    <Typography color="text.secondary" mt={1}>{t("goals.description")}</Typography>
                    {selectedPatient?.nome && <Chip size="small" sx={{ mt: 1.5 }} label={`${t("goals.patient")}: ${selectedPatient.nome}`} />}
                </Box>
                <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate} disabled={!cpf}>{t("goals.new")}</Button>
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
                        const progress = Math.max(0, Math.min(Number(goal.progresso) || 0, 100));
                        const done = goal.status?.startsWith("concluido");
                        return <Card key={goal.id} sx={{ borderRadius: 3, border: "1px solid", borderColor: theme.vitta.border, boxShadow: theme.vitta.shadow }}>
                            <CardContent>
                                <Stack direction="row" justifyContent="space-between" gap={1} alignItems="flex-start">
                                    <Typography variant="h6" fontWeight={800}>{goal.nome}</Typography>
                                    <Chip size="small" color={done ? "success" : "primary"} label={statusLabel(goal.status)} />
                                </Stack>
                                <Typography color="text.secondary" mt={1}>{typeLabel(goal.tipoDado)}</Typography>
                                <Stack direction="row" justifyContent="space-between" mt={3}><Typography variant="body2">{t("goals.progress")}</Typography><Typography variant="body2" fontWeight={800}>{progress.toFixed(0)}%</Typography></Stack>
                                <LinearProgress variant="determinate" value={progress} sx={{ mt: 1, height: 9, borderRadius: 9 }} />
                                <Stack direction="row" justifyContent="space-between" mt={2}><Typography color="text.secondary">{t("goals.target")}</Typography><Typography fontWeight={700}>{goal.valorAlvo}</Typography></Stack>
                                <Stack direction="row" justifyContent="space-between"><Typography color="text.secondary">{t("goals.deadline")}</Typography><Typography>{goal.dataLimite ? new Date(`${goal.dataLimite}T00:00:00`).toLocaleDateString() : "—"}</Typography></Stack>
                            </CardContent>
                            <CardActions sx={{ px: 2, pb: 2, flexWrap: "wrap" }}>
                                {!done && <Button size="small" startIcon={<CheckCircleOutlineIcon />} onClick={() => handleComplete(goal)}>{t("goals.complete")}</Button>}
                                <Button size="small" startIcon={<EditOutlinedIcon />} onClick={() => openEdit(goal)}>{t("goals.edit")}</Button>
                                <Button size="small" color="error" startIcon={<DeleteOutlineIcon />} onClick={() => handleDelete(goal)}>{t("goals.delete")}</Button>
                            </CardActions>
                        </Card>;
                    })}
                </Box>
            )}

            <Dialog open={open} onClose={() => !saving && setOpen(false)} fullWidth maxWidth="sm">
                <DialogTitle>{t(editing ? "goals.form.editTitle" : "goals.form.createTitle")}</DialogTitle>
                <DialogContent>
                    <TextField fullWidth margin="normal" label={t("goals.form.name")} value={form.nome} onChange={(e) => setForm({ ...form, nome: e.target.value })} inputProps={{ maxLength: 150 }} />
                    <TextField select fullWidth margin="normal" label={t("goals.form.type")} value={form.tipoDado} onChange={(e) => setForm({ ...form, tipoDado: e.target.value })}>
                        {["sinais_vitais", "habitos", "personalizado"].map(type => <MenuItem key={type} value={type}>{typeLabel(type)}</MenuItem>)}
                    </TextField>
                    <TextField fullWidth margin="normal" type="number" label={t("goals.form.target")} value={form.valorAlvo} onChange={(e) => setForm({ ...form, valorAlvo: e.target.value })} inputProps={{ min: 0, step: "any" }} />
                    <TextField fullWidth margin="normal" type="date" label={t("goals.form.deadline")} value={form.dataLimite} onChange={(e) => setForm({ ...form, dataLimite: e.target.value })} InputLabelProps={{ shrink: true }} />
                </DialogContent>
                <DialogActions><Button onClick={() => setOpen(false)} disabled={saving}>{t("goals.form.cancel")}</Button><Button variant="contained" onClick={saveGoal} disabled={!canSubmit || saving}>{saving ? t("goals.form.saving") : t("goals.form.save")}</Button></DialogActions>
            </Dialog>
        </Box>
    );
}
