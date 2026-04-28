import {
    Card,
    CardContent,
    Typography,
    Stack,
    Switch,
    Box,
} from "@mui/material";

import NotificationsNoneIcon from "@mui/icons-material/NotificationsNone";
import AccessTimeIcon from "@mui/icons-material/AccessTime";

const daysMap = {
    MONDAY: "Segunda-feira",
    TUESDAY: "Terça-feira",
    WEDNESDAY: "Quarta-feira",
    THURSDAY: "Quinta-feira",
    FRIDAY: "Sexta-feira",
    SATURDAY: "Sábado",
    SUNDAY: "Domingo",
};

export default function ReminderList({
    reminder,
    handleToggleReminder,
}) {

    return (
        <>
            {reminder.map((item) => (
                <Card
                    key={item.id}
                    sx={{
                        mt: 1.5,
                        fullwidth: true,
                        borderRadius: "20px",
                        backgroundColor: "#F3F5F4",
                        border: item.ativo
                            ? "1.5px solid #7BE0A7"
                            : "1px solid #D9D9D9",
                        boxShadow: "none",
                        p: 0.5,
                    }}
                >
                    <CardContent
                        sx={{
                            p: 1.5,
                            "&:last-child": {
                                pb: 1.5,
                            },
                        }}
                    >
                        <Box
                            sx={{
                                alignContent: "center",
                                display: "flex",
                                justifyContent: "space-between",
                            }}
                        >
                            <NotificationsNoneIcon
                                sx={{
                                    color: item.ativo
                                        ? "#00B26F"
                                        : "#9CA3AF"
                                }}
                            />

                            <Typography
                                fontWeight="bold"
                            >
                                Fazer medição
                            </Typography>

                            <Switch
                                checked={item.ativo}
                                onChange={() =>
                                    handleToggleReminder(item.id)
                                }
                                color="success"
                            />
                        </Box>


                        <Stack
                            direction="row"
                            justifyContent="space-between"
                            alignItems="flex-start"
                        >
                            <Stack
                                direction="row"
                                spacing={1.5}
                                alignItems="flex-start"
                            >

                                <Box>
                                    <Stack
                                        direction="row"
                                        spacing={2}
                                        alignItems="center"
                                        mt={1}
                                    >
                                        <AccessTimeIcon
                                            sx={{
                                                fontSize: 18,
                                                color: "#6B7280",
                                            }}
                                        />

                                        <Typography
                                            color="text.secondary"
                                        >
                                            {item.horario.slice(0, 5)}
                                        </Typography>

                                        <Typography
                                            color="text.secondary"
                                        >
                                            •
                                        </Typography>

                                        <Typography
                                            color="text.secondary"
                                        >
                                            {daysMap[item.diasSemana]}
                                        </Typography>
                                    </Stack>
                                </Box>
                            </Stack>
                        </Stack>
                    </CardContent>
                </Card>
            ))}
        </>
    );
}