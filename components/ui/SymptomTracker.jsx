import { useEffect, useState } from "react";

import { Box, Button, Grid, IconButton, Paper, Tooltip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import AddIcon from "@mui/icons-material/Add";
import AssignmentOutlinedIcon from "@mui/icons-material/AssignmentOutlined";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import SpeedIcon from "@mui/icons-material/Speed";
import { CalendarIcon } from "@mui/x-date-pickers";

import { usePatient } from "../../context/PatientContext";
import { useAlert } from "../../hooks/useAlert";
import { editSymptom, getSymptom, registerSymptom } from "../../services/symptomService";
import { getMedicoStyle, getNomeFuncao, getResponsavelStyle } from "../../utils/validators/userFunction";
import SymptomCard from "../ui/cards/SymptomCard";
import { useI18n } from "../../src/i18n";
import { useThemeMode } from "../../src/theme/ThemeModeProvider";

const emptyInputs = {
    symptom: "",
    intencity: "",
    date: null,
};

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

export function SymptomTracker() {
    const { selectedPatient } = usePatient();
    const { showAlert } = useAlert();
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const { t, formatDate, formatDateTime, formatNumber } = useI18n();
    const { accessibilityMode } = useThemeMode();

    const [editing, setEditing] = useState(false);
    const [addSymptom, setAddSymptom] = useState(false);
    const [error, setError] = useState(false);
    const [errorSymptom, setErrorSymptom] = useState(false);
    const [errorIntensity, setErrorIntensity] = useState(false);
    const [errorDate, setErrorDate] = useState(false);
    const [symptoms, setSymptoms] = useState([]);
    const [symptomInputs, setSymptomInputs] = useState(emptyInputs);

    const userType = localStorage.getItem("tipo");
    const canEdit = userType !== "saude";
    const isFormOpen = addSymptom || editing;
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
    const accessibleActionButtonSx = (variant) => ({
        ...actionButtonSx[variant],
        minHeight: 48,
        px: { xs: 1.5, sm: 2 },
        borderRadius: 2,
        fontWeight: 800,
        textTransform: "none",
        flex: { xs: "1 1 140px", sm: "0 0 auto" },
        "&:focus-visible": {
            outline: "3px solid",
            outlineColor: "secondary.main",
            outlineOffset: 2
        }
    });

    const lastSymptom = symptoms.reduce((latest, current) => {
        if (!latest) return current;

        return new Date(current.dataRegistro) > new Date(latest.dataRegistro)
            ? current
            : latest;
    }, null);

    let style = null;

    if (lastSymptom?.usuarioTipo === "responsavel") {
        style = getResponsavelStyle(lastSymptom.usuarioFuncao, t);
    } else if (lastSymptom?.usuarioTipo === "saude") {
        style = getMedicoStyle(lastSymptom.usuarioFuncao, t);
    }

    function updateInput(field, value) {
        setSymptomInputs((prev) => ({
            ...prev,
            [field]: value
        }));
    }

    function clearErrors() {
        setError(false);
        setErrorIntensity(false);
        setErrorSymptom(false);
        setErrorDate(false);
    }

    function closeForm() {
        setEditing(false);
        setAddSymptom(false);
        clearErrors();
    }

    function handleClearInputs() {
        setSymptomInputs(emptyInputs);
    }

    function handleDataEditing() {
        if (!lastSymptom) return;

        setSymptomInputs({
            symptom: lastSymptom.sintoma,
            intencity: lastSymptom.intensidadeDor,
            date: lastSymptom.dataReferencia
        });
    }

    function isValidIntencity(value) {
        const num = Number(value);
        return !isNaN(num) && num >= 1 && num <= 10;
    }

    function canRegister() {
        if (!symptomInputs.symptom || !symptomInputs.intencity || !symptomInputs.date) {
            showAlert("error", t("healthTracker.habits.fillAll"));
            setError(true);
            return false;
        }

        if (!isValidIntencity(symptomInputs.intencity)) {
            showAlert("error", t("healthTracker.symptoms.invalidIntensity"));
            setErrorSymptom(true);
            return false;
        }

        clearErrors();
        return true;
    }

    async function refreshSymptoms() {
        if (!CPF) return;

        const updatedSymptom = await getSymptom(CPF);
        setSymptoms(updatedSymptom);
    }

    async function handleRegister() {
        if (!canRegister()) return;

        const data = {
            sintoma: symptomInputs.symptom,
            intensidadeDor: parseInt(symptomInputs.intencity, 10),
            dataReferencia: symptomInputs.date,
        };

        try {
            if (addSymptom) {
                await registerSymptom(CPF, data);
                showAlert("success", t("healthTracker.symptoms.registered"));
            }

            if (editing) {
                await editSymptom(lastSymptom.id, CPF, data);
                showAlert("success", t("healthTracker.symptoms.edited"));
            }

            await refreshSymptoms();
            handleClearInputs();
            closeForm();
        } catch (registerError) {
            console.error("Erro ao salvar sintomas:", registerError);
            showAlert("error", t("healthTracker.symptoms.saveError"));
        }
    }

    useEffect(() => {
        if (!selectedPatient) return;

        async function fetchSymptoms() {
            try {
                const data = await getSymptom(selectedPatient.cpf);
                setSymptoms(data);
            } catch (fetchError) {
                console.error("Erro ao buscar sintomas:", fetchError);
                setSymptoms([]);
            }
        }

        fetchSymptoms();
    }, [selectedPatient]);

    useEffect(() => {
        const hasUnsavedChanges = Object.values(symptomInputs).some(Boolean);

        const handleBeforeUnload = (event) => {
            if (!hasUnsavedChanges) return;

            event.preventDefault();
            event.returnValue = "";
        };

        window.addEventListener("beforeunload", handleBeforeUnload);

        return () => {
            window.removeEventListener("beforeunload", handleBeforeUnload);
        };
    }, [symptomInputs]);

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
                alignItems={{ xs: "stretch", sm: "center" }}
                justifyContent="space-between"
                flexDirection={{ xs: "column", sm: "row" }}
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
                            overflowWrap: "break-word",
                            wordBreak: "normal"
                        }}
                    >
                        {t("healthTracker.symptoms.title")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            fontSize: "0.88rem",
                            mt: 0.25,
                            overflowWrap: "break-word",
                            wordBreak: "normal"
                        }}
                    >
                        {t("healthTracker.symptoms.description")}
                    </Typography>
                </Box>

                {isFormOpen ? (
                    <Box display="flex" gap={1} flexShrink={0} flexWrap="wrap" width={{ xs: "100%", sm: "auto" }}>
                        {accessibilityMode ? (
                            <>
                                <Button onClick={closeForm} startIcon={<CloseIcon />} sx={accessibleActionButtonSx("cancel")}>
                                    {t("healthTracker.common.cancel")}
                                </Button>
                                <Button onClick={handleRegister} startIcon={<CheckIcon />} sx={accessibleActionButtonSx("save")}>
                                    {t("healthTracker.common.save")}
                                </Button>
                            </>
                        ) : (
                            <>
                                <Tooltip title={t("healthTracker.common.cancel")}>
                                    <IconButton onClick={closeForm} aria-label={t("healthTracker.common.cancel")} sx={actionButtonSx.cancel}>
                                        <CloseIcon />
                                    </IconButton>
                                </Tooltip>
                                <Tooltip title={t("healthTracker.common.save")}>
                                    <IconButton onClick={handleRegister} aria-label={t("healthTracker.common.save")} sx={actionButtonSx.save}>
                                        <CheckIcon />
                                    </IconButton>
                                </Tooltip>
                            </>
                        )}
                    </Box>
                ) : (
                    <Box display="flex" alignItems="center" gap={1} flexShrink={0} flexWrap="wrap" width={{ xs: "100%", sm: "auto" }}>
                        {canEdit && (
                            accessibilityMode ? (
                                <Button
                                    onClick={() => {
                                        setAddSymptom(true);
                                        handleClearInputs();
                                    }}
                                    startIcon={<AddIcon />}
                                    sx={accessibleActionButtonSx("add")}
                                >
                                    {t("healthTracker.symptoms.add")}
                                </Button>
                            ) : (
                                <Tooltip title={t("healthTracker.symptoms.add")}>
                                    <IconButton
                                        onClick={() => {
                                            setAddSymptom(true);
                                            handleClearInputs();
                                        }}
                                        aria-label={t("healthTracker.symptoms.add")}
                                        sx={actionButtonSx.add}
                                    >
                                        <AddIcon />
                                    </IconButton>
                                </Tooltip>
                            )
                        )}

                        {canEdit && (
                            accessibilityMode ? (
                                <Button
                                    onClick={() => {
                                        setEditing(true);
                                        handleDataEditing();
                                    }}
                                    disabled={!lastSymptom}
                                    startIcon={<EditOutlinedIcon />}
                                    sx={accessibleActionButtonSx("edit")}
                                >
                                    {t("healthTracker.symptoms.edit")}
                                </Button>
                            ) : (
                                <Tooltip title={t("healthTracker.symptoms.edit")}>
                                    <span>
                                        <IconButton
                                            onClick={() => {
                                                setEditing(true);
                                                handleDataEditing();
                                            }}
                                            disabled={!lastSymptom}
                                            aria-label={t("healthTracker.symptoms.edit")}
                                            sx={actionButtonSx.edit}
                                        >
                                            <EditOutlinedIcon />
                                        </IconButton>
                                    </span>
                                </Tooltip>
                            )
                        )}
                    </Box>
                )}
            </Box>

            <Grid container spacing={2.5}>
                <Grid item xs={12} sm={6} lg={4}>
                    <SymptomCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<AssignmentOutlinedIcon />}
                        title={t("healthTracker.symptoms.symptom")}
                        error={(error && !symptomInputs.symptom) || errorIntensity}
                        type="text"
                        value={lastSymptom ? lastSymptom.sintoma : t("healthTracker.common.notAvailable")}
                        date={lastSymptom ? formatDateTime(lastSymptom.dataRegistro) : t("healthTracker.common.notAvailable")}
                        inputValue={symptomInputs.symptom}
                        placeholder={t("healthTracker.symptoms.symptomExample")}
                        helperText={
                            error && !symptomInputs.symptom
                                ? t("healthTracker.habits.fillAll")
                                : undefined
                        }
                        onInputChange={(event) => {
                            updateInput("symptom", event.target.value);
                            setErrorIntensity(false);
                        }}
                        userName={lastSymptom?.usuarioNome}
                        userFunction={getNomeFuncao(lastSymptom?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <SymptomCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<SpeedIcon />}
                        title={t("healthTracker.symptoms.intensity")}
                        error={(error && !symptomInputs.intencity) || errorSymptom}
                        type="number"
                        value={lastSymptom ? formatNumber(lastSymptom.intensidadeDor) : t("healthTracker.common.notAvailable")}
                        date={lastSymptom ? formatDateTime(lastSymptom.dataRegistro) : t("healthTracker.common.notAvailable")}
                        inputValue={symptomInputs.intencity}
                        placeholder={t("healthTracker.symptoms.intensityExample")}
                        helperText={
                            errorSymptom
                                ? t("healthTracker.symptoms.invalidIntensity")
                                : error && !symptomInputs.intencity
                                    ? t("healthTracker.habits.fillAll")
                                    : undefined
                        }
                        onInputChange={(event) => {
                            updateInput("intencity", event.target.value);
                            setErrorSymptom(false);
                        }}
                        userName={lastSymptom?.usuarioNome}
                        userFunction={getNomeFuncao(lastSymptom?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <SymptomCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<CalendarIcon />}
                        title={t("healthTracker.common.date")}
                        error={(error && !symptomInputs.date) || errorDate}
                        value={lastSymptom ? formatDate(lastSymptom.dataReferencia) : t("healthTracker.common.notAvailable")}
                        date={lastSymptom ? formatDateTime(lastSymptom.dataRegistro) : t("healthTracker.common.notAvailable")}
                        inputValue={symptomInputs.date}
                        onInputChange={(newValue) => {
                            updateInput("date", newValue);
                            setErrorDate(false);
                        }}
                        dataPicker
                        userName={lastSymptom?.usuarioNome}
                        userFunction={getNomeFuncao(lastSymptom?.usuarioFuncao, t)}
                    />
                </Grid>
            </Grid>
        </Paper>
    );
}
