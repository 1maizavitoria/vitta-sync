import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import InputUI from "./Input";
import { Grid } from "@mui/material";
import DatePickerUI from "./DatePicker";
// import { useState } from "react";

export default function HabitCard({
    icon,
    title,
    value,
    type,
    error = false,
    unit,
    date,
    dataPicker,
    inputValue,
    onInputChange,
    showInput
}) {

    // const [showInput, setShowInput] = useState(false);

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
                        Último registro
                    </Typography>

                    {/* Data */}
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                        {date}
                    </Typography>

                    {/* Botão */}

                    {showInput && !dataPicker && <InputUI
                        value={inputValue}
                        type={type}
                        onChange={onInputChange}
                        error={error}
                    >
                    </InputUI>}

                    {showInput && dataPicker && <DatePickerUI
                        value={inputValue}
                        onChange={onInputChange}
                        error={error}
                    >
                    </DatePickerUI>}

                    {/* <DatePickerUI
                        label="Data de nascimento"
                        dateLimit={dateLimit}
                        error={error && birthDate == null}
                        value={birthDate}
                        onChange={setBirthDate}
                    /> */}

                </CardContent>
            </Card>
        </Grid>
    );
}