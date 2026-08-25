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
import FlagOutlinedIcon from "@mui/icons-material/FlagOutlined";
import GroupsOutlinedIcon from "@mui/icons-material/GroupsOutlined";

import { usePatient } from "../../context/PatientContext";
import { logout } from "../../services/authService";
import { getUnreadEventsCount } from "../../services/eventService";
import { getUserByCpf } from "../../services/userService";
import { useI18n } from "../../src/i18n";

const menuItems = [
    { labelKey: "nav.group", icon: <GroupsOutlinedIcon />, path: "/links" },
    { labelKey: "nav.dashboard", icon: <DashboardIcon />, path: "/dashboard" },
    { labelKey: "nav.records", icon: <MonitorHeartOutlinedIcon />, path: "/health-tracker" },
    { labelKey: "nav.information", icon: <ShowChartIcon />, path: "/reports" },
    { labelKey: "nav.documents", icon: <FolderOutlinedIcon />, path: "/documents" },
    { labelKey: "nav.goals", icon: <FlagOutlinedIcon />, path: "/goals" },
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
            ["/reports", "/documents", "/goals", "/activity"].includes(item.path) &&
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

    const getTypeVisual = (type) => ({
        paciente: {
            color: "#34d399",
            background: "rgba(16, 185, 129, 0.2)",
            border: "rgba(52, 211, 153, 0.36)",
            activeText: "#d1fae5",
            activeBackground: isDark
                ? "linear-gradient(135deg, rgba(20, 184, 166, 0.22) 0%, rgba(6, 95, 70, 0.42) 100%)"
                : "linear-gradient(135deg, rgba(20, 184, 166, 0.22) 0%, rgba(6, 95, 70, 0.38) 100%)",
            activeBorder: "rgba(45, 212, 191, 0.28)",
            passiveText: "rgba(209,250,229,0.76)",
            passiveBackground: "rgba(6, 24, 20, 0.42)",
            passiveBorder: "rgba(45, 212, 191, 0.12)",
            hoverBackground: "rgba(20, 184, 166, 0.12)",
            panelBackground: "rgba(6, 24, 20, 0.5)",
            paperBorder: "rgba(45, 212, 191, 0.14)",
            sidebarBackground: isDark
                ? "radial-gradient(circle at 18% 7%, rgba(20, 184, 166, 0.18) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(14, 165, 233, 0.1) 0%, transparent 34%), linear-gradient(180deg, #06130f 0%, #071812 42%, #07111f 100%)"
                : "radial-gradient(circle at 18% 7%, rgba(20, 184, 166, 0.16) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(14, 165, 233, 0.08) 0%, transparent 34%), linear-gradient(180deg, #071912 0%, #082018 46%, #071426 100%)"
        },
        responsavel: {
            color: "#38bdf8",
            background: "rgba(14, 165, 233, 0.2)",
            border: "rgba(56, 189, 248, 0.36)",
            activeText: "#e0f2fe",
            activeBackground: isDark
                ? "linear-gradient(135deg, rgba(14, 165, 233, 0.24) 0%, rgba(30, 64, 175, 0.42) 100%)"
                : "linear-gradient(135deg, rgba(14, 165, 233, 0.22) 0%, rgba(30, 64, 175, 0.36) 100%)",
            activeBorder: "rgba(56, 189, 248, 0.3)",
            passiveText: "rgba(224,242,254,0.76)",
            passiveBackground: "rgba(8, 20, 36, 0.46)",
            passiveBorder: "rgba(56, 189, 248, 0.12)",
            hoverBackground: "rgba(14, 165, 233, 0.13)",
            panelBackground: "rgba(8, 20, 36, 0.52)",
            paperBorder: "rgba(56, 189, 248, 0.15)",
            sidebarBackground: isDark
                ? "radial-gradient(circle at 18% 7%, rgba(14, 165, 233, 0.2) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(59, 130, 246, 0.13) 0%, transparent 34%), linear-gradient(180deg, #06111f 0%, #071827 45%, #081020 100%)"
                : "radial-gradient(circle at 18% 7%, rgba(14, 165, 233, 0.18) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(59, 130, 246, 0.11) 0%, transparent 34%), linear-gradient(180deg, #071522 0%, #081b2d 45%, #081122 100%)"
        },
        saude: {
            color: "#c084fc",
            background: "rgba(168, 85, 247, 0.2)",
            border: "rgba(192, 132, 252, 0.36)",
            activeText: "#f3e8ff",
            activeBackground: isDark
                ? "linear-gradient(135deg, rgba(168, 85, 247, 0.24) 0%, rgba(88, 28, 135, 0.44) 100%)"
                : "linear-gradient(135deg, rgba(168, 85, 247, 0.22) 0%, rgba(88, 28, 135, 0.38) 100%)",
            activeBorder: "rgba(192, 132, 252, 0.3)",
            passiveText: "rgba(243,232,255,0.76)",
            passiveBackground: "rgba(24, 12, 36, 0.48)",
            passiveBorder: "rgba(192, 132, 252, 0.12)",
            hoverBackground: "rgba(168, 85, 247, 0.13)",
            panelBackground: "rgba(24, 12, 36, 0.54)",
            paperBorder: "rgba(192, 132, 252, 0.15)",
            sidebarBackground: isDark
                ? "radial-gradient(circle at 18% 7%, rgba(168, 85, 247, 0.2) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(124, 58, 237, 0.14) 0%, transparent 34%), linear-gradient(180deg, #14091f 0%, #190d2a 45%, #0f1024 100%)"
                : "radial-gradient(circle at 18% 7%, rgba(168, 85, 247, 0.18) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(124, 58, 237, 0.12) 0%, transparent 34%), linear-gradient(180deg, #150b22 0%, #1a0f2c 45%, #101126 100%)"
        }
    }[type?.toLowerCase()] || {
        color: "#a7f3d0",
        background: "rgba(167, 243, 208, 0.14)",
        border: "rgba(167, 243, 208, 0.24)",
        activeText: "#d1fae5",
        activeBackground: "linear-gradient(135deg, rgba(20, 184, 166, 0.22) 0%, rgba(6, 95, 70, 0.42) 100%)",
        activeBorder: "rgba(45, 212, 191, 0.28)",
        passiveText: "rgba(209,250,229,0.76)",
        passiveBackground: "rgba(6, 24, 20, 0.42)",
        passiveBorder: "rgba(45, 212, 191, 0.12)",
        hoverBackground: "rgba(20, 184, 166, 0.12)",
        panelBackground: "rgba(6, 24, 20, 0.5)",
        paperBorder: "rgba(45, 212, 191, 0.14)",
        sidebarBackground: "radial-gradient(circle at 18% 7%, rgba(20, 184, 166, 0.18) 0%, transparent 28%), radial-gradient(circle at 88% 18%, rgba(14, 165, 233, 0.1) 0%, transparent 34%), linear-gradient(180deg, #06130f 0%, #071812 42%, #07111f 100%)"
    });

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

    const sidebarVisual = getTypeVisual(userResponse.tipo || userType);

    const activeItemSx = {
        color: sidebarVisual.activeText,
        background: sidebarVisual.activeBackground,
        borderColor: sidebarVisual.activeBorder,
        boxShadow: "0 14px 34px rgba(3, 31, 25, 0.32)"
    };

    const sidebarBackground = sidebarVisual.sidebarBackground;

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
                    borderColor: sidebarVisual.paperBorder,
                    background: sidebarBackground,
                    backgroundBlendMode: "screen, normal, normal",
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
                            const patientVisual = getTypeVisual("paciente");

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
                                            color: isSelected ? activeItemSx.color : sidebarVisual.passiveText,
                                            background: isSelected ? activeItemSx.background : sidebarVisual.passiveBackground,
                                            border: `1px solid ${sidebarVisual.passiveBorder}`,
                                            boxShadow: isSelected ? activeItemSx.boxShadow : "none",
                                            fontWeight: 700,
                                            transition: "background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease",
                                            "&:hover": {
                                                background: isSelected
                                                    ? activeItemSx.background
                                                    : sidebarVisual.hoverBackground,
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
                                                bgcolor: isSelected ? patientVisual.background : "rgba(6, 24, 20, 0.58)",
                                                color: patientVisual.color,
                                                fontSize: 13,
                                                fontWeight: 800,
                                                border: `1px solid ${patientVisual.border}`
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
                                        color: isActive ? activeItemSx.color : sidebarVisual.passiveText,
                                        background: isActive ? activeItemSx.background : sidebarVisual.passiveBackground,
                                        border: "1px solid",
                                        borderColor: isActive ? activeItemSx.borderColor : sidebarVisual.passiveBorder,
                                        boxShadow: isActive ? activeItemSx.boxShadow : "none",
                                        transition: "background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease",
                                        "&:hover": {
                                            background: isActive ? activeItemSx.background : sidebarVisual.hoverBackground,
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
                        bgcolor: isExpanded ? sidebarVisual.panelBackground : "transparent",
                        border: isExpanded ? `1px solid ${sidebarVisual.passiveBorder}` : "none",
                    }}
                >
                    <Tooltip title={t("nav.openProfile")}>
                        <Avatar
                            onClick={handleOpenProfile}
                            sx={{
                                bgcolor: getTypeVisual(userResponse.tipo).background,
                                color: getTypeVisual(userResponse.tipo).color,
                                width: isExpanded ? 44 : 42,
                                height: isExpanded ? 44 : 42,
                                fontWeight: 800,
                                fontSize: isExpanded ? "0.95rem" : "0.9rem",
                                border: `1px solid ${getTypeVisual(userResponse.tipo).border}`,
                                boxShadow: "0 10px 20px rgba(0,0,0,0.16)",
                                cursor: "pointer",
                                transition: "background-color .18s ease, border-color .18s ease, box-shadow .18s ease, transform .18s ease",
                                "&:hover": {
                                    transform: "scale(1.04)",
                                    bgcolor: getTypeVisual(userResponse.tipo).background,
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
                                    color: getTypeVisual(userResponse.tipo).color,
                                    fontSize: 12,
                                    fontWeight: 800,
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
