import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import { Box, Chip, Paper, Tooltip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { useMemo } from "react";

const categoryOrder = {
    critico: 0,
    moderado: 1,
    saudavel: 2,
    "n/a": 3
};

const categoryStyles = {
    saudavel: { color: "#15803d", background: "rgba(34, 197, 94, 0.13)", accent: "#22c55e" },
    moderado: { color: "#b45309", background: "rgba(245, 158, 11, 0.14)", accent: "#f59e0b" },
    critico: { color: "#dc2626", background: "rgba(239, 68, 68, 0.13)", accent: "#ef4444" },
    "n/a": { color: "#64748b", background: "rgba(100, 116, 139, 0.13)", accent: "#94a3b8" }
};

export default function ClinicalStability({ items = [], period, t }) {
    const theme = useTheme();
    const general = items.find((item) => item.tipo === "geral");
    const factors = useMemo(
        () => items
            .filter((item) => item.tipo !== "geral")
            .sort((first, second) => {
                const categoryDifference = (categoryOrder[first.categoria] ?? 3) - (categoryOrder[second.categoria] ?? 3);
                return categoryDifference || (first.indice ?? 11) - (second.indice ?? 11);
            }),
        [items]
    );

    const generalCategory = general?.categoria || "n/a";
    const generalStyle = categoryStyles[generalCategory] || categoryStyles["n/a"];
    const generalValue = general?.indice != null ? `${general.indice}/10` : "—";

    return (
        <Paper
            sx={{
                p: { xs: 2.5, md: 3 },
                mb: 3,
                borderRadius: 3,
                border: "1px solid",
                borderColor: theme.vitta.border,
                boxShadow: theme.vitta.shadow,
                backgroundColor: "background.paper"
            }}
        >
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: { xs: "1fr", lg: "minmax(220px, 0.8fr) minmax(0, 2.2fr)" },
                    gap: 3
                }}
            >
                <Box
                    sx={{
                        p: 2.5,
                        borderRadius: 2.5,
                        borderLeft: `5px solid ${generalStyle.accent}`,
                        background: generalStyle.background
                    }}
                >
                    <Typography variant="body2" sx={{ fontWeight: 800, color: "text.secondary" }}>
                        {t("dashboard.stability.title")}
                    </Typography>
                    <Typography variant="h3" sx={{ mt: 0.75, fontWeight: 900, color: generalStyle.color }}>
                        {generalValue}
                    </Typography>
                    <Typography variant="h6" sx={{ fontWeight: 800, color: generalStyle.color }}>
                        {t(`dashboard.stability.categories.${generalCategory}`)}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                        {t("dashboard.stability.period").replace("{days}", period)}
                    </Typography>
                </Box>

                <Box sx={{ minWidth: 0 }}>
                    <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 2 }}>
                        <Typography variant="h6" sx={{ fontWeight: 800 }}>
                            {t("dashboard.stability.factorsTitle")}
                        </Typography>
                        <Tooltip title={t("dashboard.stability.weightHelp")} arrow>
                            <InfoOutlinedIcon fontSize="small" color="action" />
                        </Tooltip>
                    </Box>

                    {factors.length === 0 ? (
                        <Typography color="text.secondary">{t("dashboard.stability.noFactors")}</Typography>
                    ) : (
                        <Box
                            sx={{
                                display: "grid",
                                gridTemplateColumns: { xs: "1fr", sm: "repeat(2, minmax(0, 1fr))", xl: "repeat(3, minmax(0, 1fr))" },
                                gap: 1.5
                            }}
                        >
                            {factors.map((factor) => {
                                const factorCategory = factor.categoria || "n/a";
                                const factorStyle = categoryStyles[factorCategory] || categoryStyles["n/a"];

                                return (
                                    <Box
                                        key={factor.tipo}
                                        sx={{
                                            p: 1.75,
                                            borderRadius: 2,
                                            border: "1px solid",
                                            borderColor: theme.vitta.border,
                                            minWidth: 0
                                        }}
                                    >
                                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", gap: 1 }}>
                                            <Typography sx={{ fontWeight: 800 }}>
                                                {t(`dashboard.stability.factors.${factor.tipo}`)}
                                            </Typography>
                                            <Typography sx={{ fontWeight: 900, color: factorStyle.color, whiteSpace: "nowrap" }}>
                                                {factor.indice != null ? `${factor.indice}/10` : "—"}
                                            </Typography>
                                        </Box>
                                        <Box sx={{ mt: 1, display: "flex", alignItems: "center", justifyContent: "space-between", gap: 1 }}>
                                            <Chip
                                                size="small"
                                                label={t(`dashboard.stability.categories.${factorCategory}`)}
                                                sx={{ color: factorStyle.color, bgcolor: factorStyle.background, fontWeight: 800 }}
                                            />
                                            <Tooltip title={t("dashboard.stability.weight").replace("{weight}", factor.peso ?? 1)} arrow>
                                                <Typography variant="caption" color="text.secondary" sx={{ cursor: "help" }}>
                                                    {t("dashboard.stability.weightShort").replace("{weight}", factor.peso ?? 1)}
                                                </Typography>
                                            </Tooltip>
                                        </Box>
                                    </Box>
                                );
                            })}
                        </Box>
                    )}
                </Box>
            </Box>

            <Typography variant="caption" color="text.secondary" sx={{ display: "block", mt: 2 }}>
                {t("dashboard.stability.disclaimer")}
            </Typography>
        </Paper>
    );
}
