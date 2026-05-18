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
import { getAvailablePatients } from "../../services/linkService";

const drawerWidth = 240;

const menuItems = [
    { label: "Grupo", icon: <DashboardIcon />, path: "/dashboard" },
    // { label: "Registros", icon: <DescriptionIcon />, path: "/health-tracker" },
    // { label: "Vínculos", icon: <LinkIcon />, path: "/links" },
    // { label: "Relatórios", icon: <ShowChartIcon />, path: "/reports" },
];

export default function Sidebar({ open, setOpen }) {


    const {
        // patients,
        selectedPatient,
        setSelectedPatient
    } = usePatient();

    const [patients, setPatients] = useState([]);

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

        async function fetchPatients() {

            try {

                const data =
                    await getAvailablePatients();

                setPatients(data);

                // seleciona automaticamente
                // o primeiro paciente
                if (
                    data.length > 0 &&
                    !selectedPatient
                ) {

                    setSelectedPatient(
                        data[0]
                    );
                }

            } catch (error) {

                console.error(error);
            }
        }

        fetchUsers();
        fetchPatients();
    }, []);

    return (
        <Drawer
            variant="temporary"
            open={open}
            onClose={() => setOpen(false)}
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                    boxSizing: "border-box",
                    borderRight: "1px solid #eee",
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
                        <Typography
                            variant="caption"
                            sx={{
                                color: "#777",
                                fontWeight: 700,
                                textTransform: "uppercase",
                                letterSpacing: 1
                            }}
                        >
                            Pacientes
                        </Typography>

                        <Box mt={1}>
                            {patients.map((patient) => (
                                <Button
                                    key={patient.id}
                                    onClick={() =>
                                        setSelectedPatient(patient)
                                    }
                                    fullWidth
                                    sx={{
                                        justifyContent: "flex-start",
                                        borderRadius: "12px",
                                        mb: 1,
                                        textTransform: "none",
                                        color: "#333",
                                        px: 2,
                                        py: 1.2,
                                        backgroundColor: selectedPatient?.id === patient.id
                                            ? "#EEF7F1"
                                            : "transparent",
                                        border: selectedPatient?.id === patient.id
                                            ? "1px solid #A8E6CF"
                                            : "1px solid transparent",
                                        fontWeight: selectedPatient?.id === patient.id
                                            ? 600
                                            : 400,
                                        "&:hover": {
                                            backgroundColor: "#F5F5F5",
                                        },
                                    }}
                                >
                                    {patient.nome}
                                </Button>

                            ))}

                        </Box>

                    </Box>
                )}

            </Box>



            {/* FOOTER (user) */}
            <Box sx={{ mt: "auto", p: 2 }}>
                {/* MENU */}
                <List sx={{ p: 1, mt: 3 }}>
                    {filteredMenu.map((item) => {
                        const isActive = location.pathname === item.path;

                        return (
                            <ListItemButton
                                key={item.label}
                                onClick={() => navigate(item.path)}
                                sx={{
                                    borderRadius: 3,
                                    mb: 0.5,

                                    ...(isActive && {
                                        background: "linear-gradient(90deg, #a8e6cf, #dcedc1)",
                                        boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
                                    }),
                                }}
                            >
                                <ListItemIcon sx={{ color: "inherit" }}>
                                    {item.icon}
                                </ListItemIcon>

                                <ListItemText primary={item.label} />
                            </ListItemButton>
                        );
                    })}
                </List>
                <Divider sx={{ mb: 2 }} />

                <Box
                    onClick={handleOpenProfile}

                    sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between",
                        width: "100%",
                        cursor: "pointer",
                        borderRadius: "12px",
                        p: 1,
                        transition: "0.2s",
                        "&:hover": {
                            backgroundColor: "#f5f5f5",
                        },
                    }}
                >
                    <Avatar sx={{ bgcolor: "#a8e6cf", color: "#000" }}>
                        {getIniciais(userResponse.nome)}
                    </Avatar>

                    <Box>
                        <Typography variant="body2" fontWeight="bold">
                            {userResponse.nome || "Usuário"}
                        </Typography>

                        <Typography
                            variant="caption"
                            color="text.secondary"
                        >
                            {userResponse.tipo || "Tipo de usuário"}
                        </Typography>
                    </Box>

                    <IconButton
                        onClick={handleLogout}
                        sx={{
                            backgroundColor: "#ffe5e5",
                            "&:hover": {
                                backgroundColor: "#ffd6d6",
                            },
                        }}
                    >
                        <LogoutIcon color="error" />
                    </IconButton>

                </Box>
            </Box>
        </Drawer>
    );
}