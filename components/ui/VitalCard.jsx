import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import InputUI from "./Input";
import ButtonUI from "./Button";
import { WidthFull } from "@mui/icons-material";
import { Grid } from "@mui/material";
import { useState } from "react";

export default function VitalCard({
    icon,
    title,
    value,
    type,
    error = false,
    unit,
    date,
    inputValue,
    onInputChange,
}) {

    const [showInput, setShowInput] = useState(false);

    return (
        <Grid item xs={12} md={4} sx={{ display: "flex" }}>

            <Card
                sx={{
                    borderRadius: 4,
                    p: 1,
                    height: "100%",
                    width: "100%",
                    boxShadow: "0 2px 10px rgba(0,0,0,0.05)",

                    border: error ? "2px solid #d32f2f" : "none", // vermelho MUI
                    backgroundColor: error ? "#fdecea" : "#fff", // leve fundo vermelho
                    transition: "all 0.3s ease"
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
                            background: "linear-gradient(135deg, #a8e6cf, #dcedc1)",
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

                    {/* Valor */}
                    <Typography variant="h5" fontWeight="bold" sx={{ mt: 1 }}>
                        {value} {unit}
                    </Typography>

                    {/* Sub */}
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                        Última medição
                    </Typography>

                    {/* Data */}
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                        {date}
                    </Typography>

                    {/* Botão */}

                    {!showInput && <ButtonUI onClick={() => setShowInput(true)} >
                        + Adicionar Medição
                    </ButtonUI>}

                    {showInput && <InputUI
                        value={inputValue}
                        type={type}
                        onChange={onInputChange}
                        error={error}
                    >
                    </InputUI>}

                </CardContent>
            </Card>
        </Grid>
    );
}