import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import { useTheme } from "@mui/material/styles";

import DatePickerUI from "../DatePicker";
import InputUI from "../Input";
import { useI18n } from "../../../src/i18n";

export default function HabitCard({
    icon,
    title,
    value,
    type,
    error = false,
    unit,
    date,
    dataPicker,
    userName,
    userFunction,
    inputValue,
    onInputChange,
    userStyle,
    showInput
}) {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();

    return (
        <Card
            sx={{
                borderRadius: 3,
                height: "100%",
                width: "100%",
                minWidth: 0,
                boxShadow: vitta.shadow,
                border: error
                    ? "1px solid"
                    : "1px solid",
                borderColor: error ? "error.main" : vitta.border,
                backgroundColor: error
                    ? isDark ? "rgba(220, 38, 38, 0.12)" : "#fef2f2"
                    : "background.paper",
                transition: "transform .2s ease, box-shadow .2s ease, border-color .2s ease",

                "&:hover": {
                    transform: "translateY(-2px)",
                    boxShadow: vitta.shadow,
                    borderColor: error ? "error.main" : vitta.borderStrong
                }
            }}
        >
            <CardContent
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    height: "100%",
                    p: 2.5,
                    minWidth: 0
                }}
            >
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between",
                        gap: 1.5,
                        mb: 2,
                        minWidth: 0
                    }}
                >
                    <Box
                        sx={{
                            width: 46,
                            height: 46,
                            borderRadius: 2,
                            background: isDark
                                ? "linear-gradient(135deg, rgba(34, 197, 94, 0.18) 0%, rgba(14, 165, 233, 0.14) 100%)"
                                : "linear-gradient(135deg, #dcfce7 0%, #e0f2fe 100%)",
                            color: isDark ? "#bbf7d0" : "#166534",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            flex: "0 0 auto"
                        }}
                    >
                        {icon}
                    </Box>

                    <Box sx={{ minWidth: 0, flex: 1 }}>
                        <Typography
                            sx={{
                                fontSize: "0.92rem",
                                fontWeight: 800,
                                color: "text.primary",
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap"
                            }}
                        >
                            {title}
                        </Typography>

                        <Typography
                            sx={{
                                mt: 0.5,
                                color: "text.secondary",
                                fontSize: "0.78rem"
                            }}
                        >
                            {t("healthTracker.common.lastRecord")}
                        </Typography>
                    </Box>
                </Box>

                <Typography
                    sx={{
                        fontSize: { xs: "1.55rem", md: "1.75rem" },
                        lineHeight: 1.1,
                        fontWeight: 900,
                        color: "text.primary",
                        overflowWrap: "anywhere"
                    }}
                >
                    {value} {unit}
                </Typography>

                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        mt: 1,
                        overflowWrap: "anywhere"
                    }}
                >
                    {date}
                </Typography>

                {userName && (
                    <Typography
                        variant="body2"
                        sx={{
                            mt: 1,
                            fontWeight: 700,
                            overflowWrap: "anywhere"
                        }}
                    >
                        {userName}
                        {" • "}
                        <Box
                            component="span"
                            sx={{
                                color: userStyle?.color,
                                backgroundColor:
                                    userStyle?.background,
                                px: 1,
                                py: 0.3,
                                borderRadius: 2,
                                ml: 0.5,
                                display: "inline-block"
                            }}
                        >
                            {userFunction}
                        </Box>
                    </Typography>
                )}

                {showInput && !dataPicker && (
                    <Box sx={{ mt: 2 }}>
                        <InputUI
                            value={inputValue}
                            type={type}
                            onChange={onInputChange}
                            error={error}
                        />
                    </Box>
                )}

                {showInput && dataPicker && (
                    <Box sx={{ mt: 2 }}>
                        <DatePickerUI
                            value={inputValue}
                            onChange={onInputChange}
                            error={error}
                        />
                    </Box>
                )}
            </CardContent>
        </Card>
    );
}
