import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Chip from "@mui/material/Chip";
import ButtonUI from "./Button";
import InputUI from "./Input";

export default function HabitCard({
    icon,
    title,
    description,
    isDone,
}) {
    return (
        <Card
            sx={{
                borderRadius: 4,
                p: 1,
                height: "100%",
                boxShadow: "0 2px 10px rgba(0,0,0,0.05)",
            }}
        >
            <CardContent
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    mt: 2
                }}
            >
                {/* Ícone */}
                <Box
                    sx={{
                        width: 50,
                        height: 50,
                        borderRadius: 3,
                        background: "#e0e0e0",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        mb: 2,
                    }}
                >
                    {icon}
                </Box>

                {/* Título */}
                <Typography variant="h6" fontWeight="bold">
                    {title}
                </Typography>

                {/* Descrição */}
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                    {description}
                </Typography>

                {/* STATUS */}
                {isDone && (
                    <Chip
                        label="✓ Registrado hoje"
                        sx={{
                            mt: 2,
                            backgroundColor: "#e8f5e9",
                            color: "#2e7d32",
                            fontWeight: "bold",
                        }}
                    />
                )}

                {/* Botão */}
                <ButtonUI
                    fullWidth
                    minWidth="6px"
                    variant={isDone ? "outlined" : "contained"}
                >
                    + Registrar
                </ButtonUI>

            </CardContent>
        </Card >
    );
}