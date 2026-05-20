import { useLocation, useNavigate } from "react-router-dom";

import Drawer from "@mui/material/Drawer";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import Avatar from "@mui/material/Avatar";

import DashboardIcon from "@mui/icons-material/Dashboard";
import DescriptionIcon from "@mui/icons-material/Description";
import LinkIcon from "@mui/icons-material/Link";
import ShowChartIcon from "@mui/icons-material/ShowChart";
import IconButton from "@mui/material/IconButton";
import LogoutIcon from "@mui/icons-material/Logout";

import { getUserByCpf } from "../../services/userService";
import { useEffect, useState } from "react";
import { logout } from "../../services/authService";
import { Button } from "@mui/material";
import ButtonUI from "../ui/Button";

import { usePatient } from "../../context/PatientContext";
import { Refresh } from "@mui/icons-material";

const menuItems = [
    { label: "Grupo", icon: <DashboardIcon />, path: "/dashboard" },
    // { label: "Registros", icon: <DescriptionIcon />, path: "/health-tracker" },
    // { label: "Vínculos", icon: <LinkIcon />, path: "/links" },
    // { label: "Relatórios", icon: <ShowChartIcon />, path: "/reports" },
];

export default function Sidebar({ open, setOpen }) {

    const {
        patients,
        selectedPatient,
        setSelectedPatient
    } = usePatient();

    const location = useLocation();
    const navigate = useNavigate();

    const [userResponse, setUserResponse] = useState("");

    const filteredMenu = menuItems.filter((item) => {
        if (item.path === "/health-tracker" && localStorage.getItem("tipo") !== "paciente") {
            return false;
        }
        return true;
    });

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

    const isPersonalContext =
        selectedPatient?.cpf
        === userResponse.cpf;

    const userType =
        localStorage
            .getItem("tipo")
            ?.toLowerCase();

    const sidebarColor = {
        paciente: "#4F8FCF",
        responsavel: "#2C7A4B",
        saude: "#7A5DC7"
    }[userType] || "#4F8FCF";

    const drawerWidth =
        open ? 280 : 84;

    function handleNavigate(path) {
        if (userResponse !== "paciente" && isPersonalContext) {

            setSelectedPatient(
                patients[0]
            );

            navigate("/dashboard");

            return;
        }
        navigate(path);
    }

    function handleOpenProfile() {

        setSelectedPatient(userResponse);

        navigate("/reports");
    }

    async function handleLogout() {
        try {
            await logout();
            localStorage.removeItem("token");
            navigate("/login");
        } catch (e) {
            console.error("Erro ao fazer logout:", e);
        }
    }

    useEffect(() => {
        async function fetchUsers() {
            try {
                const CPF = localStorage.getItem("CPF");
                const data = await getUserByCpf({ CPF });

                setUserResponse(data);
                localStorage.setItem("nome", data.nome);
                localStorage.setItem("tipo", data.tipo);
                localStorage.setItem("conselho", data.conselho);
                localStorage.setItem("email", data.email);
                localStorage.setItem("dataNascimento", data.dataNascimento);
                localStorage.setItem("privCompartilharDiario", data.privCompartilharDiario);
                localStorage.setItem("privCompartilharHabitos", data.privCompartilharHabitos);
                localStorage.setItem("telefone", data.telefone);
                localStorage.setItem("pesoInicial", data.pesoInicial);
                localStorage.setItem("altura", data.altura);
            } catch (error) {
                console.error("Erro ao buscar usuários:", error);
            }
        }

        fetchUsers();

    }, []);

    return (
        <Drawer
            variant="permanent"
            open={open}
            onClose={() => setOpen(false)}
            sx={{
                width: drawerWidth,
                transition: "0.2s",
                overflowX: "hidden",
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                    transition: "0.2s",
                    overflowX: "hidden",
                    boxSizing: "border-box",
                    borderRight: "1px solid #eee",
                    backgroundColor: sidebarColor,
                    // color: "#fff",
                },
            }}
        >
            {/* Pacientes */}
            <Box
                sx={{
                    px: 2,
                    py: 0,
                    mt: 8
                }}
            >
                {patients.length > 0 && userResponse.tipo !== "paciente" && (
                    <Box mt={2}>

                        {open && <Typography

                            sx={{
                                opacity: open ? 1 : 2,
                                width: open ? "auto" : 0,
                                transition: "0.2s",
                                overflow: "hidden",
                                whiteSpace: "nowrap"
                            }}
                        >
                            Pacientes
                        </Typography>}


                        <Box mt={1}>

                            {patients.map((patient) => (
                                <Button

                                    key={patient.id}
                                    onClick={() =>
                                        setSelectedPatient(patient)
                                    }
                                    fullWidth
                                    sx={{
                                        display: "flex",
                                        justifyContent: open ? "flex-start" : "center",
                                        alignItems: "center",
                                        overflow: "hidden",
                                        borderRadius: "16px",
                                        mb: 1,
                                        textTransform: "none",
                                        minHeight: 52,
                                        px: open ? 2 : 0,
                                        width: open ? "100%" : "52px",
                                        minWidth: open ? "100%" : "52px",
                                        height: "52px",
                                        color:
                                            selectedPatient?.id === patient.id
                                                ? "#1B1B1B"
                                                : "rgba(255,255,255,0.82)",

                                        background:
                                            selectedPatient?.id === patient.id
                                                ? "linear-gradient(90deg, #69f08a, #F3FFE8)"
                                                : "transparent",

                                        border:
                                            selectedPatient?.id === patient.id
                                                ? "1px solid rgba(255,255,255,0.7)"
                                                : "1px solid rgba(255,255,255,0.7)",

                                        boxShadow:
                                            selectedPatient?.id === patient.id
                                                ? "0 4px 10px rgba(0,0,0,0.12)"
                                                : "none",

                                        fontWeight:
                                            selectedPatient?.id === patient.id
                                                ? 700
                                                : 500,

                                        transition: "all .2s ease",

                                        "&:hover": {
                                            background:
                                                selectedPatient?.id === patient.id
                                                    ? "linear-gradient(90deg, #69f08a, #F3FFE8)"
                                                    : "rgba(255,255,255,0.10)",

                                            transform: "translateX(2px)",
                                        },
                                    }}
                                >
                                    <>
                                        {!open && (
                                            <Typography
                                                sx={{
                                                    fontWeight: 700,
                                                    fontSize: "1rem",
                                                    color:
                                                        selectedPatient?.id === patient.id
                                                            ? "#1B1B1B"
                                                            : "#FFFFFF",
                                                }}
                                            >
                                                {getIniciais(patient.nome)}
                                            </Typography>
                                        )}

                                        <Typography
                                            sx={{
                                                opacity: open ? 1 : 0,
                                                maxWidth: open ? "180px" : 0,
                                                transition: "all .2s ease",
                                                overflow: "hidden",
                                                whiteSpace: "nowrap",
                                                textOverflow: "ellipsis"
                                            }}
                                        >
                                            {patient.nome}
                                        </Typography>
                                    </>
                                </Button>

                            ))}

                        </Box>

                    </Box>
                )}

            </Box>



            {/* FOOTER (user) */}
            <Box sx={{ mt: "auto", p: 2 }}>

                {/* MENU */}
                <List sx={{}}>
                    {filteredMenu.map((item) => {
                        const isActive = location.pathname === item.path;

                        return (
                            <ListItemButton
                                key={item.label}
                                onClick={() => handleNavigate(item.path)}
                                sx={{
                                    borderRadius: "16px",
                                    mb: 1,
                                    minHeight: 52,
                                    justifyContent: open ? "initial" : "center",
                                    px: open ? 2 : 1.5,
                                    color: isActive
                                        ? "#1B1B1B"
                                        : "rgba(255,255,255,0.82)",
                                    background: isActive
                                        ? "linear-gradient(90deg, #69f08a, #F3FFE8)"
                                        : "transparent",
                                    border: isActive
                                        ? "1px solid rgba(255,255,255,0.7)"
                                        : "1px solid rgba(255,255,255,0.7)",
                                    boxShadow: isActive
                                        ? "0 4px 10px rgba(0,0,0,0.12)"
                                        : "none",
                                    transition: "all .2s ease",
                                    "&:hover": {
                                        background: isActive
                                            ? "linear-gradient(90deg, #69f08a, #F3FFE8)"
                                            : "rgba(255,255,255,0.10)",
                                        transform: "translateX(2px)",
                                    },
                                }}
                            >
                                <ListItemIcon
                                    sx={{
                                        color: "inherit",
                                        minWidth: 0,
                                        mr: open ? 2 : 0,
                                        justifyContent: "center",
                                    }}
                                >
                                    {item.icon}
                                </ListItemIcon>

                                <ListItemText
                                    primary={item.label}
                                    sx={{
                                        opacity: open ? 1 : 0,
                                        transition: "0.2s",
                                        whiteSpace: "nowrap",
                                    }}
                                />
                            </ListItemButton>
                        );
                    })}
                </List>


                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: open ? "space-between" : "center",
                        cursor: "pointer",
                        borderRadius: "12px",
                        p: 1,
                        transition: "0.2s",
                    }}
                >
                    <Avatar
                        onClick={handleOpenProfile}
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            bgcolor: "rgba(255,255,255,0.14)",
                            color: "#FFFFFF",
                            width: open ? 52 : 42,
                            height: open ? 52 : 42,
                            fontWeight: 700,
                            fontSize: open ? "1rem" : "0.9rem",
                            border: "1px solid rgba(255,255,255,0.18)",
                            boxShadow: "0 4px 10px rgba(0,0,0,0.12)",
                            backdropFilter: "blur(6px)",
                            transition: "all .2s ease",
                            "&:hover": {
                                transform: "scale(1.04)",
                                bgcolor: "rgba(255,255,255,0.20)",
                            },
                        }}
                    >
                        {getIniciais(userResponse.nome)}
                    </Avatar>

                    {open && <Box
                        sx={{
                            flex: 1,
                            ml: 1.5,
                            overflow: "hidden",
                        }}
                    >

                        {open && <Typography
                            sx={{
                                opacity: open ? 1 : 0,
                                width: open ? "auto" : 0,
                                transition: "0.2s",
                                overflow: "hidden",
                                whiteSpace: "nowrap"
                            }}
                        >
                            {userResponse.nome || "Usuário"}
                        </Typography>}

                        {open && <Typography
                            sx={{
                                opacity: open ? 1 : 0,
                                width: open ? "auto" : 0,
                                transition: "0.2s",
                                overflow: "hidden",
                                whiteSpace: "nowrap"
                            }}
                        >
                            {userResponse.tipo || "Tipo de usuário"}
                        </Typography>}
                    </Box>}

                    {open && <IconButton
                        onClick={handleLogout}
                        sx={{
                            backgroundColor: "#ffe5e5",
                            "&:hover": {
                                backgroundColor: "#ffd6d6",
                            },
                        }}
                    >
                        <LogoutIcon color="error" />
                    </IconButton>}

                </Box>
            </Box>
        </Drawer >
    );
}