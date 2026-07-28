import {
    Card,
    CardContent,
    Typography,
    Stack,
    Switch,
    Box,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";

import NotificationsNoneIcon from "@mui/icons-material/NotificationsNone";
import AccessTimeIcon from "@mui/icons-material/AccessTime";

import { useI18n } from "../../src/i18n";

export default function ReminderList({
    reminder,
    handleToggleReminder,
}) {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();

    if (
        !reminder?.diasSemana ||
        !reminder?.horario
    ) {
        return null;
    }

    const item = reminder;

    const translatedDays =
        item.diasSemana
            .split(",")
            .map(day => t(`healthTracker.reminders.days.${day}`))
            .join(", ");

    return (
        <Card
            key={item.id}
            sx={{
                mt: 1.5,
                width: "100%",
                borderRadius: "20px",
                backgroundColor: isDark ? "rgba(220, 252, 231, 0.08)" : "#F3F5F4",

                border: item.ativo
                    ? "1.5px solid"
                    : "1px solid",
                borderColor: item.ativo ? "primary.main" : vitta.border,

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
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                    }}
                >
                    <NotificationsNoneIcon
                        sx={{
                            color: item.ativo
                                ? "primary.main"
                                : "text.secondary"
                        }}
                    />

                    <Typography fontWeight="bold">
                        {t("healthTracker.reminders.measurement")}
                    </Typography>

                    <Switch
                        checked={item.ativo}
                        onChange={() =>
                            handleToggleReminder()
                        }
                        color="success"
                    />
                </Box>

                <Stack
                    direction="row"
                    spacing={1}
                    alignItems="center"
                    mt={1}
                >
                    <AccessTimeIcon
                        sx={{
                            fontSize: 18,
                            color: "text.secondary",
                        }}
                    />

                    <Typography color="text.secondary">
                        {item.horario.slice(0, 5)}
                    </Typography>

                    <Typography color="text.secondary">
                        •
                    </Typography>

                    <Typography color="text.secondary">
                        {translatedDays}
                    </Typography>
                </Stack>
            </CardContent>
        </Card>
    );
}
