import { useCallback, useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import Avatar from "@mui/material/Avatar";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Drawer from "@mui/material/Drawer";
import IconButton from "@mui/material/IconButton";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Tooltip from "@mui/material/Tooltip";
import Typography from "@mui/material/Typography";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useTheme } from "@mui/material/styles";

import ActivityIcon from "@mui/icons-material/NotificationsActiveOutlined";
import DashboardIcon from "@mui/icons-material/Dashboard";
import FolderOutlinedIcon from "@mui/icons-material/FolderOutlined";
import LogoutIcon from "@mui/icons-material/Logout";
import MonitorHeartOutlinedIcon from "@mui/icons-material/MonitorHeartOutlined";
import ShowChartIcon from "@mui/icons-material/ShowChart";

import { usePatient } from "../../context/PatientContext";
import { logout } from "../../services/authService";
import { getUnreadEventsCount } from "../../services/eventService";
import { getUserByCpf } from "../../services/userService";
import { useI18n } from "../../src/i18n";

const menuItems = [
    { labelKey: "nav.group", icon: <DashboardIcon />, path: "/dashboard" },
    { labelKey: "nav.records", icon: <MonitorHeartOutlinedIcon />, path: "/health-tracker" },
    { labelKey: "nav.information", icon: <ShowChartIcon />, path: "/reports" },
    { labelKey: "nav.documents", icon: <FolderOutlinedIcon />, path: "/documents" },
    { labelKey: "nav.activity", icon: <ActivityIcon />, path: "/activity" },
];

const drawerSizes = {
    open: 280,
    closed: 84
};

export default function Sidebar({ open, setOpen }) {
    const sidebarRef = useRef(null);
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const vitta = theme.vitta;
    const isMobile = useMediaQuery(theme.breakpoints.down("md"));
    const { t } = useI18n();
    const [notificationCounts, setNotificationCounts] = useState({});
    const [userResponse, setUserResponse] = useState("");

    const {
        patients,
        selectedPatient,
        setSelectedPatient
    } = usePatient();

    const location = useLocation();
    const navigate = useNavigate();
    const userType = localStorage.getItem("tipo")?.toLowerCase();

    const filteredMenu = menuItems.filter((item) => {
        const hasPatientContext = selectedPatient?.cpf || patients.length > 0;

        if (
            item.path === "/health-tracker" &&
            userType !== "paciente" &&
            !hasPatientContext
        ) {
            return false;
        }

        if (
            ["/reports", "/documents", "/activity"].includes(item.path) &&
            userType !== "paciente" &&
            !selectedPatient?.cpf
        ) {
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

    const formatUserType = (type) => ({
        paciente: t("userTypes.paciente"),
        responsavel: t("userTypes.responsavel"),
        saude: t("userTypes.saude")
    }[type?.toLowerCase()] || type || t("nav.userType"));

    const drawerWidth = isMobile
        ? drawerSizes.open
        : open
            ? drawerSizes.open
            : drawerSizes.closed;

    const isExpanded = isMobile || open;

    function closeMobileSidebar() {
        if (isMobile) {
            setOpen(false);
        }
    }

    function handleNavigate(path) {
        if (path === "/reports") {
            navigate("/reports?view=patient");
            closeMobileSidebar();
            return;
        }

        navigate(path);
        closeMobileSidebar();
    }

    function handleOpenProfile() {
        navigate("/reports?view=profile");
        closeMobileSidebar();
    }

    async function handleLogout() {
        try {
            await logout();
            localStorage.removeItem("token");
            navigate("/login");
            closeMobileSidebar();
        } catch (e) {
            console.error("Erro ao fazer logout:", e);
        }
    }

    const loadNotificationCounts = useCallback(async () => {
        try {
            const counts = {};

            for (const patient of patients) {
                const total = await getUnreadEventsCount(patient.id);
                counts[patient.id] = total;
            }

            setNotificationCounts(counts);
        } catch (error) {
            console.error(error);
        }
    }, [patients]);

    useEffect(() => {
        if (patients.length === 0) {
            return;
        }

        const timer = setTimeout(loadNotificationCounts, 0);

        return () => clearTimeout(timer);
    }, [patients.length, loadNotificationCounts]);

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

    useEffect(() => {
        function handleClickOutside(event) {
            if (sidebarRef.current && !sidebarRef.current.contains(event.target)) {
                setOpen(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [setOpen]);

    useEffect(() => {
        function handleNotificationsUpdated() {
            loadNotificationCounts();
        }

        window.addEventListener("notificationsUpdated", handleNotificationsUpdated);

        return () => {
            window.removeEventListener("notificationsUpdated", handleNotificationsUpdated);
        };
    }, [patients.length, loadNotificationCounts]);

    const collapsedTextSx = {
        opacity: isExpanded ? 1 : 0,
        maxWidth: isExpanded ? 180 : 0,
        transition: "opacity .18s ease, max-width .24s ease",
        overflow: "hidden",
        whiteSpace: "nowrap"
    };

    const activeItemSx = {
        color: isDark ? "#ecfdf3" : "#102014",
        background: isDark
            ? "linear-gradient(135deg, rgba(34, 197, 94, 0.22) 0%, rgba(14, 165, 233, 0.16) 100%)"
            : "linear-gradient(135deg, #dcfce7 0%, #f0fdf4 100%)",
        borderColor: isDark ? "rgba(220, 252, 231, 0.22)" : "rgba(220, 252, 231, 0.88)",
        boxShadow: isDark ? "0 12px 24px rgba(0, 0, 0, 0.24)" : "0 12px 24px rgba(20, 83, 45, 0.18)"
    };

    return (
        <Drawer
            ref={sidebarRef}
            variant={isMobile ? "temporary" : "permanent"}
            open={isMobile ? open : true}
            onClose={() => setOpen(false)}
            onClick={(event) => {
                if (isMobile) {
                    return;
                }

                if (
                    event.target.closest("button") ||
                    event.target.closest("li") ||
                    event.target.closest(".MuiAvatar-root") ||
                    event.target.closest(".MuiListItemButton-root") ||
                    event.target.closest("svg")
                ) {
                    return;
                }
                setOpen(prev => !prev);
            }}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                position: "fixed",
                width: {
                    xs: 0,
                    md: drawerWidth
                },
                transition: "width .28s cubic-bezier(0.4, 0, 0.2, 1)",
                overflowX: "hidden",
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    position: "fixed",
                    left: 0,
                    top: 0,
                    width: drawerWidth,
                    transition: "width .28s cubic-bezier(0.4, 0, 0.2, 1)",
                    overflowX: "hidden",
                    boxSizing: "border-box",
                    borderRight: "1px solid",
                    borderColor: isDark ? vitta.border : "rgba(220, 252, 231, 0.18)",
                    background: isDark
                        ? "linear-gradient(180deg, #071a12 0%, #0f2418 54%, #081f23 100%)"
                        : "linear-gradient(180deg, #14532d 0%, #166534 52%, #0f3f2a 100%)",
                    color: "#ffffff",
                },
            }}
        >
            <Box
                sx={{
                    px: 2,
                    pt: 9,
                    pb: 2,
                    minHeight: 0,
                    flex: 1,
                    overflowY: "auto"
                }}
            >
                {patients.length > 0 && userResponse.tipo !== "paciente" && (
                    <Box>
                        <Typography
                            sx={{
                                ...collapsedTextSx,
                                mb: 1,
                                px: 1,
                                color: "rgba(255,255,255,0.72)",
                                fontSize: 12,
                                fontWeight: 800,
                                letterSpacing: "0.08em",
                                textTransform: "uppercase"
                            }}
                        >
                            {t("nav.patients")}
                        </Typography>

                        {patients.map((patient) => {
                            const isSelected = selectedPatient?.id === patient.id;

                            return (
                                <Tooltip key={patient.id} title={!isExpanded ? patient.nome : ""} placement="right">
                                    <Button
                                        onClick={() => {
                                            setSelectedPatient(patient);
                                            closeMobileSidebar();
                                        }}
                                        fullWidth
                                        sx={{
                                            display: "flex",
                                            justifyContent: isExpanded ? "flex-start" : "center",
                                            alignItems: "center",
                                            overflow: "hidden",
                                            borderRadius: 2,
                                            mb: 1,
                                            textTransform: "none",
                                            minHeight: 52,
                                            px: isExpanded ? 1.5 : 0,
                                            py: 0,
                                            width: isExpanded ? "100%" : 48,
                                            minWidth: isExpanded ? "100%" : 48,
                                            maxWidth: isExpanded ? "100%" : 48,
                                            height: 52,
                                            color: isSelected ? activeItemSx.color : "rgba(255,255,255,0.82)",
                                            background: isSelected ? activeItemSx.background : "rgba(255,255,255,0.06)",
                                            border: "1px solid rgba(255,255,255,0.12)",
                                            boxShadow: isSelected ? activeItemSx.boxShadow : "none",
                                            fontWeight: 700,
                                            transition: "background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease",
                                            "&:hover": {
                                                background: isSelected
                                                    ? activeItemSx.background
                                                    : "rgba(255,255,255,0.12)",
                                                transform: "translateY(-1px)",
                                            },
                                        }}
                                    >
                                        <Avatar
                                            sx={{
                                                width: isExpanded ? 32 : 34,
                                                height: isExpanded ? 32 : 34,
                                                mr: isExpanded ? 1.25 : 0,
                                                flexShrink: 0,
                                                bgcolor: isSelected ? "primary.main" : "rgba(255,255,255,0.14)",
                                                color: "#ffffff",
                                                fontSize: 13,
                                                fontWeight: 800
                                            }}
                                        >
                                            {getIniciais(patient.nome)}
                                        </Avatar>

                                        <Typography
                                            sx={{
                                                display: "block",
                                                opacity: isExpanded ? 1 : 0,
                                                maxWidth: isExpanded ? 180 : 0,
                                                transition: "opacity .18s ease, max-width .24s ease",
                                                overflow: "hidden",
                                                lineHeight: 1.2,
                                                fontSize: patient.nome.length > 25 ? "0.85rem" : "0.95rem",
                                                fontWeight: 700,
                                                textAlign: "left",
                                                whiteSpace: "nowrap",
                                                textOverflow: "ellipsis",
                                                wordBreak: "normal"
                                            }}
                                        >
                                            {patient.nome}
                                        </Typography>

                                        {notificationCounts[patient.id] > 0 && (
                                            <Box
                                                onClick={(event) => {
                                                    event.stopPropagation();
                                                    setSelectedPatient(patient);
                                                    closeMobileSidebar();
                                                    setNotificationCounts((prev) => ({
                                                        ...prev,
                                                        [patient.id]: 0
                                                    }));
                                                    navigate("/activity");
                                                }}
                                                sx={{
                                                    minWidth: isExpanded ? 22 : 20,
                                                    height: isExpanded ? 22 : 20,
                                                    borderRadius: "999px",
                                                    backgroundColor: "#dc2626",
                                                    color: "#fff",
                                                    fontSize: "0.75rem",
                                                    fontWeight: 800,
                                                    display: "flex",
                                                    alignItems: "center",
                                                    justifyContent: "center",
                                                    px: isExpanded ? 0.8 : 0,
                                                    ml: isExpanded ? "auto" : 0,
                                                    position: isExpanded ? "static" : "absolute",
                                                    top: isExpanded ? "auto" : 4,
                                                    right: isExpanded ? "auto" : 4,
                                                    boxShadow: "0 8px 18px rgba(220,38,38,0.28)"
                                                }}
                                            >
                                                {notificationCounts[patient.id]}
                                            </Box>
                                        )}
                                    </Button>
                                </Tooltip>
                            );
                        })}
                    </Box>
                )}
            </Box>

            <Box sx={{ p: 2 }}>
                <List sx={{ p: 0, mb: 2 }}>
                    {filteredMenu.map((item) => {
                        const isActive = location.pathname === item.path;
                        const label = t(item.labelKey);

                        return (
                            <Tooltip key={item.path} title={!isExpanded ? label : ""} placement="right">
                                <ListItemButton
                                    onClick={() => handleNavigate(item.path)}
                                    sx={{
                                        borderRadius: 2,
                                        mb: 1,
                                        minHeight: 50,
                                        justifyContent: isExpanded ? "initial" : "center",
                                        px: isExpanded ? 1.5 : 1.25,
                                        color: isActive ? activeItemSx.color : "rgba(255,255,255,0.82)",
                                        background: isActive ? activeItemSx.background : "rgba(255,255,255,0.06)",
                                        border: "1px solid",
                                        borderColor: isActive ? activeItemSx.borderColor : "rgba(255,255,255,0.12)",
                                        boxShadow: isActive ? activeItemSx.boxShadow : "none",
                                        transition: "background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease",
                                        "&:hover": {
                                            background: isActive ? activeItemSx.background : "rgba(255,255,255,0.12)",
                                            transform: "translateY(-1px)",
                                        },
                                    }}
                                >
                                    <ListItemIcon
                                        sx={{
                                            color: "inherit",
                                            minWidth: 0,
                                            mr: isExpanded ? 1.5 : 0,
                                            justifyContent: "center",
                                        }}
                                    >
                                        {item.icon}
                                    </ListItemIcon>

                                    <ListItemText
                                        primary={label}
                                        primaryTypographyProps={{
                                            fontWeight: 800,
                                            fontSize: 14
                                        }}
                                        sx={collapsedTextSx}
                                    />
                                </ListItemButton>
                            </Tooltip>
                        );
                    })}
                </List>

                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: isExpanded ? "space-between" : "center",
                        borderRadius: 2,
                        p: isExpanded ? 1 : 0,
                        bgcolor: isExpanded ? "rgba(255,255,255,0.08)" : "transparent",
                        border: isExpanded ? "1px solid rgba(255,255,255,0.12)" : "none",
                    }}
                >
                    <Tooltip title={t("nav.openProfile")}>
                        <Avatar
                            onClick={handleOpenProfile}
                            sx={{
                                bgcolor: "rgba(255,255,255,0.16)",
                                color: "#FFFFFF",
                                width: isExpanded ? 44 : 42,
                                height: isExpanded ? 44 : 42,
                                fontWeight: 800,
                                fontSize: isExpanded ? "0.95rem" : "0.9rem",
                                border: "1px solid rgba(255,255,255,0.18)",
                                boxShadow: "0 10px 20px rgba(0,0,0,0.16)",
                                cursor: "pointer",
                                transition: "background-color .18s ease, border-color .18s ease, box-shadow .18s ease, transform .18s ease",
                                "&:hover": {
                                    transform: "scale(1.04)",
                                    bgcolor: "rgba(255,255,255,0.22)",
                                },
                            }}
                        >
                            {getIniciais(userResponse.nome)}
                        </Avatar>
                    </Tooltip>

                    {isExpanded && (
                        <Box sx={{ flex: 1, ml: 1.5, overflow: "hidden" }}>
                            <Typography
                                sx={{
                                    fontWeight: 800,
                                    overflow: "hidden",
                                    whiteSpace: "nowrap",
                                    textOverflow: "ellipsis"
                                }}
                            >
                                {userResponse.nome || t("userTypes.user")}
                            </Typography>

                            <Typography
                                sx={{
                                    color: "rgba(255,255,255,0.68)",
                                    fontSize: 12,
                                    overflow: "hidden",
                                    whiteSpace: "nowrap",
                                    textOverflow: "ellipsis"
                                }}
                            >
                                {formatUserType(userResponse.tipo)}
                            </Typography>
                        </Box>
                    )}

                    {isExpanded && (
                        <Tooltip title={t("nav.logout")}>
                            <IconButton
                                onClick={handleLogout}
                                sx={{
                                    backgroundColor: "rgba(220,38,38,0.12)",
                                    color: "#fecaca",
                                    "&:hover": {
                                        backgroundColor: "rgba(220,38,38,0.2)",
                                    },
                                }}
                            >
                                <LogoutIcon />
                            </IconButton>
                        </Tooltip>
                    )}
                </Box>
            </Box>
        </Drawer>
    );
}
