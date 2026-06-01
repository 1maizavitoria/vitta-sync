import Grid from "@mui/material/Grid";
import FavoriteIcon from "@mui/icons-material/Favorite";
import MonitorHeartIcon from "@mui/icons-material/MonitorHeart";
import AirIcon from '@mui/icons-material/Air';
import DeviceThermostatIcon from '@mui/icons-material/DeviceThermostat';
import { Box, Paper } from "@mui/material";
import { useEffect, useState } from "react";
import { useAlert } from "../../hooks/useAlert";
import { editSymptom, getSymptom, registerSymptom } from "../../services/symptomService";
import ButtonUI from "./Button";
import HabitCard from "../ui/cards/HabitCard";

import BedtimeIcon from "@mui/icons-material/Bedtime";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import { CalendarIcon } from "@mui/x-date-pickers";
import SymptomCard from "../ui/cards/SymptomCard";
import { formatDate } from "../../utils/formatters/formatDate";

import { usePatient } from "../../context/PatientContext";

export function SymptomTracker() {
    const { selectedPatient } = usePatient();
    const { showAlert } = useAlert();

    const [editing, setEditing] = useState(false);
    const [addSymptom, setAddSymptom] = useState(false);

    const [error, setError] = useState(false);
    const [errorSymptom, setErrorSymptom] = useState(false);
    const [errorIntensity, setErrorIntensity] = useState(false);
    const [errorDate, setErrorDate] = useState(false);

    const [Symptom, setSymptom] = useState([]);

    const [closeMeditionInput, setCloseMeditionInput] = useState(false);

    const [resetKey, setResetKey] = useState(0);

    const lastSymptom = Symptom.reduce((latest, current) => {
        if (!latest) return current;

        return new Date(current.dataRegistro) > new Date(latest.dataRegistro)
            ? current
            : latest;
    }, null);

    const [SymptomInputs, setSymptomInputs] = useState({
        symptom: "",
        intencity: "",
        date: null,
    });

    const userType =
        localStorage.getItem("tipo");

    const canEdit =
        userType !== "saude";

    function isValidIntencity(value) {
        const num = Number(value);
        return !isNaN(num) && num >= 1 && num <= 10;
    }

    const formatDateString = (dateString) => {
        if (!dateString) return "N/A";
        const [year, month, day] = dateString.split("-");
        return `${day}/${month}/${year}`;
    };


    function canRegister() {
        // Verifica se todos os campos estão preenchidos
        if (!SymptomInputs.symptom || !SymptomInputs.intencity || !SymptomInputs.date) {
            showAlert("error", "Preencha todos os campos");
            setError(true);
            return false;
        }

        if (!isValidIntencity(SymptomInputs.intencity)) {
            showAlert("error", "Intensidade deve estar em um valor válido (1–10)");
            setErrorSymptom(true);
            return false;
        }

        setErrorIntensity(false);
        setErrorSymptom(false);
        setErrorDate(false);

        setError(false);
        return true;

    }

    async function handleRegister() {

        if (!canRegister()) return;

        const data = {
            sintoma: SymptomInputs.symptom,
            intensidadeDor: parseInt(SymptomInputs.intencity, 10),
            dataReferencia: SymptomInputs.date,
        }

        const CPF = selectedPatient?.cpf;

        if (addSymptom) {

            try {

                await registerSymptom(CPF, data);
                const updatedSymptom = await getSymptom(CPF);
                setSymptom(updatedSymptom);
                showAlert("success", "Sintomas registrados com sucesso");

                handleClearInputs();

                setCloseMeditionInput(true);

                //serve parar resetar o estado dos componentes sem o useeffect.
                setResetKey(prev => prev + 1);
                setAddSymptom(false);


            } catch (error) {
                console.error("Erro ao registrar Sintomas:", error);
                showAlert("error", "Erro ao registrar Sintomas");
            }
        }

        if (editing) {

            try {

                await editSymptom(lastSymptom.id, CPF, data);
                const updatedSymptom = await getSymptom(CPF);
                setSymptom(updatedSymptom);
                showAlert("success", "Sintomas editados com sucesso");

                handleClearInputs();

                setCloseMeditionInput(true);

                //serve parar resetar o estado dos componentes sem o useeffect.
                setResetKey(prev => prev + 1);
                setEditing(false);


            } catch (error) {
                console.error("Erro ao editar Sintomas:", error);
                showAlert("error", "Erro ao editar Sintomas");
            }
        }

    }

    function handleDataEditing() {
        setSymptomInputs({
            symptom: lastSymptom.sintoma,
            intencity: lastSymptom.intensidadeDor,
            date: lastSymptom.dataReferencia
        });
    }

    function handleClearInputs() {
        setSymptomInputs({
            symptom: "",
            intencity: "",
            date: "",
        });
    }

    useEffect(() => {

        if (!selectedPatient) return;

        async function fetchSymptoms() {

            try {

                const data =
                    await getSymptom(
                        selectedPatient.cpf
                    );

                setSymptom(data);

                console.log(
                    "Sintomas obtidos:",
                    data
                );

            } catch (error) {

                console.error(
                    "Erro ao buscar sintomas:",
                    error
                );

                setSymptom([]);
            }
        }

        fetchSymptoms();

    }, [selectedPatient]);

    const hasUnsavedChanges =
        SymptomInputs.intencity ||
        SymptomInputs.symptom ||
        SymptomInputs.date;

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

            <Box display="flex" alignItems="center" mb={2}>

                {(addSymptom || editing) ? (
                    <Box display="flex" gap={1}>

                        <ButtonUI onClick={() => {
                            setEditing(false);
                            setAddSymptom(false);
                            setErrorIntensity(false);
                            setErrorSymptom(false);
                            setErrorDate(false);
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
                        {canEdit && <ButtonUI onClick={() => {
                            setAddSymptom(true);
                            handleClearInputs();
                        }}>
                            + Adicionar Sintomas
                        </ButtonUI>}

                        {canEdit && <ButtonUI onClick={() => {
                            setEditing(true);
                            handleDataEditing();
                        }}>
                            Editar Sintomas
                        </ButtonUI>}
                    </Box>
                )}
            </Box>

            <Grid container spacing={3}>

                <Grid item xs={12} md={4}>
                    <SymptomCard
                        showInput={editing || addSymptom}
                        icon={<FitnessCenterIcon />}
                        title="Sintoma"
                        error={(error && !SymptomInputs.symptom) || errorIntensity}
                        type="text"
                        value={lastSymptom ? lastSymptom.sintoma : "N/A"}
                        // unit="Minutos"
                        date={lastSymptom ? new Date(lastSymptom.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={SymptomInputs.symptom}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setSymptomInputs({ ...SymptomInputs, symptom: e.target.value }, setErrorIntensity(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <SymptomCard
                        showInput={editing || addSymptom}
                        icon={<BedtimeIcon />}
                        title="Intensidade"
                        error={(error && !SymptomInputs.intencity) || errorSymptom}
                        type="text"
                        value={lastSymptom ? lastSymptom.intensidadeDor : "N/A"}
                        // unit="Horas"
                        date={lastSymptom ? new Date(lastSymptom.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={SymptomInputs.intencity}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setSymptomInputs({ ...SymptomInputs, intencity: e.target.value }, setErrorSymptom(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <SymptomCard
                        showInput={editing || addSymptom}
                        icon={<CalendarIcon />}
                        title="Data"
                        error={(error && !SymptomInputs.date) || errorDate}
                        // type="number"
                        value={lastSymptom ? formatDateString(lastSymptom.dataReferencia) : "N/A"}
                        // unit="yyyy-MM-dd"
                        date={lastSymptom ? new Date(lastSymptom.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={SymptomInputs.date}
                        closeMeditionInput={closeMeditionInput}
                        // onInputChange={(e) => setSymptomInputs({ ...SymptomInputs, date: e.target.value }, setErrorDate(false))}
                        onInputChange={(newValue) => {
                            setSymptomInputs({ ...SymptomInputs, date: newValue });
                            setErrorDate(false);
                        }}
                        key={resetKey}
                        dataPicker={true}
                    />
                </Grid>

            </Grid>

        </Paper>

    )

}
