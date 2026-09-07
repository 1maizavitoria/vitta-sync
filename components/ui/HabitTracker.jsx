import { useEffect, useState } from "react";

import { Box, Chip, FormControl, IconButton, InputLabel, MenuItem, Paper, Select, Tooltip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import AddIcon from "@mui/icons-material/Add";
import BedtimeIcon from "@mui/icons-material/Bedtime";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import { CalendarIcon } from "@mui/x-date-pickers";

import { usePatient } from "../../context/PatientContext";
import { useAlert } from "../../hooks/useAlert";
import { editHabits, getHabits, registerHabits } from "../../services/habitService";
import { getMedicoStyle, getNomeFuncao, getResponsavelStyle } from "../../utils/validators/userFunction";
import HabitCard from "../ui/cards/HabitCard";
import { useI18n } from "../../src/i18n";

const emptyInputs = {
    timeExercise: "",
    timeSleep: "",
    date: null,
    channel: "email",
};

function isEmptyInput(value) {
    return value == null || String(value).trim() === "";
}

const iconButtonSx = {
    add: {
        color: "#ffffff",
        bgcolor: "#16a34a",
        border: "1px solid rgba(22, 163, 74, 0.2)",
        "&:hover": { bgcolor: "#15803d" }
    },
    edit: {
        color: "#0f766e",
        bgcolor: "rgba(15, 118, 110, 0.08)",
        border: "1px solid rgba(15, 118, 110, 0.16)"
    },
    cancel: {
        color: "#dc2626",
        bgcolor: "rgba(220, 38, 38, 0.08)",
        border: "1px solid rgba(220, 38, 38, 0.14)"
    },
    save: {
        color: "#ffffff",
        bgcolor: "#16a34a",
        border: "1px solid rgba(22, 163, 74, 0.2)",
        "&:hover": { bgcolor: "#15803d" }
    }
};

export function HabitTracker() {
    const { selectedPatient } = usePatient();
    const { showAlert } = useAlert();
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const { t, formatDate, formatDateTime, formatNumber } = useI18n();

    const [editing, setEditing] = useState(false);
    const [addHabit, setAddHabit] = useState(false);
    const [error, setError] = useState(false);
    const [errorEx, setErrorEx] = useState(false);
    const [errorSl, setErrorSl] = useState(false);
    const [errorDate, setErrorDate] = useState(false);
    const [habits, setHabit] = useState([]);
    const [habitInputs, setHabitInputs] = useState(emptyInputs);
    const [initialInputs, setInitialInputs] = useState(emptyInputs);

    const userType = localStorage.getItem("tipo");
    const canEdit = userType !== "saude";
    const isFormOpen = addHabit || editing;
    const CPF = selectedPatient?.cpf;
    const actionButtonSx = {
        add: {
            color: "#ffffff",
            bgcolor: "primary.main",
            border: "1px solid",
            borderColor: theme.vitta.borderStrong,
            "&:hover": { bgcolor: "primary.dark" }
        },
        edit: {
            color: "secondary.main",
            bgcolor: isDark ? "rgba(14, 165, 233, 0.12)" : "rgba(15, 118, 110, 0.08)",
            border: "1px solid",
            borderColor: isDark ? "rgba(14, 165, 233, 0.22)" : "rgba(15, 118, 110, 0.16)"
        },
        cancel: iconButtonSx.cancel,
        save: {
            color: "#ffffff",
            bgcolor: "primary.main",
            border: "1px solid",
            borderColor: theme.vitta.borderStrong,
            "&:hover": { bgcolor: "primary.dark" }
        }
    };

    const lastHabit = habits.reduce((latest, current) => {
        if (!latest) return current;

        return new Date(current.dataRegistro) > new Date(latest.dataRegistro)
            ? current
            : latest;
    }, null);

    let style = null;

    if (lastHabit?.usuarioTipo === "responsavel") {
        style = getResponsavelStyle(lastHabit.usuarioFuncao, t);
    } else if (lastHabit?.usuarioTipo === "saude") {
        style = getMedicoStyle(lastHabit.usuarioFuncao, t);
    }

    function updateInput(field, value) {
        setHabitInputs((prev) => ({
            ...prev,
            [field]: value
        }));
    }

    function clearErrors() {
        setError(false);
        setErrorEx(false);
        setErrorSl(false);
        setErrorDate(false);
    }

    function closeForm() {
        setEditing(false);
        setAddHabit(false);
        clearErrors();
    }

    function handleClearInputs() {
        setHabitInputs(emptyInputs);
        setInitialInputs(emptyInputs);
    }

    function handleDataEditing() {
        if (!lastHabit) return;

        const inputs = {
            timeExercise: lastHabit.minutosExercicio,
            timeSleep: lastHabit.horasSono,
            date: lastHabit.dataReferencia,
            channel: lastHabit.canal || "email"
        };
        setHabitInputs(inputs);
        setInitialInputs(inputs);
    }

    function isValidMinutes(value) {
        const num = Number(value);
        return !isNaN(num) && num >= 0 && num <= 1440;
    }

    function isValidHours(value) {
        const num = Number(value);
        return !isNaN(num) && num >= 0 && num <= 24;
    }

    function canRegister() {
        if (Object.values(habitInputs).some(isEmptyInput)) {
            showAlert("error", t("healthTracker.habits.fillAll"));
            setError(true);
            return false;
        }

        if (!isValidMinutes(habitInputs.timeExercise)) {
            showAlert("error", t("healthTracker.habits.invalidExercise"));
            setErrorEx(true);
            return false;
        }

        if (!isValidHours(habitInputs.timeSleep)) {
            showAlert("error", t("healthTracker.habits.invalidSleep"));
            setErrorSl(true);
            return false;
        }

        clearErrors();
        return true;
    }

    async function refreshHabits() {
        if (!CPF) return;

        const updatedHabits = await getHabits(CPF);
        setHabit(updatedHabits);
    }

    async function handleRegister() {
        if (!canRegister()) return;

        const data = {
            horasSono: habitInputs.timeSleep,
            minutosExercicio: habitInputs.timeExercise,
            dataReferencia: habitInputs.date,
            canal: habitInputs.channel,
        };

        try {
            if (addHabit) {
                await registerHabits(CPF, data);
                showAlert("success", t("healthTracker.habits.registered"));
            }

            if (editing) {
                await editHabits(lastHabit.id, CPF, data);
                showAlert("success", t("healthTracker.habits.edited"));
            }

            await refreshHabits();
            handleClearInputs();
            closeForm();
        } catch (registerError) {
            console.error("Erro ao salvar hábitos:", registerError);
            showAlert("error", t("healthTracker.habits.saveError"));
        }
    }

    useEffect(() => {
        if (!selectedPatient) return;

        async function fetchHabits() {
            try {
                const data = await getHabits(selectedPatient.cpf);
                setHabit(data);
            } catch (fetchError) {
                console.error("Erro ao buscar hábitos:", fetchError);
                setHabit([]);
            }
        }

        fetchHabits();
    }, [selectedPatient]);

    useEffect(() => {
        const hasUnsavedChanges = isFormOpen && Object.keys(emptyInputs).some(
            (field) => String(habitInputs[field] ?? "") !== String(initialInputs[field] ?? "")
        );

        if (!hasUnsavedChanges) return;

        const handleBeforeUnload = (event) => {
            event.preventDefault();
            event.returnValue = "";
        };

        window.addEventListener("beforeunload", handleBeforeUnload);

        return () => {
            window.removeEventListener("beforeunload", handleBeforeUnload);
        };
    }, [habitInputs, initialInputs, isFormOpen]);

    return (
        <Paper
            elevation={0}
            sx={{
                p: 0,
                background: "transparent",
                boxShadow: "none",
                minWidth: 0
            }}
        >
            <Box
                display="flex"
                alignItems="center"
                justifyContent="space-between"
                gap={2}
                mb={2.5}
                sx={{ minWidth: 0 }}
            >
                <Box sx={{ minWidth: 0 }}>
                    <Typography
                        sx={{
                            fontWeight: 800,
                            color: "text.primary",
                            fontSize: "1.05rem",
                            overflowWrap: "anywhere"
                        }}
                    >
                        {t("healthTracker.habits.title")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            fontSize: "0.88rem",
                            mt: 0.25,
                            overflowWrap: "anywhere"
                        }}
                    >
                        {t("healthTracker.habits.description")}
                    </Typography>
                </Box>

                {isFormOpen ? (
                    <Box display="flex" gap={1} flexShrink={0}>
                        <Tooltip title={t("healthTracker.common.cancel")}>
                            <IconButton onClick={closeForm} sx={actionButtonSx.cancel}>
                                <CloseIcon />
                            </IconButton>
                        </Tooltip>

                        <Tooltip title={t("healthTracker.common.save")}>
                            <IconButton onClick={handleRegister} sx={actionButtonSx.save}>
                                <CheckIcon />
                            </IconButton>
                        </Tooltip>
                    </Box>
                ) : (
                    <Box display="flex" alignItems="center" gap={1} flexShrink={0}>
                        {canEdit && (
                            <Tooltip title={t("healthTracker.habits.add")}>
                                <IconButton
                                    onClick={() => {
                                        setAddHabit(true);
                                        handleClearInputs();
                                    }}
                                    sx={actionButtonSx.add}
                                >
                                    <AddIcon />
                                </IconButton>
                            </Tooltip>
                        )}

                        {canEdit && (
                            <Tooltip title={t("healthTracker.habits.edit")}>
                                <span>
                                    <IconButton
                                        onClick={() => {
                                            setEditing(true);
                                            handleDataEditing();
                                        }}
                                        disabled={!lastHabit}
                                        sx={actionButtonSx.edit}
                                    >
                                        <EditOutlinedIcon />
                                    </IconButton>
                                </span>
                            </Tooltip>
                        )}
                    </Box>
                )}
            </Box>

            <Box sx={{ display: "grid", gridTemplateColumns: { xs: "minmax(0, 1fr)", sm: "repeat(2, minmax(0, 1fr))", lg: "repeat(3, minmax(0, 1fr))" }, gap: 2.5 }}>
                <Box sx={{ minWidth: 0 }}>
                    <HabitCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<BedtimeIcon />}
                        title={t("healthTracker.habits.sleepTime")}
                        error={(error && isEmptyInput(habitInputs.timeSleep)) || errorSl}
                        type="number"
                        value={lastHabit ? formatNumber(lastHabit.horasSono) : t("healthTracker.common.notAvailable")}
                        unit={t("healthTracker.common.hours")}
                        date={lastHabit ? formatDateTime(lastHabit.dataRegistro) : t("healthTracker.common.notAvailable")}
                        inputValue={habitInputs.timeSleep}
                        onInputChange={(event) => {
                            updateInput("timeSleep", event.target.value);
                            setErrorSl(false);
                        }}
                        userName={lastHabit?.usuarioNome}
                        userFunction={getNomeFuncao(lastHabit?.usuarioFuncao, t)}
                    />
                </Box>

                <Box sx={{ minWidth: 0 }}>
                    <HabitCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<FitnessCenterIcon />}
                        title={t("healthTracker.habits.exerciseTime")}
                        error={(error && isEmptyInput(habitInputs.timeExercise)) || errorEx}
                        type="number"
                        value={lastHabit ? formatNumber(lastHabit.minutosExercicio) : t("healthTracker.common.notAvailable")}
                        unit={t("healthTracker.common.minutes")}
                        date={lastHabit ? formatDateTime(lastHabit.dataRegistro) : t("healthTracker.common.notAvailable")}
                        inputValue={habitInputs.timeExercise}
                        onInputChange={(event) => {
                            updateInput("timeExercise", event.target.value);
                            setErrorEx(false);
                        }}
                        userName={lastHabit?.usuarioNome}
                        userFunction={getNomeFuncao(lastHabit?.usuarioFuncao, t)}
                    />
                </Box>

                <Box sx={{ minWidth: 0 }}>
                    <HabitCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<CalendarIcon />}
                        title={t("healthTracker.common.date")}
                        error={(error && !habitInputs.date) || errorDate}
                        value={lastHabit ? formatDate(lastHabit.dataReferencia) : t("healthTracker.common.notAvailable")}
                        date={lastHabit ? formatDateTime(lastHabit.dataRegistro) : t("healthTracker.common.notAvailable")}
                        inputValue={habitInputs.date}
                        onInputChange={(newValue) => {
                            updateInput("date", newValue);
                            setErrorDate(false);
                        }}
                        dataPicker
                        userName={lastHabit?.usuarioNome}
                        userFunction={getNomeFuncao(lastHabit?.usuarioFuncao, t)}
                    />
                </Box>

            </Box>

            <Paper
                variant="outlined"
                sx={{
                    mt: 2,
                    px: 2,
                    py: 1.5,
                    borderRadius: 3,
                    borderColor: theme.vitta.borderStrong,
                    display: "flex",
                    flexDirection: { xs: "column", sm: "row" },
                    flexWrap: "wrap",
                    gap: { xs: 1.5, sm: 3 },
                    width: { xs: "100%", sm: "fit-content" },
                    maxWidth: "100%",
                    boxSizing: "border-box"
                }}
            >
                    <Box sx={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: 1 }}>
                        <Typography color="text.secondary" fontSize="0.82rem" fontWeight={700}>
                            {t("healthTracker.habits.restIndex")}
                        </Typography>
                        <Typography fontSize="1rem" fontWeight={800} color="text.primary">
                            {lastHabit?.indiceRepouso != null
                                ? formatNumber(lastHabit.indiceRepouso)
                                : t("healthTracker.common.notAvailable")}
                        </Typography>
                    </Box>

                    <Box sx={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: 1 }}>
                        <Typography color="text.secondary" fontSize="0.82rem" fontWeight={700}>
                            {t("healthTracker.habits.restRecommended")}
                        </Typography>
                        <Box>
                            {lastHabit?.repouso == null ? (
                                <Typography fontWeight={800}>{t("healthTracker.common.notAvailable")}</Typography>
                            ) : (
                                <Chip
                                    size="small"
                                    color={lastHabit.repouso ? "warning" : "success"}
                                    label={lastHabit.repouso
                                        ? t("healthTracker.habits.yes")
                                        : t("healthTracker.habits.no")}
                                />
                            )}
                        </Box>
                    </Box>
            </Paper>

                {isFormOpen && (
                    <Box sx={{ mt: 2.5, width: "100%", maxWidth: { sm: 360 } }}>
                        <FormControl fullWidth>
                            <InputLabel id="habit-notification-channel-label">
                                {t("healthTracker.habits.notificationChannel")}
                            </InputLabel>
                            <Select
                                labelId="habit-notification-channel-label"
                                value={habitInputs.channel}
                                label={t("healthTracker.habits.notificationChannel")}
                                onChange={(event) => updateInput("channel", event.target.value)}
                            >
                                <MenuItem value="email">{t("healthTracker.habits.email")}</MenuItem>
                                <MenuItem value="sms">{t("healthTracker.habits.sms")}</MenuItem>
                                <MenuItem value="ambos">{t("healthTracker.habits.both")}</MenuItem>
                            </Select>
                        </FormControl>
                    </Box>
                )}
        </Paper>
    );
}
