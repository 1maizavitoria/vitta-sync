import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Chip from "@mui/material/Chip";
import IconButton from "@mui/material/IconButton";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import Toolbar from "@mui/material/Toolbar";
import Tooltip from "@mui/material/Tooltip";
import Typography from "@mui/material/Typography";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useTheme } from "@mui/material/styles";

import DarkModeIcon from "@mui/icons-material/DarkMode";
import LightModeIcon from "@mui/icons-material/LightMode";
import MenuIcon from "@mui/icons-material/Menu";
import MenuOpenIcon from "@mui/icons-material/MenuOpen";

import { useLocation, useNavigate } from "react-router-dom";
import { useI18n } from "../../src/i18n";
import { useThemeMode } from "../../src/theme/ThemeModeProvider";
import ButtonUI from "../ui/Button";

export default function Navbar({ open, setOpen, selectedPatient }) {
    const navigate = useNavigate();
    const location = useLocation();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("md"));
    const { language, languages, setLanguage, t } = useI18n();
    const { mode, toggleMode } = useThemeMode();
    const isDarkMode = mode === "dark";
    const vitta = theme.vitta;

    const currentUser = {
        nome: localStorage.getItem("nome"),
        tipo: localStorage.getItem("tipo")
    };

    const userType = currentUser.tipo?.toLowerCase();
    const hasSelectedPatientContext =
        ["responsavel", "saude"].includes(userType) &&
        Boolean(selectedPatient?.nome);

    const displayTypeLabel = {
        paciente: t("userTypes.paciente"),
        responsavel: t("userTypes.responsavel"),
        saude: t("userTypes.saude")
    }[userType] || currentUser.tipo || t("userTypes.user");

    const isPublicPage =
        location.pathname === "/" ||
        location.pathname === "/login" ||
        location.pathname === "/register" ||
        location.pathname === "/entrar";

    return (
        <AppBar
            position="fixed"
            elevation={0}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 2,
                backgroundColor: isDarkMode ? "rgba(7, 26, 18, 0.9)" : "rgba(255, 255, 255, 0.9)",
                color: "text.primary",
                backdropFilter: "blur(18px)",
                borderBottom: "1px solid",
                borderColor: "divider"
            }}
        >
            <Toolbar
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    gap: { xs: 1, sm: 2 },
                    minHeight: { xs: 58, sm: 64 },
                    px: { xs: 1.5, sm: 3 }
                }}
            >
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: { xs: 0.75, sm: 1.25 },
                        minWidth: 0,
                        flexShrink: 0
                    }}
                >
                    {!isPublicPage && isMobile && (
                        <Tooltip title={open ? t("nav.closeSidebar") : t("nav.openSidebar")}>
                            <IconButton
                                onClick={() => setOpen?.((prev) => !prev)}
                                size="small"
                                aria-label={open ? t("nav.closeSidebar") : t("nav.openSidebar")}
                                sx={{
                                    color: "primary.main",
                                    bgcolor: isDarkMode ? "rgba(34, 197, 94, 0.12)" : "rgba(22, 163, 74, 0.08)",
                                    border: "1px solid",
                                    borderColor: vitta.border,
                                    "&:hover": {
                                        bgcolor: isDarkMode ? "rgba(34, 197, 94, 0.18)" : "rgba(22, 163, 74, 0.14)"
                                    }
                                }}
                            >
                                {open ? <MenuOpenIcon /> : <MenuIcon />}
                            </IconButton>
                        </Tooltip>
                    )}

                    <Typography
                        sx={{
                            fontSize: {
                                xs: "1.05rem",
                                sm: "1.45rem",
                                md: "1.75rem"
                            },
                            fontWeight: 800,
                            color: "primary.dark",
                            lineHeight: 1,
                            whiteSpace: "nowrap",
                        }}
                    >
                        {t("brand")}
                    </Typography>
                </Box>

                {!isPublicPage && (
                    <Box
                        sx={{
                            flex: 1,
                            minWidth: 0,
                            display: "flex",
                            justifyContent: "center",
                            px: { xs: 0.5, sm: 2 }
                        }}
                    >
                        <Box
                            sx={{
                                display: "flex",
                                flexDirection: {
                                    xs: hasSelectedPatientContext ? "column" : "row",
                                    md: "row"
                                },
                                alignItems: {
                                    xs: hasSelectedPatientContext ? "flex-start" : "center",
                                    md: "center"
                                },
                                gap: { xs: 1, sm: 1.5 },
                                minWidth: 0,
                                maxWidth: "100%",
                                px: { xs: 1.25, sm: 2 },
                                py: {
                                    xs: hasSelectedPatientContext ? 0.45 : 0.55,
                                    sm: 0.75
                                },
                                borderRadius: 2,
                                bgcolor: isDarkMode ? "rgba(34, 197, 94, 0.1)" : "rgba(22, 163, 74, 0.07)",
                                border: "1px solid",
                                borderColor: vitta.border
                            }}
                        >
                            <Box
                                sx={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 1,
                                    minWidth: 0
                                }}
                            >
                                <Typography
                                    sx={{
                                        fontWeight: 800,
                                        fontSize: { xs: "0.82rem", sm: "0.95rem" },
                                        maxWidth: {
                                            xs: hasSelectedPatientContext ? 150 : 92,
                                            sm: hasSelectedPatientContext ? 150 : 220,
                                            md: hasSelectedPatientContext ? 220 : 420
                                        },
                                        overflow: "hidden",
                                        textOverflow: "ellipsis",
                                        whiteSpace: "nowrap"
                                    }}
                                >
                                    {currentUser.nome || t("userTypes.user")}
                                </Typography>

                                <Chip
                                    label={displayTypeLabel}
                                    size="small"
                                    sx={{
                                        display: { xs: "none", sm: "inline-flex" },
                                        fontWeight: 800,
                                        bgcolor: isDarkMode ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.12)",
                                        border: "1px solid",
                                        borderColor: vitta.border,
                                        color: "primary.dark",
                                        flexShrink: 0
                                    }}
                                />
                            </Box>

                            {hasSelectedPatientContext && (
                                <Box
                                    sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        gap: { xs: 0.5, md: 1 },
                                        minWidth: 0,
                                        maxWidth: "100%",
                                        pl: { xs: 0, md: 1.5 },
                                        borderLeft: {
                                            xs: "none",
                                            md: `1px solid ${vitta.borderStrong}`
                                        }
                                    }}
                                >
                                    <Typography
                                        sx={{
                                            color: "text.secondary",
                                            fontSize: { xs: "0.72rem", md: "0.78rem" },
                                            fontWeight: 800,
                                            whiteSpace: "nowrap"
                                        }}
                                    >
                                        {t("nav.patient")}
                                    </Typography>

                                    <Typography
                                        sx={{
                                            color: "text.primary",
                                            fontSize: { xs: "0.78rem", md: "0.9rem" },
                                            fontWeight: 800,
                                            maxWidth: {
                                                xs: 132,
                                                sm: 180,
                                                md: 180,
                                                lg: 280
                                            },
                                            overflow: "hidden",
                                            textOverflow: "ellipsis",
                                            whiteSpace: "nowrap"
                                        }}
                                    >
                                        {selectedPatient.nome}
                                    </Typography>
                                </Box>
                            )}
                        </Box>
                    </Box>
                )}

                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: { xs: 1, sm: 1.5 },
                        flexShrink: 0
                    }}
                >
                    {isPublicPage && (
                        <Select
                            size="small"
                            value={language}
                            title={t("nav.language")}
                            inputProps={{ "aria-label": t("nav.language") }}
                            onChange={(event) => setLanguage(event.target.value)}
                            sx={{
                                minWidth: 72,
                                borderRadius: 2,
                                bgcolor: "background.paper",
                                "& .MuiSelect-select": {
                                    py: 0.75,
                                    fontWeight: 700
                                }
                            }}
                        >
                            {languages.map((item) => (
                                <MenuItem key={item.code} value={item.code}>
                                    {item.label}
                                </MenuItem>
                            ))}
                        </Select>
                    )}

                    {!isPublicPage && (
                        <Select
                            size="small"
                            value={language}
                            title={t("nav.language")}
                            inputProps={{ "aria-label": t("nav.language") }}
                            onChange={(event) => setLanguage(event.target.value)}
                            sx={{
                                display: { xs: "none", sm: "inline-flex" },
                                minWidth: 68,
                                borderRadius: 2,
                                bgcolor: "background.paper",
                                "& .MuiSelect-select": {
                                    py: 0.75,
                                    fontWeight: 800
                                }
                            }}
                        >
                            {languages.map((item) => (
                                <MenuItem key={item.code} value={item.code}>
                                    {item.label}
                                </MenuItem>
                            ))}
                        </Select>
                    )}

                    <Tooltip title={isDarkMode ? t("theme.light") : t("theme.dark")}>
                        <IconButton
                            onClick={toggleMode}
                            size="small"
                            aria-label={t("theme.toggle")}
                            sx={{
                                color: isDarkMode ? "warning.light" : "primary.main",
                                bgcolor: isDarkMode
                                    ? "rgba(250, 204, 21, 0.12)"
                                    : "rgba(22, 163, 74, 0.08)",
                                border: "1px solid",
                                borderColor: isDarkMode
                                    ? "rgba(250, 204, 21, 0.22)"
                                    : "rgba(22, 163, 74, 0.14)",
                                "&:hover": {
                                    bgcolor: isDarkMode
                                        ? "rgba(250, 204, 21, 0.18)"
                                        : "rgba(22, 163, 74, 0.14)"
                                }
                            }}
                        >
                            {isDarkMode ? <LightModeIcon fontSize="small" /> : <DarkModeIcon fontSize="small" />}
                        </IconButton>
                    </Tooltip>

                    {location.pathname === "/" && (
                        <ButtonUI
                            variant="outlined"
                            sx={{
                                display: { xs: "none", sm: "inline-flex" },
                                whiteSpace: "nowrap"
                            }}
                            onClick={() => navigate("/login")}
                        >
                            {t("nav.loginRegister")}
                        </ButtonUI>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
}
