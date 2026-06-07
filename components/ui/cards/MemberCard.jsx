import {
    Box,
    Typography,
    Chip,
    IconButton
} from "@mui/material";

import DeleteIcon from "@mui/icons-material/Delete";

import { formatDate } from "../../../utils/formatters/formatDate";

export default function MemberCard({
    link,
    typeStyle,
    onRemove,
    highlight = false,
    hideRemove = false
}) {

    return (

        <Box
            sx={{
                backgroundColor: "#fff",
                borderRadius: "24px",
                padding: 3,
                marginBottom: 2,
                boxShadow:
                    "0px 4px 15px rgba(0,0,0,0.08)",

                border: highlight
                    ? "2px solid #00b7ff"
                    : "1px solid #f0f0f0"
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
            >

                <Box
                    display="flex"
                    alignItems="center"
                    gap={2}
                >

                    <Box
                        sx={{
                            width: 52,
                            height: 52,
                            fontSize: "1rem",
                            boxShadow: "0 4px 10px rgba(0,0,0,0.12)",
                            background:
                                "linear-gradient(90deg, #69f08a, #F3FFE8)",
                            color: "#1B1B1B",
                            fontWeight: 700,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            borderRadius: "16px",
                        }}
                    >

                        {link.nome
                            ?.substring(0, 2)
                            .toUpperCase()}

                    </Box>

                    <Box>

                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 700,
                                fontSize: "1rem",
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
                                color: "#888",
                                fontSize: ".9rem"
                            }}
                        >
                            {link.email}
                        </Typography>

                        {link.conselho && (

                            <Typography
                                sx={{
                                    color: "#888",
                                    fontSize: ".9rem"
                                }}
                            >
                                {link.conselho}
                            </Typography>

                        )}

                        <Typography
                            sx={{
                                color: "#999",
                                fontSize: ".85rem",
                                mt: 1
                            }}
                        >
                            Vinculado em {formatDate(link.criadoEm)}
                        </Typography>

                    </Box>

                </Box>

                {!hideRemove && onRemove && (

                    <IconButton
                        color="error"
                        onClick={onRemove}
                    >
                        <DeleteIcon />
                    </IconButton>

                )}

            </Box>

        </Box>
    );
}