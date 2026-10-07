import {
    Alert,
    Box,
    Button,
    Checkbox,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControlLabel,
    IconButton,
    Paper,
    Stack,
    Tooltip,
    Typography
} from "@mui/material";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import ArrowUpwardIcon from "@mui/icons-material/ArrowUpward";
import RestartAltIcon from "@mui/icons-material/RestartAlt";

import { DASHBOARD_WIDGETS } from "../domain/dashboardLayout";

const widgetLabels = new Map(DASHBOARD_WIDGETS.map((widget) => [widget.id, widget.labelKey]));

export default function DashboardCustomizationDialog({
    open,
    draft,
    saving,
    error,
    accessibilityMode,
    t,
    onClose,
    onSave,
    onRestore,
    onToggle,
    onMove
}) {
    const visibleCount = draft.widgets.filter((widget) => widget.visible).length;
    const actionSx = accessibilityMode
        ? {
            minHeight: 48,
            px: 2,
            fontWeight: 800,
            textTransform: "none",
            "&:focus-visible": {
                outline: "3px solid",
                outlineColor: "secondary.main",
                outlineOffset: 2
            }
        }
        : { textTransform: "none", fontWeight: 700 };

    return (
        <Dialog open={open} onClose={saving ? undefined : onClose} fullWidth maxWidth="sm">
            <DialogTitle sx={{ fontWeight: 800 }}>
                {t("dashboard.customization.title")}
            </DialogTitle>

            <DialogContent>
                <Typography color="text.secondary" sx={{ mb: 2, fontSize: accessibilityMode ? "1rem" : undefined }}>
                    {t("dashboard.customization.description")}
                </Typography>

                {error && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        {t("dashboard.customization.saveError")}
                    </Alert>
                )}

                <Stack spacing={1.5} role="list" aria-label={t("dashboard.customization.widgetList")}>
                    {draft.widgets.map((widget, index) => {
                        const label = t(widgetLabels.get(widget.id));
                        const cannotHide = widget.visible && visibleCount === 1;

                        return (
                            <Paper
                                key={widget.id}
                                role="listitem"
                                variant="outlined"
                                sx={{
                                    p: 1.5,
                                    display: "flex",
                                    alignItems: { xs: "stretch", sm: "center" },
                                    justifyContent: "space-between",
                                    flexDirection: { xs: "column", sm: "row" },
                                    gap: 1.5,
                                    borderRadius: 2
                                }}
                            >
                                <FormControlLabel
                                    sx={{ m: 0, minHeight: accessibilityMode ? 48 : undefined }}
                                    control={
                                        <Checkbox
                                            checked={widget.visible}
                                            disabled={saving || cannotHide}
                                            onChange={(event) => onToggle(widget.id, event.target.checked)}
                                            inputProps={{ "aria-label": label }}
                                            sx={accessibilityMode ? { "& .MuiSvgIcon-root": { fontSize: 28 } } : undefined}
                                        />
                                    }
                                    label={<Typography sx={{ fontWeight: 800, fontSize: accessibilityMode ? "1rem" : undefined }}>{label}</Typography>}
                                />

                                <Box display="flex" gap={1}>
                                    <Tooltip title={t("dashboard.customization.moveUp")}>
                                        <span>
                                            <IconButton
                                                disabled={saving || index === 0}
                                                onClick={() => onMove(widget.id, "up")}
                                                aria-label={`${t("dashboard.customization.moveUp")}: ${label}`}
                                                sx={accessibilityMode ? { minWidth: 48, minHeight: 48 } : undefined}
                                            >
                                                <ArrowUpwardIcon />
                                            </IconButton>
                                        </span>
                                    </Tooltip>
                                    <Tooltip title={t("dashboard.customization.moveDown")}>
                                        <span>
                                            <IconButton
                                                disabled={saving || index === draft.widgets.length - 1}
                                                onClick={() => onMove(widget.id, "down")}
                                                aria-label={`${t("dashboard.customization.moveDown")}: ${label}`}
                                                sx={accessibilityMode ? { minWidth: 48, minHeight: 48 } : undefined}
                                            >
                                                <ArrowDownwardIcon />
                                            </IconButton>
                                        </span>
                                    </Tooltip>
                                </Box>
                            </Paper>
                        );
                    })}
                </Stack>

                <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
                    {t("dashboard.customization.minimumWidget")}
                </Typography>
            </DialogContent>

            <DialogActions sx={{ px: 3, pb: 2.5, flexWrap: "wrap", gap: 1 }}>
                <Button
                    onClick={onRestore}
                    disabled={saving}
                    startIcon={<RestartAltIcon />}
                    sx={{ ...actionSx, mr: { sm: "auto" }, width: { xs: "100%", sm: "auto" } }}
                >
                    {t("dashboard.customization.restore")}
                </Button>
                <Button onClick={onClose} disabled={saving} sx={{ ...actionSx, width: { xs: "100%", sm: "auto" } }}>
                    {t("common.cancel")}
                </Button>
                <Button
                    onClick={onSave}
                    disabled={saving}
                    variant="contained"
                    sx={{ ...actionSx, width: { xs: "100%", sm: "auto" } }}
                >
                    {saving ? t("dashboard.customization.saving") : t("common.save")}
                </Button>
            </DialogActions>
        </Dialog>
    );
}
