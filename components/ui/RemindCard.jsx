import React, { useEffect, useState } from "react";
import { Box, Button, Card, CardContent, Dialog, DialogTitle, DialogContent, DialogActions, Stack, Typography, ToggleButton, ToggleButtonGroup } from "@mui/material";
import { LocalizationProvider, TimePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { activateReminder, deactivateReminder, getReminder, registerReminder } from "../../services/reminderService";
import ReminderList from "./RemindList";
import ButtonUI from "./Button";
import DialogUI from "./Dialog";
import { useAlert } from "../../hooks/useAlert";

const weekDays = [
    { label: "Dom", value: "Sunday" },
    { label: "Seg", value: "Monday" },
    { label: "Ter", value: "Tuesday" },
    { label: "Qua", value: "Wednesday" },
    { label: "Qui", value: "Thursday" },
    { label: "Sex", value: "Friday" },
    { label: "Sáb", value: "Saturday" },
];



export default function ReminderCard() {
    const { showAlert } = useAlert();

    const [open, setOpen] = useState(false);

    const [selectedDay, setSelectedDay] = useState([]);

    const [time, setTime] = useState(null);

    const [reminder, setReminder] = useState([]);

    const [error, setError] = useState(false);

    function sortWeekDays(days) {

        const daysOrder = [
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday",
        ];

        return [...days].sort(
            (a, b) =>
                daysOrder.indexOf(a) -
                daysOrder.indexOf(b)
        );
    }

    const handleDays = (_, newDays) => {
        setSelectedDay(newDays);

        if (newDays.length > 0) {
            setError(false);
        }
    };

    function canSave() {

        if (!selectedDay || !time) {
            showAlert("error", "Escolha um dia e horário para o lembrete.");
            setError(true);
            return false;
        }

        return true;
    }

    async function handleSave() {
        if (!canSave()) return;
        const data = {

            diasSemana: sortWeekDays(selectedDay).join(","),
            horario: time.format("HH:mm"),
            ativo: true
        };

        try {
            const response = await registerReminder(data);
            const dataReminder = await getReminder();
            setReminder([dataReminder]);
            console.log(response);
        } catch (error) {
            console.log(error);
        }

        setOpen(false);
    };

    useEffect(() => {

        async function fetchReminder() {
            try {
                const data = await getReminder();
                setReminder([data]);
                console.log("Lembretes obtidos:", data);
            } catch (error) {
                console.error("Erro ao buscar lembretes:", error);
            }
        }

        fetchReminder();
    }, []);

    async function handleToggleReminder(id) {

        try {
            const reminderItem = reminder.find(
                (item) => item.id === id
            );

            if (!reminderItem) return;

            const newStatus = !reminderItem.ativo;

            if (newStatus) {
                await activateReminder();
            } else {
                await deactivateReminder();
            }

            setReminder((prev) =>
                prev.map((item) =>
                    item.id === id
                        ? { ...item, ativo: newStatus }
                        : item
                )
            );

        } catch (error) {
            console.error(
                "Erro ao alternar lembrete:",
                error
            );
        }
    }

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Card
                sx={{
                    width: 280,
                    borderRadius: 4,
                    p: 1
                }}>

                <CardContent>
                    <Typography variant="h6" fontWeight="bold">
                        Lembretes
                    </Typography>

                    <ReminderList reminder={reminder} handleToggleReminder={handleToggleReminder} />

                    <ButtonUI
                        fullWidth
                        onClick={() => setOpen(true)}
                        sx={{
                            mt: 2,
                        }}
                    >
                        + Novo Lembrete
                    </ButtonUI>
                </CardContent>
            </Card>

            <DialogUI
                open={open}
                onClose={() => setOpen(false)}
                title="Adicionar Lembrete"
                onConfirm={handleSave}
                confirmText="Salvar"
                cancelText="Cancelar"
            >
                <Stack spacing={4} mt={1}>

                    <Box>
                        <Typography mb={2} fontWeight="bold">
                            Dias da semana
                        </Typography>

                        <ToggleButtonGroup
                            value={selectedDay}
                            onChange={handleDays}

                            // exclusive
                            fullWidth
                            sx={{
                                border: error
                                    ? "1px solid #d32f2f"
                                    : "1px solid transparent",
                                borderRadius: "8px",
                                p: 0.5,
                            }}
                        >
                            {weekDays.map((day) => (
                                <ToggleButton
                                    key={day.value}
                                    value={day.value}
                                    sx={{
                                        textTransform: "none",

                                        "&.Mui-selected": {
                                            background:
                                                "linear-gradient(90deg, #c6eee6 0%, #b6d98e 100%)",
                                            color: "#2b2b2b",
                                            borderColor: "#8fcf7a",
                                        },

                                        "&.Mui-selected:hover": {
                                            background:
                                                "linear-gradient(90deg, #d4f5ee 0%, #c6e7a7 100%)",
                                        },
                                    }}
                                >
                                    {day.label}
                                </ToggleButton>
                            ))}
                        </ToggleButtonGroup>
                    </Box>

                    <Box>
                        <Typography mb={2} fontWeight="bold">
                            Horário
                        </Typography>

                        <TimePicker
                            label="Escolha o horário"
                            value={time}
                            onChange={(newValue) => (setTime(newValue), setError(false))}
                            ampm={false}
                            format="HH:mm"
                            slotProps={{
                                textField: {
                                    error: error,
                                    fullWidth: true,
                                },
                            }}
                        />
                    </Box>

                </Stack>
            </DialogUI>
        </LocalizationProvider>
    );
}