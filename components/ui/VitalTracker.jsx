import { useEffect, useState } from "react";

import { Box, Grid, IconButton, Paper, Tooltip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import AddIcon from "@mui/icons-material/Add";
import AirIcon from "@mui/icons-material/Air";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import DeviceThermostatIcon from "@mui/icons-material/DeviceThermostat";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import FavoriteIcon from "@mui/icons-material/Favorite";
import MonitorHeartIcon from "@mui/icons-material/MonitorHeart";
import ScaleIcon from "@mui/icons-material/Scale";

import { usePatient } from "../../context/PatientContext";
import { useAlert } from "../../hooks/useAlert";
import { editVitalSigns, getVitalSigns, registerVitalSigns } from "../../services/vitalService";
import { getMedicoStyle, getNomeFuncao, getResponsavelStyle } from "../../utils/validators/userFunction";
import VitalCard from "../ui/cards/VitalCard";
import { useI18n } from "../../src/i18n";

const emptyInputs = {
    peso: "",
    frequenciaCardiaca: "",
    frequenciaRespiratoria: "",
    saturacao: "",
    temperatura: "",
    sistolica: "",
    diastolica: "",
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

export function VitalTracker() {
    const { showAlert } = useAlert();
    const { selectedPatient } = usePatient();
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();

    const [editing, setEditing] = useState(false);
    const [addVital, setAddVital] = useState(false);
    const [error, setError] = useState(false);
    const [errorFC, setErrorFC] = useState(false);
    const [errorFR, setErrorFR] = useState(false);
    const [errorSPO2, setErrorSPO2] = useState(false);
    const [errorTemp, setErrorTemp] = useState(false);
    const [errorSistolica, setErrorSistolica] = useState(false);
    const [errorDiastolica, setErrorDiastolica] = useState(false);
    const [vitals, setVitals] = useState([]);
    const [vitalInputs, setVitalInputs] = useState(emptyInputs);

    const userType = localStorage.getItem("tipo");
    const canEdit = userType !== "saude";
    const isFormOpen = addVital || editing;
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

    const lastVital = vitals.reduce((latest, current) => {
        if (!latest) return current;

        return new Date(current.dataRegistro) > new Date(latest.dataRegistro)
            ? current
            : latest;
    }, null);

    let style = null;

    if (lastVital?.usuarioTipo === "responsavel") {
        style = getResponsavelStyle(lastVital.usuarioFuncao, t);
    } else if (lastVital?.usuarioTipo === "saude") {
        style = getMedicoStyle(lastVital.usuarioFuncao, t);
    }

    function updateInput(field, value) {
        setVitalInputs((prev) => ({
            ...prev,
            [field]: value
        }));
    }

    function clearErrors() {
        setError(false);
        setErrorFC(false);
        setErrorFR(false);
        setErrorSPO2(false);
        setErrorTemp(false);
        setErrorSistolica(false);
        setErrorDiastolica(false);
    }

    function closeForm() {
        setEditing(false);
        setAddVital(false);
        clearErrors();
    }

    function handleClearInputs() {
        setVitalInputs(emptyInputs);
    }

    function handleDataEditing() {
        if (!lastVital) return;

        setVitalInputs({
            peso: lastVital.peso,
            frequenciaCardiaca: lastVital.fcBpm,
            frequenciaRespiratoria: lastVital.frRpm,
            saturacao: lastVital.spo2Porcento,
            temperatura: lastVital.tempCelcius,
            sistolica: lastVital.paSistolica,
            diastolica: lastVital.paDiastolica,
        });
    }

    function canRegister() {
        if (
            !vitalInputs.peso ||
            !vitalInputs.frequenciaCardiaca ||
            !vitalInputs.frequenciaRespiratoria ||
            !vitalInputs.saturacao ||
            !vitalInputs.temperatura ||
            !vitalInputs.sistolica ||
            !vitalInputs.diastolica
        ) {
            showAlert("error", t("healthTracker.vitals.fillAll"));
            setError(true);
            return false;
        }

        if (Number(vitalInputs.frequenciaCardiaca) < 30 || Number(vitalInputs.frequenciaCardiaca) > 220) {
            showAlert("error", t("healthTracker.vitals.invalidHeartRate"));
            setErrorFC(true);
            return false;
        }

        if (Number(vitalInputs.frequenciaRespiratoria) < 5 || Number(vitalInputs.frequenciaRespiratoria) > 60) {
            showAlert("error", t("healthTracker.vitals.invalidRespiratoryRate"));
            setErrorFR(true);
            return false;
        }

        if (Number(vitalInputs.saturacao) < 70 || Number(vitalInputs.saturacao) > 100) {
            showAlert("error", t("healthTracker.vitals.invalidSaturation"));
            setErrorSPO2(true);
            return false;
        }

        if (Number(vitalInputs.temperatura) < 30 || Number(vitalInputs.temperatura) > 45) {
            showAlert("error", t("healthTracker.vitals.invalidTemperature"));
            setErrorTemp(true);
            return false;
        }

        if (
            Number(vitalInputs.sistolica) < 50 ||
            Number(vitalInputs.sistolica) > 250 ||
            Number(vitalInputs.diastolica) < 30 ||
            Number(vitalInputs.diastolica) > 150
        ) {
            showAlert("error", t("healthTracker.vitals.invalidBloodPressure"));
            setErrorSistolica(true);
            setErrorDiastolica(true);
            return false;
        }

        if (Number(vitalInputs.sistolica) <= Number(vitalInputs.diastolica)) {
            showAlert("error", t("healthTracker.vitals.invalidPressureOrder"));
            setErrorSistolica(true);
            setErrorDiastolica(true);
            return false;
        }

        if (Number(vitalInputs.peso) < 1 || Number(vitalInputs.peso) > 400) {
            showAlert("error", t("healthTracker.vitals.invalidWeight"));
            return false;
        }

        clearErrors();
        return true;
    }

    async function refreshVitals() {
        if (!CPF) return;

        const updatedVitals = await getVitalSigns(CPF);
        setVitals(updatedVitals);
    }

    async function handleRegister() {
        if (!canRegister()) return;

        const data = {
            peso: vitalInputs.peso,
            fcBpm: vitalInputs.frequenciaCardiaca,
            frRpm: vitalInputs.frequenciaRespiratoria,
            paSistolica: vitalInputs.sistolica,
            paDiastolica: vitalInputs.diastolica,
            tempCelcius: vitalInputs.temperatura,
            spo2Porcento: vitalInputs.saturacao,
        };

        try {
            if (addVital) {
                await registerVitalSigns(CPF, data);
                showAlert("success", t("healthTracker.vitals.registered"));
            }

            if (editing) {
                await editVitalSigns(lastVital.id, CPF, data);
                showAlert("success", t("healthTracker.vitals.edited"));
            }

            await refreshVitals();
            handleClearInputs();
            closeForm();
        } catch (registerError) {
            console.error("Erro ao salvar sinais vitais:", registerError);
            showAlert("error", t("healthTracker.vitals.saveError"));
        }
    }

    useEffect(() => {
        if (!selectedPatient) return;

        async function fetchVitals() {
            try {
                const data = await getVitalSigns(selectedPatient.cpf);
                setVitals(data);
            } catch (fetchError) {
                console.error("Erro ao buscar sinais vitais:", fetchError);
                setVitals([]);
            }
        }

        fetchVitals();
    }, [selectedPatient]);

    useEffect(() => {
        const hasUnsavedChanges = Object.values(vitalInputs).some(Boolean);

        const handleBeforeUnload = (event) => {
            if (!hasUnsavedChanges) return;

            event.preventDefault();
            event.returnValue = "";
        };

        window.addEventListener("beforeunload", handleBeforeUnload);

        return () => {
            window.removeEventListener("beforeunload", handleBeforeUnload);
        };
    }, [vitalInputs]);

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
                        {t("healthTracker.vitals.title")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            fontSize: "0.88rem",
                            mt: 0.25,
                            overflowWrap: "anywhere"
                        }}
                    >
                        {t("healthTracker.vitals.description")}
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
                            <Tooltip title={t("healthTracker.vitals.add")}>
                                <IconButton
                                    onClick={() => {
                                        setAddVital(true);
                                        handleClearInputs();
                                    }}
                                    sx={actionButtonSx.add}
                                >
                                    <AddIcon />
                                </IconButton>
                            </Tooltip>
                        )}

                        {canEdit && (
                            <Tooltip title={t("healthTracker.vitals.edit")}>
                                <span>
                                    <IconButton
                                        onClick={() => {
                                            setEditing(true);
                                            handleDataEditing();
                                        }}
                                        disabled={!lastVital}
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

            <Grid container spacing={2.5}>
                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<ScaleIcon />}
                        title={t("healthTracker.vitals.weight")}
                        type="number"
                        value={lastVital ? lastVital.peso : t("healthTracker.common.notAvailable")}
                        unit="kg"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.peso}
                        onInputChange={(event) => updateInput("peso", event.target.value)}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<MonitorHeartIcon />}
                        title={t("healthTracker.vitals.heartRate")}
                        error={(error && !vitalInputs.frequenciaCardiaca) || errorFC}
                        type="number"
                        value={lastVital ? lastVital.fcBpm : t("healthTracker.common.notAvailable")}
                        unit="bpm"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.frequenciaCardiaca}
                        onInputChange={(event) => {
                            updateInput("frequenciaCardiaca", event.target.value);
                            setErrorFC(false);
                        }}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<FavoriteIcon />}
                        title={t("healthTracker.vitals.respiratoryRate")}
                        error={(error && !vitalInputs.frequenciaRespiratoria) || errorFR}
                        type="number"
                        value={lastVital ? lastVital.frRpm : t("healthTracker.common.notAvailable")}
                        unit="rpm"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.frequenciaRespiratoria}
                        onInputChange={(event) => {
                            updateInput("frequenciaRespiratoria", event.target.value);
                            setErrorFR(false);
                        }}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<AirIcon />}
                        title={t("healthTracker.vitals.oxygenSaturation")}
                        error={(error && !vitalInputs.saturacao) || errorSPO2}
                        type="number"
                        value={lastVital ? lastVital.spo2Porcento : t("healthTracker.common.notAvailable")}
                        unit="%"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.saturacao}
                        onInputChange={(event) => {
                            updateInput("saturacao", event.target.value);
                            setErrorSPO2(false);
                        }}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<DeviceThermostatIcon />}
                        title={t("healthTracker.vitals.bodyTemperature")}
                        error={(error && !vitalInputs.temperatura) || errorTemp}
                        type="number"
                        value={lastVital ? lastVital.tempCelcius : t("healthTracker.common.notAvailable")}
                        unit="°C"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.temperatura}
                        onInputChange={(event) => {
                            updateInput("temperatura", event.target.value);
                            setErrorTemp(false);
                        }}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<MonitorHeartIcon />}
                        title={t("healthTracker.vitals.systolicPressure")}
                        error={(error && !vitalInputs.sistolica) || errorSistolica}
                        type="number"
                        value={lastVital ? lastVital.paSistolica : t("healthTracker.common.notAvailable")}
                        unit="mmHg"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.sistolica}
                        onInputChange={(event) => {
                            updateInput("sistolica", event.target.value);
                            setErrorSistolica(false);
                        }}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>

                <Grid item xs={12} sm={6} lg={4}>
                    <VitalCard
                        userStyle={style}
                        showInput={isFormOpen}
                        icon={<MonitorHeartIcon />}
                        title={t("healthTracker.vitals.diastolicPressure")}
                        error={(error && !vitalInputs.diastolica) || errorDiastolica}
                        type="number"
                        value={lastVital ? lastVital.paDiastolica : t("healthTracker.common.notAvailable")}
                        unit="mmHg"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : t("healthTracker.common.notAvailable")}
                        inputValue={vitalInputs.diastolica}
                        onInputChange={(event) => {
                            updateInput("diastolica", event.target.value);
                            setErrorDiastolica(false);
                        }}
                        userName={lastVital?.usuarioNome}
                        userFunction={getNomeFuncao(lastVital?.usuarioFuncao, t)}
                    />
                </Grid>
            </Grid>
        </Paper>
    );
}
