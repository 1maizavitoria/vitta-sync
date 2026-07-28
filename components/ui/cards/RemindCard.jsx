import { useEffect, useState } from "react";
import { Box, Card, CardContent, Stack, Typography, ToggleButton, ToggleButtonGroup } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { LocalizationProvider, TimePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { activateReminder, deactivateReminder, getReminder, registerReminder } from "../../../services/reminderService";
import ReminderList from "../RemindList";
import ButtonUI from "../Button";
import DialogUI from "../Dialog";
import { useAlert } from "../../../hooks/useAlert";
import { usePatient } from "../../../context/PatientContext";
import AddIcon from "@mui/icons-material/Add";
import {
    Checkbox,
    FormControlLabel
} from "@mui/material";
import { useI18n } from "../../../src/i18n";

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
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { selectedPatient } = usePatient();
    const { t } = useI18n();

    const { showAlert } = useAlert();

    const [open, setOpen] = useState(false);

    const [selectedDay, setSelectedDay] = useState([]);

    const [time, setTime] = useState(null);

    const [notificationType,
        setNotificationType] =
        useState("EMAIL");

    const [reminder, setReminder] = useState(null);

    const [error, setError] = useState(false);

    const userType =
        localStorage.getItem("tipo");

    const canEdit =
        userType !== "saude";

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

        if (
            selectedDay.length === 0 ||
            !time
        ) {
            showAlert("error", t("healthTracker.reminders.chooseDayAndTime"));
            setError(true);
            return false;
        }

        if (
            selectedDay.length === 0 ||
            !time
        ) {

            showAlert(
                "error",
                t("healthTracker.reminders.chooseDayAndTime")
            );

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
            ativo: true,
            enviarEmail: notificationType === "EMAIL" || notificationType === "AMBOS",
            enviarSms: notificationType === "SMS" || notificationType === "AMBOS",
        };

        try {
            await registerReminder(selectedPatient.cpf, data);
            const dataReminder = await getReminder(selectedPatient.cpf);
            setReminder(dataReminder);
        } catch (error) {
            console.error("Erro ao salvar lembrete:", error);
        }

        setOpen(false);
    };

    useEffect(() => {

        if (!selectedPatient) return;

        async function fetchReminder() {

            try {

                const data =
                    await getReminder(
                        selectedPatient.cpf
                    );

                setReminder(data);

            } catch (error) {

                if (
                    error.response?.status === 404
                ) {

                    setReminder(null);

                    return;
                }

                console.error(
                    "Erro ao buscar lembrete:",
                    error
                );
            }
        }

        fetchReminder();

    }, [selectedPatient]);



    async function handleToggleReminder() {

        if (!reminder) return;

        try {

            const newStatus =
                !reminder.ativo;

            if (newStatus) {

                await activateReminder(selectedPatient.cpf);

            } else {

                await deactivateReminder(selectedPatient.cpf);
            }

            setReminder(prev => ({
                ...prev,
                ativo: newStatus
            }));

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
                    width: "100%",
                    maxWidth: { xs: "100%", lg: 340 },
                    borderRadius: 3,
                    border: "1px solid",
                    borderColor: vitta.border,
                    boxShadow: vitta.shadow,
                    bgcolor: "background.paper",
                    p: 1,
                    minWidth: 0
                }}>

                <CardContent>

                    <Typography
                        variant="h6"
                        sx={{
                            fontWeight: 800,
                            color: "text.primary"
                        }}
                    >
                        {t("healthTracker.reminders.title")}
                    </Typography>

                    <ReminderList reminder={reminder} handleToggleReminder={handleToggleReminder} />

                    {canEdit && <ButtonUI
                        fullWidth
                        onClick={() => setOpen(true)}
                        sx={{
                            mt: 2,
                            width: "100%",
                            justifyContent: "center"
                        }}
                        startIcon={<AddIcon />}
                    >
                        {t("healthTracker.reminders.new")}
                    </ButtonUI>}

                </CardContent>
            </Card>

            <DialogUI
                open={open}
                onClose={() => setOpen(false)}
                title={t("healthTracker.reminders.addTitle")}
                onConfirm={handleSave}
                confirmText={t("healthTracker.common.save")}
                cancelText={t("healthTracker.common.cancel")}
            >
                <Stack spacing={4} mt={1}>

                    <Box>
                        <Typography mb={2} fontWeight="bold">
                            {t("healthTracker.reminders.weekDays")}
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
                                            background: isDark
                                                ? "linear-gradient(90deg, rgba(34, 197, 94, 0.18) 0%, rgba(14, 165, 233, 0.14) 100%)"
                                                : "linear-gradient(90deg, #c6eee6 0%, #b6d98e 100%)",
                                            color: "text.primary",
                                            borderColor: "primary.main",
                                        },

                                        "&.Mui-selected:hover": {
                                            background: isDark
                                                ? "linear-gradient(90deg, rgba(34, 197, 94, 0.24) 0%, rgba(14, 165, 233, 0.2) 100%)"
                                                : "linear-gradient(90deg, #d4f5ee 0%, #c6e7a7 100%)",
                                        },
                                    }}
                                >
                                    {t(`healthTracker.reminders.days.${day.value}`)}
                                </ToggleButton>
                            ))}
                        </ToggleButtonGroup>
                    </Box>

                    <Box>
                        <Typography mb={2} fontWeight="bold">
                            {t("healthTracker.reminders.time")}
                        </Typography>

                        <TimePicker
                            label={t("healthTracker.reminders.chooseTime")}
                            value={time}
                            onChange={(newValue) => {
                                setTime(newValue);
                                setError(false);
                            }}
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
                    <Box>

                        <Typography
                            mb={2}
                            fontWeight="bold"
                        >
                            {t("healthTracker.reminders.channel")}
                        </Typography>

                        <ToggleButtonGroup
                            value={notificationType}
                            exclusive
                            onChange={(_, newValue) => {

                                if (newValue) {

                                    setNotificationType(
                                        newValue
                                    );
                                }
                            }}
                            fullWidth
                        >

                            <ToggleButton value="EMAIL">
                                Email
                            </ToggleButton>

                            <ToggleButton value="SMS">
                                SMS
                            </ToggleButton>

                            <ToggleButton value="AMBOS">
                                {t("healthTracker.reminders.both")}
                            </ToggleButton>

                        </ToggleButtonGroup>

                    </Box>

                </Stack>
            </DialogUI>
        </LocalizationProvider>
    );
}
