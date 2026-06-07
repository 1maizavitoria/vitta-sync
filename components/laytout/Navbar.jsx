import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import Badge from "@mui/material/Badge";
import Button from "@mui/material/Button";

import NotificationsIcon from "@mui/icons-material/Notifications";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import MenuIcon from '@mui/icons-material/Menu';

import ButtonUI from "../ui/Button";
import { usePatient } from "../../context/PatientContext";
import { Navigate, useLocation, useNavigate } from "react-router-dom";

import Chip from "@mui/material/Chip";

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const { selectedPatient } = usePatient();
    console.log("Paciente selecionado no Navbar:", selectedPatient);
    const currentUser = {
        nome: localStorage.getItem("nome"),
        tipo: localStorage.getItem("tipo")
    };

    const displayName =
        selectedPatient?.nome ||
        currentUser.nome;

    const displayType = selectedPatient?.tipo
        ? selectedPatient.tipo
        : selectedPatient
            ? "paciente"
            : currentUser.tipo;

    const displayTypeLabel = {
        paciente: "Paciente",
        responsavel: "Responsável",
        saude: "Saúde"
    }[displayType] || displayType;

    return (

        <AppBar
            position="fixed"
            elevation={1}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                backgroundColor: "#fff",
                color: "#000"
            }}
        >
            <Toolbar
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                }}
            >
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>

                    <Typography
                        sx={{
                            fontSize: "2rem",
                            fontWeight: 700,
                            color: "#5A9B4D",
                            lineHeight: 1,
                        }}
                    >
                        VittaSync
                    </Typography>
                </Box>

                <Box>
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            gap: 1.5
                        }}
                    >
                        <Typography
                            sx={{
                                fontWeight: 700,
                                fontSize: "1rem"
                            }}
                        >
                            {displayName}
                        </Typography>

                        <Chip
                            label={displayTypeLabel}
                            size="small"
                            sx={{
                                fontWeight: 600
                            }}
                        />
                    </Box>
                </Box>

                {/* DIREITA */}
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>

                    {location.pathname === "/" && <ButtonUI
                        variant="outlined"
                        sx={{
                            textTransform: "none",
                            borderRadius: 3,
                        }}
                        onClick={() => navigate("/login")}
                    >
                        Entrar / Registrar
                    </ButtonUI>}



                </Box>
            </Toolbar>
        </AppBar>
    );
}