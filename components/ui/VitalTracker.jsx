import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";

import FavoriteIcon from "@mui/icons-material/Favorite";
import MonitorHeartIcon from "@mui/icons-material/MonitorHeart";

import AirIcon from '@mui/icons-material/Air';
import DeviceThermostatIcon from '@mui/icons-material/DeviceThermostat';

import { Box, Paper } from "@mui/material";
import { useEffect, useState } from "react";
import { useAlert } from "../../hooks/useAlert";
import { editVitalSigns, getVitalSigns, registerVitalSigns } from "../../services/vitalService";
import VitalCard from "./VitalCard";
import ButtonUI from "./Button";

export function VitalTracker() {
    const [editing, setEditing] = useState(false);
    const [addVital, setAddVital] = useState(false);
    const { showAlert } = useAlert();
    const [error, setError] = useState(false);
    const [errorFC, setErrorFC] = useState(false);
    const [errorFR, setErrorFR] = useState(false);
    const [errorSPO2, setErrorSPO2] = useState(false);
    const [errorTemp, setErrorTemp] = useState(false);
    const [errorSistolica, setErrorSistolica] = useState(false);
    const [errorDiastolica, setErrorDiastolica] = useState(false);
    const [vitals, setVitals] = useState([]);
    const [closeMeditionInput, setCloseMeditionInput] = useState(false);

    const [resetKey, setResetKey] = useState(0);

    const lastVital = vitals.reduce((latest, current) => {
        if (!latest) return current;

        return new Date(current.dataRegistro) > new Date(latest.dataRegistro)
            ? current
            : latest;
    }, null);

    const [vitalInputs, setVitalInputs] = useState({
        frequenciaCardiaca: "",
        frequenciaRespiratoria: "",
        saturacao: "",
        temperatura: "",
        sistolica: "",
        diastolica: "",
    });

    function canRegister() {
        // Verifica se todos os campos estão preenchidos
        if (!vitalInputs.frequenciaCardiaca || !vitalInputs.frequenciaRespiratoria || !vitalInputs.saturacao || !vitalInputs.temperatura || !vitalInputs.sistolica || !vitalInputs.diastolica) {
            showAlert("error", "Faça todas as medições antes de salvar");
            setError(true);
            return false;
        }

        // Frequência cardíaca
        if (Number(vitalInputs.frequenciaCardiaca) < 30 || Number(vitalInputs.frequenciaCardiaca) > 220) {
            showAlert("error", "Frequência cardíaca inválida");
            setErrorFC(true);
            return false;
        }

        // Frequência respiratória
        if (
            Number(vitalInputs.frequenciaRespiratoria) < 5 ||
            Number(vitalInputs.frequenciaRespiratoria) > 60
        ) {
            showAlert("error", "Frequência respiratória inválida");
            setErrorFR(true);
            return false;
        }

        // Saturação
        if (
            Number(vitalInputs.saturacao) < 70 ||
            Number(vitalInputs.saturacao) > 100
        ) {
            showAlert("error", "Saturação deve estar entre 70% e 100%");
            setErrorSPO2(true);
            return false;
        }

        // Temperatura
        if (
            Number(vitalInputs.temperatura) < 30 ||
            Number(vitalInputs.temperatura) > 45
        ) {
            showAlert("error", "Temperatura inválida");
            setErrorTemp(true);
            return false;
        }

        // Pressão arterial
        if (
            Number(vitalInputs.sistolica) < 50 ||
            Number(vitalInputs.sistolica) > 250 ||
            Number(vitalInputs.diastolica) < 30 ||
            Number(vitalInputs.diastolica) > 150
        ) {
            showAlert("error", "Pressão arterial inválida");
            setErrorSistolica(true);
            setErrorDiastolica(true);
            return false;
        }

        // Regra clínica importante
        if (Number(vitalInputs.sistolica) <= Number(vitalInputs.diastolica)) {
            showAlert("error", "Sistólica deve ser maior que a diastólica");
            setErrorSistolica(true);
            setErrorDiastolica(true);
            return false;
        }

        setErrorFC(false);
        setErrorFR(false);
        setErrorSPO2(false);
        setErrorTemp(false);
        setErrorSistolica(false);
        setErrorDiastolica(false);

        setError(false);
        return true;

    }

    async function handleRegister() {

        if (!canRegister()) return;

        const CPF = localStorage.getItem("CPF");

        const data = {
            fcBpm: vitalInputs.frequenciaCardiaca,
            frRpm: vitalInputs.frequenciaRespiratoria,
            paSistolica: vitalInputs.sistolica,
            paDiastolica: vitalInputs.diastolica,
            tempCelcius: vitalInputs.temperatura,
            spo2Porcento: vitalInputs.saturacao,
        }

        if (addVital) {

            try {
                await registerVitalSigns(CPF, data);
                const updatedVitals = await getVitalSigns();
                setVitals(updatedVitals);
                showAlert("success", "Sinais vitais registrados com sucesso");

                handleClearInputs();

                setCloseMeditionInput(true);

                //serve parar resetar o estado dos componentes sem o useeffect.
                setResetKey(prev => prev + 1);
                setAddVital(false);


            } catch (error) {
                console.error("Erro ao registrar sinais vitais:", error);
                showAlert("error", "Erro ao registrar sinais vitais");
            }
        }

        if (editing) {

            try {
                await editVitalSigns(lastVital.id, data);
                const updatedVitals = await getVitalSigns();
                setVitals(updatedVitals);
                showAlert("success", "Sinais vitais editados com sucesso");

                handleClearInputs();

                setCloseMeditionInput(true);

                //serve parar resetar o estado dos componentes sem o useeffect.
                setResetKey(prev => prev + 1);
                setEditing(false);


            } catch (error) {
                console.error("Erro ao editar sinais vitais:", error);
                showAlert("error", "Erro ao editar sinais vitais");
            }
        }

    }

    function handleDataEditing() {
        setVitalInputs({
            frequenciaCardiaca: lastVital.fcBpm,
            frequenciaRespiratoria: lastVital.frRpm,
            saturacao: lastVital.spo2Porcento,
            temperatura: lastVital.tempCelcius,
            sistolica: lastVital.paSistolica,
            diastolica: lastVital.paDiastolica,
        });
    }

    function handleClearInputs() {
        setVitalInputs({
            frequenciaCardiaca: "",
            frequenciaRespiratoria: "",
            saturacao: "",
            temperatura: "",
            sistolica: "",
            diastolica: "",
        });
    }

    useEffect(() => {
        async function fetchVitals() {
            try {
                const data = await getVitalSigns();
                setVitals(data);
                console.log("Sinais vitais obtidos:", data);
            } catch (error) {
                console.error("Erro ao buscar sinais vitais:", error);
            }
        }

        fetchVitals();
    }, []);

    const hasUnsavedChanges =
        vitalInputs.frequenciaCardiaca ||
        vitalInputs.frequenciaRespiratoria ||
        vitalInputs.saturacao ||
        vitalInputs.temperatura ||
        vitalInputs.sistolica ||
        vitalInputs.diastolica;

    useEffect(() => {
        const handleBeforeUnload = (e) => {
            if (!hasUnsavedChanges) return;

            e.preventDefault();
            e.returnValue = ""; // necessário pro Chrome
        };

        window.addEventListener("beforeunload", handleBeforeUnload);

        return () => {
            window.removeEventListener("beforeunload", handleBeforeUnload);
        };
    }, [hasUnsavedChanges]);

    return (

        <Paper elevation={3} sx={{ p: 3 }}>

            {/* <Typography variant="h5" fontWeight="bold" sx={{ mb: 3 }}>
                Sinais Vitais
            </Typography> */}

            <Box display="flex" alignItems="center" mb={2}>

                {(addVital || editing) ? (
                    <Box display="flex" gap={1}>

                        <ButtonUI onClick={() => {
                            setEditing(false);
                            setAddVital(false);
                            setErrorFC(false);
                            setErrorFR(false);
                            setErrorSPO2(false);
                            setErrorTemp(false);
                            setErrorSistolica(false);
                            setErrorDiastolica(false);
                            setError(false);
                        }}>
                            Cancelar
                        </ButtonUI>

                        <ButtonUI
                            onClick={() => {
                                handleRegister();
                            }}
                        >
                            Salvar
                        </ButtonUI>

                    </Box>
                ) : (
                    <Box display="flex" alignItems="center" gap={1}>
                        <ButtonUI onClick={() => {
                            setAddVital(true);
                            handleClearInputs();
                        }}>
                            + Adicionar Medições
                        </ButtonUI>

                        <ButtonUI onClick={() => {
                            setEditing(true);
                            handleDataEditing();
                        }}>
                            Editar Medições
                        </ButtonUI>
                    </Box>
                )}
            </Box>

            <Grid container spacing={3}>
                <Grid item xs={12} md={4}>
                    <VitalCard
                        showInput={editing || addVital}
                        icon={<MonitorHeartIcon />}
                        title="Frequência cardíaca"
                        error={(error && !vitalInputs.frequenciaCardiaca) || errorFC}
                        type="number"
                        value={lastVital ? lastVital.fcBpm : "N/A"}
                        unit="bpm"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={vitalInputs.frequenciaCardiaca}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setVitalInputs({ ...vitalInputs, frequenciaCardiaca: e.target.value }, setErrorFC(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <VitalCard
                        showInput={editing || addVital}
                        icon={<FavoriteIcon />}
                        title="Frequência respiratória"
                        error={(error && !vitalInputs.frequenciaRespiratoria) || errorFR}
                        type="number"
                        value={lastVital ? lastVital.frRpm : "N/A"}
                        unit="rpm"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={vitalInputs.frequenciaRespiratoria}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setVitalInputs({ ...vitalInputs, frequenciaRespiratoria: e.target.value }, setErrorFR(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <VitalCard
                        showInput={editing || addVital}
                        icon={<AirIcon />}
                        title="Saturação de Oxigênio (SpO2)"
                        error={(error && !vitalInputs.saturacao) || errorSPO2}
                        type="number"
                        value={lastVital ? lastVital.spo2Porcento : "N/A"}
                        unit="%"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={vitalInputs.saturacao}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setVitalInputs({ ...vitalInputs, saturacao: e.target.value }, setErrorSPO2(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <VitalCard
                        showInput={editing || addVital}
                        icon={<DeviceThermostatIcon />}
                        title="Temperatura Corporal"
                        error={(error && !vitalInputs.temperatura) || errorTemp}
                        type="number"
                        value={lastVital ? lastVital.tempCelcius : "N/A"}
                        unit="°C"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={vitalInputs.temperatura}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setVitalInputs({ ...vitalInputs, temperatura: e.target.value }, setErrorTemp(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <VitalCard
                        showInput={editing || addVital}
                        icon={<MonitorHeartIcon />}
                        title="Pressão sistólica"
                        error={(error && !vitalInputs.sistolica) || errorSistolica}
                        type="number"
                        value={lastVital ? lastVital.paSistolica : "N/A"}
                        unit="mmHg"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={vitalInputs.sistolica}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setVitalInputs({ ...vitalInputs, sistolica: e.target.value }, setErrorSistolica(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <VitalCard
                        showInput={editing || addVital}
                        icon={<MonitorHeartIcon />}
                        title="Pressão diastólica"
                        error={(error && !vitalInputs.diastolica) || errorDiastolica}
                        type="number"
                        value={lastVital ? lastVital.paDiastolica : "N/A"}
                        unit="mmHg"
                        date={lastVital ? new Date(lastVital.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={vitalInputs.diastolica}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setVitalInputs({ ...vitalInputs, diastolica: e.target.value }, setErrorDiastolica(false))}
                        key={resetKey}
                    />
                </Grid>

            </Grid>

        </Paper>

    )

}
