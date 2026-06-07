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
import { Navigate, useLocation, useNavigate } from "react-router-dom";

import Chip from "@mui/material/Chip";

export default function Navbar({ selectedPatient }) {
    const navigate = useNavigate();
    const location = useLocation();

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

    const isPublicPage =
        location.pathname === "/" ||
        location.pathname === "/login" ||
        location.pathname === "/register" ||
        location.pathname === "/entrar";

    return (

        <AppBar

            position="fixed"
            elevation={1}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 2,
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
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 1,
                        flexShrink: 0
                    }}
                >

                    <Typography
                        sx={{
                            fontSize: {
                                xs: "1.3rem",
                                sm: "1.6rem",
                                md: "2rem"
                            },
                            fontWeight: 700,
                            color: "#5A9B4D",
                            lineHeight: 1,
                        }}
                    >
                        VittaSync
                    </Typography>
                </Box>

                {!isPublicPage && <Box
                    sx={{
                        flex: 1,
                        minWidth: 0,
                        display: "flex",
                        justifyContent: "center"
                    }}
                >
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            gap: 1.5,
                            minWidth: 0
                        }}
                    >
                        <Typography
                            sx={{
                                fontWeight: 700,
                                fontSize: "1rem",
                                maxWidth: {
                                    xs: 120,
                                    sm: 180,
                                    md: "none"
                                },
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap"
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
                </Box>}

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