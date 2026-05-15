import Grid from "@mui/material/Grid";
import FavoriteIcon from "@mui/icons-material/Favorite";
import MonitorHeartIcon from "@mui/icons-material/MonitorHeart";
import AirIcon from '@mui/icons-material/Air';
import DeviceThermostatIcon from '@mui/icons-material/DeviceThermostat';
import { Box, Paper } from "@mui/material";
import { useEffect, useState } from "react";
import { useAlert } from "../../hooks/useAlert";
import { editHabits, getHabits, registerHabits } from "../../services/habitService";
import ButtonUI from "./Button";
import HabitCard from "./HabitCard"

import BedtimeIcon from "@mui/icons-material/Bedtime";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import { CalendarIcon } from "@mui/x-date-pickers";

export function HabitTracker() {
    const { showAlert } = useAlert();

    const [editing, setEditing] = useState(false);
    const [addHabit, setAddHabit] = useState(false);

    const [error, setError] = useState(false);
    const [errorEx, setErrorEx] = useState(false);
    const [errorSl, setErrorSl] = useState(false);
    const [errorDate, setErrorDate] = useState(false);

    const [habits, setHabit] = useState([]);

    const [closeMeditionInput, setCloseMeditionInput] = useState(false);

    const [resetKey, setResetKey] = useState(0);

    const lastHabit = habits.reduce((latest, current) => {
        if (!latest) return current;

        return new Date(current.dataRegistro) > new Date(latest.dataRegistro)
            ? current
            : latest;
    }, null);

    const [habitInputs, setHabitInputs] = useState({
        timeExercise: "",
        timeSleep: "",
        date: null,
    });

    function isValidMinutes(value) {
        const num = Number(value);
        return !isNaN(num) && num >= 0 && num <= 1440;
    }

    function isValidHours(value) {
        const num = Number(value);
        return !isNaN(num) && num >= 0 && num <= 24;
    }

    function canRegister() {
        // Verifica se todos os campos estão preenchidos
        if (!habitInputs.timeExercise || !habitInputs.timeSleep || !habitInputs.date) {
            showAlert("error", "Preencha todos os campos");
            setError(true);
            return false;
        }

        if (!isValidMinutes(habitInputs.timeExercise)) {
            showAlert("error", "Exercício deve estar em minutos válidos (0–1440)");
            setErrorEx(true);
            return false;
        }

        if (!isValidHours(habitInputs.timeSleep)) {
            showAlert("error", "Sono deve estar em horas válidas (0–24)");
            setErrorSl(true);
            return false;
        }


        setErrorEx(false);
        setErrorSl(false);
        setErrorDate(false);

        setError(false);
        return true;

    }

    async function handleRegister() {


        if (!canRegister()) return;

        const data = {
            horasSono: habitInputs.timeSleep,
            minutosExercicio: habitInputs.timeExercise,
            dataReferencia: habitInputs.date,
        }

        const CPF = localStorage.getItem("CPF");

        if (addHabit) {

            try {

                await registerHabits(CPF, data);
                const updatedHabits = await getHabits(CPF);
                setHabit(updatedHabits);
                showAlert("success", "Hábitos registrados com sucesso");

                handleClearInputs();

                setCloseMeditionInput(true);

                //serve parar resetar o estado dos componentes sem o useeffect.
                setResetKey(prev => prev + 1);
                setAddHabit(false);


            } catch (error) {
                console.error("Erro ao registrar Hábitos:", error);
                showAlert("error", "Erro ao registrar Hábitos");
            }
        }

        if (editing) {

            try {

                await editHabits(lastHabit.id, CPF, data);
                const updatedHabits = await getHabits(CPF);
                setHabit(updatedHabits);
                showAlert("success", "Hábitos editados com sucesso");

                handleClearInputs();

                setCloseMeditionInput(true);

                //serve parar resetar o estado dos componentes sem o useeffect.
                setResetKey(prev => prev + 1);
                setEditing(false);


            } catch (error) {
                console.error("Erro ao editar Hábitos:", error);
                showAlert("error", "Erro ao editar Hábitos");
            }
        }

    }

    function handleDataEditing() {
        setHabitInputs({
            timeExercise: lastHabit.minutosExercicio,
            timeSleep: lastHabit.horasSono,
            date: lastHabit.dataReferencia
        });
    }

    function handleClearInputs() {
        setHabitInputs({
            timeExercise: "",
            timeSleep: "",
            date: "",
        });
    }

    useEffect(() => {
        async function fetchVitals() {
            const CPF = localStorage.getItem("CPF")
            try {
                const data = await getHabits(CPF);
                setHabit(data);
                console.log("habitos obtidos:", data);
            } catch (error) {
                console.error("Erro ao buscar Hábitos:", error);
            }
        }

        fetchVitals();
    }, []);

    const hasUnsavedChanges =
        habitInputs.timeSleep ||
        habitInputs.timeExercise ||
        habitInputs.date;

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

                {(addHabit || editing) ? (
                    <Box display="flex" gap={1}>

                        <ButtonUI onClick={() => {
                            setEditing(false);
                            setAddHabit(false);
                            setErrorEx(false);
                            setErrorSl(false);
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
                        <ButtonUI onClick={() => {
                            setAddHabit(true);
                            handleClearInputs();
                        }}>
                            + Adicionar Hábitos
                        </ButtonUI>

                        <ButtonUI onClick={() => {
                            setEditing(true);
                            handleDataEditing();
                        }}>
                            Editar Hábitos
                        </ButtonUI>
                    </Box>
                )}
            </Box>

            <Grid container spacing={3}>
                <Grid item xs={12} md={4}>
                    <HabitCard
                        showInput={editing || addHabit}
                        icon={<BedtimeIcon />}
                        title="Tempo de sono"
                        error={(error && !habitInputs.timeSleep) || errorSl}
                        type="number"
                        value={lastHabit ? lastHabit.horasSono : "N/A"}
                        unit="Horas"
                        date={lastHabit ? new Date(lastHabit.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={habitInputs.timeSleep}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setHabitInputs({ ...habitInputs, timeSleep: e.target.value }, setErrorSl(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <HabitCard
                        showInput={editing || addHabit}
                        icon={<FitnessCenterIcon />}
                        title="Tempo de exercício"
                        error={(error && !habitInputs.timeExercise) || errorEx}
                        type="number"
                        value={lastHabit ? lastHabit.minutosExercicio : "N/A"}
                        unit="Minutos"
                        date={lastHabit ? new Date(lastHabit.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={habitInputs.timeExercise}
                        closeMeditionInput={closeMeditionInput}
                        onInputChange={(e) => setHabitInputs({ ...habitInputs, timeExercise: e.target.value }, setErrorEx(false))}
                        key={resetKey}
                    />
                </Grid>

                <Grid item xs={12} md={4}>
                    <HabitCard
                        showInput={editing || addHabit}
                        icon={<CalendarIcon />}
                        title="Data"
                        error={(error && !habitInputs.date) || errorDate}
                        // type="number"
                        value={lastHabit ? lastHabit.dataReferencia : "N/A"}
                        // unit="yyyy-MM-dd"
                        date={lastHabit ? new Date(lastHabit.dataRegistro).toLocaleString() : "N/A"}
                        inputValue={habitInputs.date}
                        closeMeditionInput={closeMeditionInput}
                        // onInputChange={(e) => setHabitInputs({ ...habitInputs, date: e.target.value }, setErrorDate(false))}
                        onInputChange={(newValue) => {
                            setHabitInputs({ ...habitInputs, date: newValue });
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
