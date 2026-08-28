import {
    Box,
    Typography,
    Chip,
    IconButton
} from "@mui/material";
import { useTheme } from "@mui/material/styles";

import DeleteIcon from "@mui/icons-material/Delete";

import { useI18n } from "../../../src/i18n";

export default function MemberCard({
    link,
    typeStyle,
    onRemove,
    highlight = false,
    hideRemove = false
}) {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t, formatDate } = useI18n();

    const getIniciais = (nome) => {
        if (!nome) return "";

        const partes = nome.trim().split(" ").filter(Boolean);

        if (partes.length === 1) {
            return partes[0][0].toUpperCase();
        }

        const primeira = partes[0][0];
        const ultima = partes[partes.length - 1][0];

        return (primeira + ultima).toUpperCase();
    };

    return (

        <Box
            sx={{
                backgroundColor: "background.paper",
                borderRadius: "24px",
                padding: 3,
                marginBottom: 2,
                minWidth: 0,
                overflow: "hidden",
                boxShadow: vitta.shadow,

                border: highlight
                    ? `2px solid ${theme.palette.secondary.main}`
                    : "1px solid",
                borderColor: highlight
                    ? theme.palette.secondary.main
                    : vitta.border
            }}
        >

            <Box
                display="flex"
                flexDirection={{
                    xs: "column",
                    sm: "row"
                }}
                justifyContent="space-between"
                alignItems={{
                    xs: "flex-start",
                    sm: "center"
                }}
                gap={2}
                sx={{
                    minWidth: 0
                }}
            >

                <Box
                    display="flex"
                    alignItems="center"
                    gap={2}
                    sx={{
                        minWidth: 0,
                        width: "100%"
                    }}
                >

                    <Box
                        sx={{
                            width: 52,
                            height: 52,
                            fontSize: "1rem",
                            boxShadow: isDark
                                ? "0 10px 20px rgba(0, 0, 0, 0.24)"
                                : "0 10px 20px rgba(22, 163, 74, 0.22)",
                            background: highlight
                                ? "linear-gradient(135deg, #16a34a 0%, #0f766e 72%, #0ea5e9 100%)"
                                : isDark
                                    ? "rgba(34, 197, 94, 0.16)"
                                    : "rgba(22, 163, 74, 0.14)",
                            color: highlight
                                ? "#ffffff"
                                : isDark
                                    ? "#bbf7d0"
                                    : "#14532d",
                            fontWeight: 800,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            flex: "0 0 auto",
                            borderRadius: "50%",
                            border: highlight
                                ? "1px solid rgba(255, 255, 255, 0.22)"
                                : `1px solid ${vitta.borderStrong}`,
                        }}
                    >

                        {getIniciais(link.nome)}

                    </Box>

                    <Box
                        sx={{
                            minWidth: 0
                        }}
                    >

                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 700,
                                fontSize: "1rem",
                                minWidth: 0,
                                overflowWrap: "anywhere",
                                wordBreak: "break-word"
                            }}
                        >
                            {link.nome}
                        </Typography>

                        <Chip
                            label={typeStyle.label}
                            size="small"
                            sx={{
                                mt: 1,
                                width: "fit-content",

                                backgroundColor:
                                    typeStyle.background,

                                color:
                                    typeStyle.color,

                                fontWeight: 600
                            }}
                        />

                        <Typography
                            sx={{
                                color: "text.secondary",
                                fontSize: ".9rem",
                                minWidth: 0,
                                overflowWrap: "anywhere",
                                wordBreak: "break-word"
                            }}
                        >
                            {link.email}
                        </Typography>

                        {link.conselho && (

                            <Typography
                                sx={{
                                    color: "text.secondary",
                                    fontSize: ".9rem",
                                    minWidth: 0,
                                    overflowWrap: "anywhere",
                                    wordBreak: "break-word"
                                }}
                            >
                                {link.conselho}
                            </Typography>

                        )}

                        <Typography
                            sx={{
                                color: "text.secondary",
                                fontSize: ".85rem",
                                mt: 1,
                                minWidth: 0,
                                overflowWrap: "anywhere",
                                wordBreak: "break-word"
                            }}
                        >
                            {t("patientHub.linkedAt")} {formatDate(link.criadoEm)}
                        </Typography>

                    </Box>

                </Box>

                {!hideRemove && onRemove && (

                    <IconButton
                        color="error"
                        onClick={onRemove}
                        sx={{
                            flex: "0 0 auto"
                        }}
                    >
                        <DeleteIcon />
                    </IconButton>

                )}

            </Box>

        </Box>
    );
}
