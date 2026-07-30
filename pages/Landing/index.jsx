import {
    Box,
    Button,
    Chip,
    Container,
    Grid,
    Paper,
    Stack,
    Typography
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { useNavigate } from "react-router-dom";

import AccessTimeOutlinedIcon from "@mui/icons-material/AccessTimeOutlined";
import AirOutlinedIcon from "@mui/icons-material/AirOutlined";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import DescriptionOutlinedIcon from "@mui/icons-material/DescriptionOutlined";
import DeviceThermostatOutlinedIcon from "@mui/icons-material/DeviceThermostatOutlined";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import GroupOutlinedIcon from "@mui/icons-material/GroupOutlined";
import MonitorHeartOutlinedIcon from "@mui/icons-material/MonitorHeartOutlined";
import OpacityOutlinedIcon from "@mui/icons-material/OpacityOutlined";
import PlayCircleOutlineIcon from "@mui/icons-material/PlayCircleOutline";
import ShieldOutlinedIcon from "@mui/icons-material/ShieldOutlined";

import ButtonUI from "../../components/ui/Button";
import { useI18n } from "../../src/i18n";

export default function Landing() {
    const navigate = useNavigate();
    const theme = useTheme();
    const { t } = useI18n();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";

    const vitalIcons = [
        <MonitorHeartOutlinedIcon />,
        <FavoriteBorderIcon />,
        <OpacityOutlinedIcon />,
        <DeviceThermostatOutlinedIcon />,
        <AirOutlinedIcon />,
        <DescriptionOutlinedIcon />
    ];

    const benefitIcons = [
        <ShieldOutlinedIcon />,
        <GroupOutlinedIcon />,
        <AccessTimeOutlinedIcon />
    ];

    const cards = t("landing.cards");
    const benefits = t("landing.benefits");

    return (
        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "background.default",
                background: vitta.pageBackground,
                color: "text.primary"
            }}
        >
            <Container maxWidth="xl">
                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns: { xs: "1fr", lg: "1.05fr 0.95fr" },
                        gap: { xs: 6, lg: 8 },
                        alignItems: "center",
                        minHeight: { xs: "auto", lg: "calc(100vh - 120px)" },
                        py: { xs: 4, md: 7 }
                    }}
                >
                    <Box>
                        <Chip
                            icon={<MonitorHeartOutlinedIcon />}
                            label={t("landing.badge")}
                            sx={{
                                mb: 3,
                                px: 1,
                                bgcolor: isDark ? "rgba(34, 197, 94, 0.16)" : "rgba(22, 163, 74, 0.12)",
                                color: "primary.dark",
                                fontWeight: 700,
                                border: "1px solid",
                                borderColor: vitta.borderStrong
                            }}
                        />

                        <Typography
                            component="h1"
                            sx={{
                                maxWidth: 760,
                                fontSize: { xs: 42, sm: 56, md: 72 },
                                lineHeight: 1.02,
                                fontWeight: 800,
                                color: "text.primary",
                                mb: 3
                            }}
                        >
                            {t("landing.titleStart")}{" "}
                            <Box component="span" sx={{ color: "primary.main" }}>
                                {t("landing.titleHighlight")}
                            </Box>{" "}
                            {t("landing.titleEnd")}
                        </Typography>

                        <Typography
                            sx={{
                                maxWidth: 660,
                                color: "text.secondary",
                                fontSize: { xs: 17, md: 20 },
                                lineHeight: 1.7,
                                mb: 4
                            }}
                        >
                            {t("landing.description")}
                        </Typography>

                        <Stack
                            direction={{ xs: "column", sm: "row" }}
                            spacing={2}
                            sx={{ mb: 5 }}
                        >
                            <ButtonUI
                                onClick={() => navigate("/register")}
                                sx={{ minHeight: 48 }}
                            >
                                {t("landing.start")} <ArrowForwardIcon sx={{ ml: 1, fontSize: 18 }} />
                            </ButtonUI>

                            <Button
                                variant="outlined"
                                startIcon={<PlayCircleOutlineIcon />}
                                sx={{
                                    minHeight: 48,
                                    borderRadius: 2,
                                    borderColor: "rgba(22, 163, 74, 0.24)",
                                    color: "primary.dark",
                                    fontWeight: 700,
                                    px: 3,
                                    bgcolor: isDark ? "rgba(16, 38, 23, 0.72)" : "rgba(255,255,255,0.62)",
                                    "&:hover": {
                                        bgcolor: isDark ? "rgba(22, 62, 35, 0.9)" : "rgba(240,253,244,0.86)",
                                        borderColor: "primary.main"
                                    }
                                }}
                            >
                                {t("landing.demo")}
                            </Button>
                        </Stack>

                        <Box
                            sx={{
                                display: "grid",
                                gridTemplateColumns: { xs: "repeat(3, 1fr)", sm: "repeat(3, auto)" },
                                gap: { xs: 2, md: 5 },
                                maxWidth: 640
                            }}
                        >
                            {[
                                ["10K+", t("landing.stats.activeUsers")],
                                ["500+", t("landing.stats.doctors")],
                                ["99.9%", t("landing.stats.uptime")]
                            ].map(([value, label]) => (
                                <Box key={label}>
                                    <Typography sx={{ fontSize: { xs: 26, md: 36 }, fontWeight: 800 }}>
                                        {value}
                                    </Typography>
                                    <Typography sx={{ color: "text.secondary", fontSize: 14 }}>
                                        {label}
                                    </Typography>
                                </Box>
                            ))}
                        </Box>
                    </Box>

                    <Paper
                        elevation={0}
                        sx={{
                            p: { xs: 2.5, md: 4 },
                            borderRadius: 3,
                            border: "1px solid",
                            borderColor: vitta.border,
                            bgcolor: isDark ? "rgba(16, 38, 23, 0.84)" : "rgba(255,255,255,0.82)",
                            boxShadow: vitta.shadow,
                            backdropFilter: "blur(18px)",
                            overflow: "hidden"
                        }}
                    >
                        <Stack direction="row" justifyContent="space-between" alignItems="flex-start" sx={{ mb: 3 }}>
                            <Box>
                                <Typography sx={{ fontSize: { xs: 22, md: 28 }, fontWeight: 800 }}>
                                    {t("landing.cardTitle")}
                                </Typography>
                                <Typography sx={{ color: "text.secondary" }}>
                                    {t("landing.cardSubtitle")}
                                </Typography>
                            </Box>
                            <Chip label={t("landing.normal")} color="success" variant="outlined" />
                        </Stack>

                        <Box
                            sx={{
                                borderRadius: 3,
                                p: 3,
                                mb: 3,
                                color: "#ffffff",
                                background: "linear-gradient(135deg, #16a34a 0%, #0f766e 72%, #0ea5e9 100%)"
                            }}
                        >
                            <Typography sx={{ fontSize: { xs: 48, md: 64 }, fontWeight: 800, lineHeight: 1 }}>
                                72
                                <Box component="span" sx={{ fontSize: 22, ml: 1, fontWeight: 600 }}>
                                    bpm
                                </Box>
                            </Typography>
                            <Typography sx={{ mt: 1, color: "rgba(255,255,255,0.82)" }}>
                                {t("landing.current")} - {t("landing.normal")}
                            </Typography>
                        </Box>

                        <Box
                            sx={{
                                height: { xs: 190, md: 260 },
                                borderRadius: 3,
                                bgcolor: isDark ? "rgba(7, 26, 18, 0.72)" : "#f3faf8",
                                position: "relative",
                                overflow: "hidden",
                                mb: 3,
                                border: "1px solid",
                                borderColor: vitta.border
                            }}
                        >
                            {[1, 2, 3, 4].map((i) => (
                                <Box
                                    key={i}
                                    sx={{
                                        position: "absolute",
                                        left: 0,
                                        right: 0,
                                        top: `${i * 20}%`,
                                        borderTop: `1px dashed ${isDark ? "rgba(220, 252, 231, 0.14)" : "rgba(22, 163, 74, 0.18)"}`
                                    }}
                                />
                            ))}
                            <svg
                                width="100%"
                                height="100%"
                                viewBox="0 0 500 260"
                                style={{ position: "absolute", inset: 0 }}
                            >
                                <path
                                    d="M20 160 C 80 118, 125 182, 184 142 S 275 92, 342 134 S 430 156, 480 102"
                                    fill="none"
                                    stroke="#16a34a"
                                    strokeWidth="5"
                                    strokeLinecap="round"
                                />
                                <path
                                    d="M20 160 C 80 118, 125 182, 184 142 S 275 92, 342 134 S 430 156, 480 102"
                                    fill="none"
                                    stroke="#0ea5e9"
                                    strokeOpacity="0.22"
                                    strokeWidth="18"
                                    strokeLinecap="round"
                                />
                            </svg>
                        </Box>

                        <Grid container spacing={2}>
                            {[
                                [t("landing.min"), "65"],
                                [t("landing.avg"), "72"],
                                [t("landing.max"), "81"]
                            ].map(([label, value]) => (
                                <Grid item xs={4} key={label}>
                                    <Box
                                        sx={{
                                            borderRadius: 2,
                                            bgcolor: isDark ? "rgba(34, 197, 94, 0.12)" : "rgba(22, 163, 74, 0.08)",
                                            py: 2,
                                            textAlign: "center"
                                        }}
                                    >
                                        <Typography sx={{ color: "text.secondary", fontSize: 13 }}>
                                            {label}
                                        </Typography>
                                        <Typography sx={{ fontSize: 28, fontWeight: 800 }}>
                                            {value}
                                        </Typography>
                                    </Box>
                                </Grid>
                            ))}
                        </Grid>
                    </Paper>
                </Box>
            </Container>

            <Box
                sx={{
                    bgcolor: isDark ? "rgba(7, 26, 18, 0.62)" : "rgba(255,255,255,0.58)",
                    borderTop: "1px solid",
                    borderColor: "divider"
                }}
            >
                <Container maxWidth="xl" sx={{ py: { xs: 7, md: 10 } }}>
                    <Box sx={{ maxWidth: 760, mb: 5 }}>
                        <Typography sx={{ fontSize: { xs: 30, md: 46 }, fontWeight: 800, mb: 2 }}>
                            {t("landing.vitalTitle")}
                        </Typography>
                        <Typography sx={{ color: "text.secondary", fontSize: { xs: 16, md: 18 }, lineHeight: 1.7 }}>
                            {t("landing.vitalDescription")}
                        </Typography>
                    </Box>

                    <Grid container spacing={2.5}>
                        {cards.map(([title, description], index) => (
                            <Grid item xs={12} sm={6} lg={4} key={title}>
                                <Paper
                                    elevation={0}
                                    sx={{
                                        height: "100%",
                                        p: 3,
                                        borderRadius: 2,
                                        border: "1px solid",
                                        borderColor: vitta.border,
                                        bgcolor: "background.paper",
                                        transition: "border-color .18s ease, box-shadow .18s ease, transform .18s ease, background-color .18s ease",
                                        "&:hover": {
                                            transform: "translateY(-1px)",
                                            boxShadow: vitta.shadow,
                                            borderColor: vitta.borderStrong
                                        }
                                    }}
                                >
                                    <Box
                                        sx={{
                                            width: 48,
                                            height: 48,
                                            borderRadius: 2,
                                            display: "grid",
                                            placeItems: "center",
                                            color: "primary.contrastText",
                                            bgcolor: "primary.main",
                                            mb: 2,
                                            "& svg": { fontSize: 25 }
                                        }}
                                    >
                                        {vitalIcons[index]}
                                    </Box>
                                    <Typography sx={{ fontSize: 19, fontWeight: 800, mb: 1 }}>
                                        {title}
                                    </Typography>
                                    <Typography sx={{ color: "text.secondary", lineHeight: 1.65 }}>
                                        {description}
                                    </Typography>
                                </Paper>
                            </Grid>
                        ))}
                    </Grid>
                </Container>
            </Box>

            <Container maxWidth="xl" sx={{ py: { xs: 7, md: 10 } }}>
                <Typography sx={{ fontSize: { xs: 30, md: 44 }, fontWeight: 800, mb: 4 }}>
                    {t("landing.benefitsTitle")}
                </Typography>

                <Grid container spacing={2.5}>
                    {benefits.map(([title, description], index) => (
                        <Grid item xs={12} md={4} key={title}>
                            <Paper
                                elevation={0}
                                sx={{
                                    height: "100%",
                                    p: 3,
                                    borderRadius: 2,
                                    border: "1px solid",
                                    borderColor: vitta.border,
                                    bgcolor: "background.paper",
                                    display: "flex",
                                    gap: 2
                                }}
                            >
                                <Box
                                    sx={{
                                        minWidth: 44,
                                        width: 44,
                                        height: 44,
                                        borderRadius: 2,
                                        display: "grid",
                                        placeItems: "center",
                                        color: "primary.main",
                                        bgcolor: isDark ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.1)"
                                    }}
                                >
                                    {benefitIcons[index]}
                                </Box>
                                <Box>
                                    <Typography sx={{ fontWeight: 800, mb: 0.75 }}>
                                        {title}
                                    </Typography>
                                    <Typography sx={{ color: "text.secondary", lineHeight: 1.6 }}>
                                        {description}
                                    </Typography>
                                </Box>
                            </Paper>
                        </Grid>
                    ))}
                </Grid>
            </Container>

            <Box component="footer" sx={{ pb: 4 }}>
                <Container maxWidth="xl">
                    <Paper
                        elevation={0}
                        sx={{
                            p: { xs: 3, md: 5 },
                            borderRadius: 3,
                            color: "#e8f7f4",
                            background: isDark
                                ? "linear-gradient(135deg, #0b1f12 0%, #0f3f3b 64%, #083f61 100%)"
                                : "linear-gradient(135deg, #14532d 0%, #0f766e 72%, #0369a1 100%)",
                            border: "1px solid",
                            borderColor: isDark ? "rgba(220, 252, 231, 0.12)" : "transparent",
                            boxShadow: vitta.shadow
                        }}
                    >
                        <Grid container spacing={4}>
                            <Grid item xs={12} md={5}>
                                <Stack direction="row" alignItems="center" spacing={1.5} sx={{ mb: 2 }}>
                                    <Box
                                        sx={{
                                            width: 42,
                                            height: 42,
                                            borderRadius: 2,
                                            display: "grid",
                                            placeItems: "center",
                                            bgcolor: "rgba(255,255,255,0.12)"
                                        }}
                                    >
                                        <MonitorHeartOutlinedIcon />
                                    </Box>
                                    <Typography sx={{ fontSize: 28, fontWeight: 800 }}>
                                        {t("brand")}
                                    </Typography>
                                </Stack>
                                <Typography sx={{ maxWidth: 360, color: "rgba(232,247,244,0.78)", lineHeight: 1.7 }}>
                                    {t("landing.footerText")}
                                </Typography>
                            </Grid>

                            <Grid item xs={6} md={3}>
                                <Stack spacing={1.4}>
                                    {t("landing.footerLinks").map((item) => (
                                        <Typography key={item} sx={{ color: "rgba(232,247,244,0.78)" }}>
                                            {item}
                                        </Typography>
                                    ))}
                                </Stack>
                            </Grid>

                            <Grid item xs={6} md={4}>
                                <Stack spacing={1.4}>
                                    {t("landing.supportLinks").map((item) => (
                                        <Typography key={item} sx={{ color: "rgba(232,247,244,0.78)" }}>
                                            {item}
                                        </Typography>
                                    ))}
                                </Stack>
                            </Grid>
                        </Grid>

                        <Box
                            sx={{
                                mt: 4,
                                pt: 3,
                                borderTop: "1px solid rgba(232,247,244,0.16)",
                                color: "rgba(232,247,244,0.68)",
                                fontSize: 14
                            }}
                        >
                            © 2026 {t("brand")}. {t("landing.rights")}
                        </Box>
                    </Paper>
                </Container>
            </Box>
        </Box>
    );
}
