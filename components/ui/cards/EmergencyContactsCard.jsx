import { useEffect, useState } from "react";

import {
    Box,
    Button,
    Checkbox,
    Chip,
    Divider,
    FormControlLabel,
    FormControl,
    FormGroup,
    FormLabel,
    Grid,
    IconButton,
    Paper,
    Tooltip,
    Typography
} from "@mui/material";
import { useTheme } from "@mui/material/styles";

import AddIcon from "@mui/icons-material/Add";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import EmailOutlinedIcon from "@mui/icons-material/EmailOutlined";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";
import PhoneOutlinedIcon from "@mui/icons-material/PhoneOutlined";

import { usePatient } from "../../../context/PatientContext";
import { useAlert } from "../../../hooks/useAlert";
import {
    createEmergencyContact,
    deleteEmergencyContact,
    editEmergencyContact,
    getEmergencyContacts
} from "../../../services/contactEmergencyService";
import { formatPhone, isValidPhone } from "../../../utils/formatters/formatPhone";
import DialogUI from "../Dialog";
import InputUI from "../Input";
import { useI18n } from "../../../src/i18n";
import { useThemeMode } from "../../../src/theme/ThemeModeProvider";

const emptyContact = {
    nome: "",
    telefone: "",
    email: "",
    receberAlertaSinaisVitaisSaudavel: false,
    receberAlertaSinaisVitaisModerado: false,
    receberAlertaSinaisVitaisCritico: true,
    receberAlertaHabitosSaudavel: false,
    receberAlertaHabitosModerado: true,
    receberAlertaHabitosCritico: true,
    receberAlertaGeralSaudavel: false,
    receberAlertaGeralModerado: false,
    receberAlertaGeralCritico: true,
    canalEmail: true,
    canalSms: false
};

const alertGroups = [
    {
        labelKey: "vitalSigns",
        fields: [
            ["receberAlertaSinaisVitaisSaudavel", "healthy"],
            ["receberAlertaSinaisVitaisModerado", "moderate"],
            ["receberAlertaSinaisVitaisCritico", "critical"]
        ]
    },
    {
        labelKey: "habits",
        fields: [
            ["receberAlertaHabitosSaudavel", "healthy"],
            ["receberAlertaHabitosModerado", "moderate"],
            ["receberAlertaHabitosCritico", "critical"]
        ]
    },
    {
        labelKey: "generalStability",
        fields: [
            ["receberAlertaGeralSaudavel", "healthy"],
            ["receberAlertaGeralModerado", "moderate"],
            ["receberAlertaGeralCritico", "critical"]
        ]
    }
];

function AlertPreferences({ data, onChange, t, accessibilityMode, channelError }) {
    const checkboxSx = accessibilityMode
        ? {
            p: 1.25,
            "& .MuiSvgIcon-root": { fontSize: 28 },
            "&:focus-visible": {
                outline: "3px solid",
                outlineColor: "secondary.main",
                outlineOffset: 1
            }
        }
        : undefined;

    return (
        <Box mt={2}>
            <Divider sx={{ mb: 2 }} />

            <FormControl component="fieldset" error={channelError} fullWidth>
                <FormLabel component="legend" sx={{ fontWeight: 800, color: "text.primary" }}>
                    {t("reports.emergencyContacts.channels")}
                </FormLabel>
                <Typography variant="body2" color="text.secondary" mb={1}>
                    {t("reports.emergencyContacts.channelsDescription")}
                </Typography>

                <FormGroup row sx={{ gap: 1 }}>
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={Boolean(data.canalEmail)}
                                onChange={(event) => onChange("canalEmail", event.target.checked)}
                                sx={checkboxSx}
                            />
                        }
                        label={t("reports.emergencyContacts.emailChannel")}
                    />
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={Boolean(data.canalSms)}
                                onChange={(event) => onChange("canalSms", event.target.checked)}
                                sx={checkboxSx}
                            />
                        }
                        label={t("reports.emergencyContacts.smsChannel")}
                    />
                </FormGroup>
                {channelError && accessibilityMode && (
                    <Typography variant="caption" color="error.main">
                        {t("reports.emergencyContacts.selectChannel")}
                    </Typography>
                )}
            </FormControl>

            <Typography component="h4" fontWeight={800} mt={2} mb={0.5}>
                {t("reports.emergencyContacts.alertPreferences")}
            </Typography>

            {alertGroups.map((group) => (
                <Box key={group.labelKey} mt={1.5}>
                    <FormControl component="fieldset" fullWidth>
                        <FormLabel component="legend" sx={{ fontSize: "0.9rem", fontWeight: 800, color: "text.secondary" }}>
                            {t(`reports.emergencyContacts.${group.labelKey}`)}
                        </FormLabel>
                        <FormGroup row sx={{ gap: 0.5 }}>
                            {group.fields.map(([field, label]) => (
                                <FormControlLabel
                                    key={field}
                                    control={
                                        <Checkbox
                                            size="small"
                                            checked={Boolean(data[field])}
                                            onChange={(event) => onChange(field, event.target.checked)}
                                            sx={checkboxSx}
                                        />
                                    }
                                    label={t(`reports.emergencyContacts.${label}`)}
                                />
                            ))}
                        </FormGroup>
                    </FormControl>
                </Box>
            ))}
        </Box>
    );
}

function toEditableContact(contact = {}) {
    return Object.keys(emptyContact).reduce(
        (result, field) => ({
            ...result,
            [field]: contact[field] ?? emptyContact[field]
        }),
        {}
    );
}

const iconButtonSx = {
    add: {
        color: "#ffffff",
        bgcolor: "#16a34a",
        border: "1px solid rgba(22, 163, 74, 0.2)",
        "&:hover": { bgcolor: "#15803d" }
    },
    edit: {
        color: "#0f766e",
        bgcolor: "rgba(15, 118, 110, 0.08)",
        border: "1px solid rgba(15, 118, 110, 0.16)"
    },
    cancel: {
        color: "#dc2626",
        bgcolor: "rgba(220, 38, 38, 0.08)",
        border: "1px solid rgba(220, 38, 38, 0.14)"
    },
    delete: {
        color: "#dc2626",
        bgcolor: "rgba(220, 38, 38, 0.08)",
        border: "1px solid rgba(220, 38, 38, 0.14)"
    },
    save: {
        color: "#ffffff",
        bgcolor: "#16a34a",
        border: "1px solid rgba(22, 163, 74, 0.2)",
        "&:hover": { bgcolor: "#15803d" }
    }
};

export default function EmergencyContacts() {
    const { showAlert } = useAlert();
    const { selectedPatient } = usePatient();
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();
    const { accessibilityMode } = useThemeMode();

    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [adding, setAdding] = useState(false);
    const [formData, setFormData] = useState(emptyContact);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [selectedContactId, setSelectedContactId] = useState(null);
    const [editingId, setEditingId] = useState(null);
    const [editData, setEditData] = useState(emptyContact);
    const [hasError, setHasError] = useState({
        nome: false,
        telefone: false,
        email: false,
        channel: false
    });

    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const loggedCpf = localStorage.getItem("CPF");
    const isPersonalContext = selectedPatient?.cpf === loggedCpf;
    const canEdit =
        (userType === "paciente" && isPersonalContext) ||
        (userType === "responsavel" && !isPersonalContext);
    const shouldShowContacts =
        userType === "paciente" ||
        (userType !== "paciente" && !isPersonalContext);
    const actionButtonSx = {
        add: {
            color: "#ffffff",
            bgcolor: "primary.main",
            border: "1px solid",
            borderColor: vitta.borderStrong,
            "&:hover": { bgcolor: "primary.dark" }
        },
        edit: {
            color: "secondary.main",
            bgcolor: isDark ? "rgba(14, 165, 233, 0.12)" : "rgba(15, 118, 110, 0.08)",
            border: "1px solid",
            borderColor: isDark ? "rgba(14, 165, 233, 0.22)" : "rgba(15, 118, 110, 0.16)"
        },
        cancel: iconButtonSx.cancel,
        delete: iconButtonSx.delete,
        save: {
            color: "#ffffff",
            bgcolor: "primary.main",
            border: "1px solid",
            borderColor: vitta.borderStrong,
            "&:hover": { bgcolor: "primary.dark" }
        }
    };
    const accessibleActionSx = {
        minHeight: 48,
        px: 2,
        borderRadius: 2,
        fontWeight: 800,
        textTransform: "none",
        flex: { xs: "1 1 140px", sm: "0 0 auto" },
        "&:focus-visible": {
            outline: "3px solid",
            outlineColor: "secondary.main",
            outlineOffset: 2
        }
    };
    const accessibleInputSx = accessibilityMode
        ? {
            "& .MuiOutlinedInput-root": { minHeight: 52 },
            "& .MuiInputBase-input": { fontSize: "1rem" },
            "& .MuiFormHelperText-root": { fontSize: "0.9rem", lineHeight: 1.4 }
        }
        : undefined;

    function resetErrors() {
        setHasError({
            nome: false,
            telefone: false,
            email: false,
            channel: false
        });
    }

    function resetForm() {
        setFormData(emptyContact);
        resetErrors();
    }

    function validateContact(data) {
        const newErrors = {
            nome: false,
            telefone: false,
            email: false,
            channel: false
        };

        if (data.nome.trim().length < 5) {
            newErrors.nome = true;
            showAlert("warning", t("reports.emergencyContacts.shortName"));
        }

        if (!isValidPhone(data.telefone)) {
            newErrors.telefone = true;
            showAlert("warning", t("reports.emergencyContacts.invalidPhone"));
        }

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email.trim())) {
            newErrors.email = true;
            showAlert("warning", t("reports.emergencyContacts.invalidEmail"));
        }

        if (!data.canalEmail && !data.canalSms) {
            newErrors.channel = true;
            showAlert("warning", t("reports.emergencyContacts.selectChannel"));
            setHasError(newErrors);
            return false;
        }

        setHasError(newErrors);

        return !(newErrors.nome || newErrors.telefone || newErrors.email);
    }

    function startEdit(contact) {
        setAdding(false);
        setEditingId(contact.id);
        setEditData(toEditableContact(contact));
        resetErrors();
    }

    async function handleSaveContact() {
        if (!validateContact(formData)) return;

        try {
            const created = await createEmergencyContact(
                selectedPatient.cpf,
                formData
            );

            setContacts((prev) => [...prev, created]);
            setAdding(false);
            resetForm();
            showAlert("success", t("reports.emergencyContacts.saved"));
        } catch (error) {
            console.error(error);
            showAlert("error", t("reports.emergencyContacts.saveError"));
        }
    }

    async function confirmDeleteContact() {
        try {
            await deleteEmergencyContact(
                selectedContactId,
                selectedPatient.cpf
            );

            setContacts((prev) =>
                prev.filter((contact) => contact.id !== selectedContactId)
            );

            showAlert("success", t("reports.emergencyContacts.removed"));
        } catch (error) {
            console.error(error);
            showAlert("error", t("reports.emergencyContacts.removeError"));
        } finally {
            setOpenDeleteDialog(false);
            setSelectedContactId(null);
        }
    }

    async function saveEdit(id) {
        if (!validateContact(editData)) return;

        try {
            const updated = await editEmergencyContact(
                id,
                selectedPatient.cpf,
                editData
            );

            setContacts((prev) =>
                prev.map((contact) =>
                    contact.id === id
                        ? updated
                        : contact
                )
            );

            setEditingId(null);
            showAlert("success", t("reports.emergencyContacts.updated"));
        } catch (error) {
            console.error(error);
            showAlert("error", t("reports.emergencyContacts.editError"));
        }
    }

    useEffect(() => {
        if (!selectedPatient?.cpf) return;

        async function fetchContacts() {
            try {
                setLoading(true);
                const data = await getEmergencyContacts(selectedPatient.cpf);
                setContacts(data);
            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
            }
        }

        setAdding(false);
        setEditingId(null);
        setFormData(emptyContact);
        setHasError({
            nome: false,
            telefone: false,
            email: false,
            channel: false
        });
        fetchContacts();
    }, [selectedPatient?.cpf]);

    if (!shouldShowContacts) {
        return null;
    }

    return (
        <Paper
            elevation={0}
            sx={{
                p: { xs: 2, md: 3 },
                borderRadius: 3,
                border: "1px solid",
                borderColor: vitta.border,
                boxShadow: vitta.shadow,
                bgcolor: "background.paper",
                minWidth: 0,
                overflow: "hidden"
            }}
        >
            <Box
                display="flex"
                alignItems={{ xs: "stretch", sm: "center" }}
                justifyContent="space-between"
                flexDirection={{ xs: "column", sm: "row" }}
                gap={2}
                mb={2.5}
                sx={{ minWidth: 0 }}
            >
                <Box sx={{ minWidth: 0 }}>
                    <Typography
                        sx={{
                            fontWeight: 800,
                            color: "text.primary",
                            fontSize: "1.2rem",
                            overflowWrap: "break-word",
                            wordBreak: "normal"
                        }}
                    >
                        {t("reports.emergencyContacts.title")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            fontSize: accessibilityMode ? "1rem" : "0.9rem",
                            mt: 0.25,
                            overflowWrap: "break-word",
                            wordBreak: "normal"
                        }}
                    >
                        {t("reports.emergencyContacts.description")}
                    </Typography>
                </Box>

                {canEdit && !adding && contacts.length < 3 && (
                    accessibilityMode ? (
                        <Button
                            onClick={() => {
                                setAdding(true);
                                setEditingId(null);
                                resetForm();
                            }}
                            startIcon={<AddIcon />}
                            sx={{ ...actionButtonSx.add, ...accessibleActionSx }}
                        >
                            {t("reports.emergencyContacts.add")}
                        </Button>
                    ) : (
                        <Tooltip title={t("reports.emergencyContacts.add")}>
                            <IconButton
                                onClick={() => {
                                    setAdding(true);
                                    setEditingId(null);
                                    resetForm();
                                }}
                                aria-label={t("reports.emergencyContacts.add")}
                                sx={actionButtonSx.add}
                            >
                                <AddIcon />
                            </IconButton>
                        </Tooltip>
                    )
                )}
            </Box>

            {loading && (
                <Typography color="text.secondary">
                    {t("reports.emergencyContacts.loading")}
                </Typography>
            )}

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: {
                        xs: "minmax(0, 1fr)",
                        md: "repeat(2, minmax(0, 1fr))",
                        lg: "minmax(0, 1fr)"
                    },
                    gap: 2,
                    minWidth: 0
                }}
            >
                {contacts.map((contact) => {
                    const isEditing = editingId === contact.id;

                    return (
                        <Paper
                            key={contact.id}
                            elevation={0}
                            sx={{
                                p: 2,
                                borderRadius: 3,
                                border: "1px solid",
                                borderColor: vitta.border,
                                background: "background.paper",
                                minWidth: 0
                            }}
                        >
                            {isEditing ? (
                                <Box sx={{ minWidth: 0 }}>
                                    <Grid container spacing={1.5}>
                                        <Grid item xs={12}>
                                            <InputUI
                                                label={t("reports.profile.name")}
                                                placeholder={t("reports.profile.nameExample")}
                                                value={editData.nome}
                                                error={hasError.nome}
                                                helperText={accessibilityMode && hasError.nome ? t("reports.emergencyContacts.shortName") : undefined}
                                                sx={accessibleInputSx}
                                                onChange={(event) => {
                                                    setEditData({
                                                        ...editData,
                                                        nome: event.target.value
                                                    });
                                                    setHasError({
                                                        ...hasError,
                                                        nome: false
                                                    });
                                                }}
                                            />
                                        </Grid>

                                        <Grid item xs={12}>
                                            <InputUI
                                                label={t("reports.profile.phone")}
                                                error={hasError.telefone}
                                                placeholder={t("reports.profile.phoneExample")}
                                                helperText={accessibilityMode && hasError.telefone ? t("reports.emergencyContacts.invalidPhone") : undefined}
                                                sx={accessibleInputSx}
                                                limit={15}
                                                value={formatPhone(editData.telefone)}
                                                onChange={(event) => {
                                                    const rawValue =
                                                        event.target.value.replace(/\D/g, "");

                                                    setEditData({
                                                        ...editData,
                                                        telefone: rawValue
                                                    });

                                                    setHasError({
                                                        ...hasError,
                                                        telefone: false
                                                    });
                                                }}
                                            />
                                        </Grid>

                                        <Grid item xs={12}>
                                            <InputUI
                                                label={t("reports.emergencyContacts.email")}
                                                placeholder={t("reports.profile.emailExample")}
                                                type="email"
                                                value={editData.email}
                                                error={hasError.email}
                                                helperText={accessibilityMode && hasError.email ? t("reports.emergencyContacts.invalidEmail") : undefined}
                                                sx={accessibleInputSx}
                                                onChange={(event) => {
                                                    setEditData({
                                                        ...editData,
                                                        email: event.target.value
                                                    });
                                                    setHasError({
                                                        ...hasError,
                                                        email: false
                                                    });
                                                }}
                                            />
                                        </Grid>
                                    </Grid>

                                    <AlertPreferences
                                        data={editData}
                                        t={t}
                                        accessibilityMode={accessibilityMode}
                                        channelError={hasError.channel}
                                        onChange={(field, value) => {
                                            setEditData((current) => ({
                                                ...current,
                                                [field]: value
                                            }));
                                            if ((field === "canalEmail" || field === "canalSms") && value) {
                                                setHasError((current) => ({ ...current, channel: false }));
                                            }
                                        }}
                                    />

                                    <Box display="flex" justifyContent="flex-end" gap={1} mt={1} flexWrap="wrap">
                                        {accessibilityMode ? (
                                            <>
                                                <Button
                                                    onClick={() => {
                                                        setEditingId(null);
                                                        resetErrors();
                                                    }}
                                                    startIcon={<CloseIcon />}
                                                    sx={{ ...actionButtonSx.cancel, ...accessibleActionSx }}
                                                >
                                                    {t("reports.profile.cancel")}
                                                </Button>
                                                <Button onClick={() => saveEdit(contact.id)} startIcon={<CheckIcon />} sx={{ ...actionButtonSx.save, ...accessibleActionSx }}>
                                                    {t("reports.profile.save")}
                                                </Button>
                                            </>
                                        ) : (
                                            <>
                                                <Tooltip title={t("reports.profile.cancel")}>
                                                    <IconButton
                                                        onClick={() => {
                                                            setEditingId(null);
                                                            resetErrors();
                                                        }}
                                                        aria-label={t("reports.profile.cancel")}
                                                        sx={actionButtonSx.cancel}
                                                    >
                                                        <CloseIcon />
                                                    </IconButton>
                                                </Tooltip>

                                                <Tooltip title={t("reports.profile.save")}>
                                                    <IconButton onClick={() => saveEdit(contact.id)} aria-label={t("reports.profile.save")} sx={actionButtonSx.save}>
                                                        <CheckIcon />
                                                    </IconButton>
                                                </Tooltip>
                                            </>
                                        )}
                                    </Box>
                                </Box>
                            ) : (
                                <Box
                                    display="flex"
                                    justifyContent="space-between"
                                    alignItems="flex-start"
                                    gap={2}
                                    sx={{
                                        minWidth: 0,
                                        flexDirection: accessibilityMode
                                            ? { xs: "column", sm: "row" }
                                            : "row"
                                    }}
                                >
                                    <Box sx={{ minWidth: 0 }}>
                                        <Box
                                            display="flex"
                                            alignItems="center"
                                            gap={1}
                                            sx={{ minWidth: 0 }}
                                        >
                                            <PersonOutlineIcon
                                                sx={{
                                                    fontSize: 20,
                                                    color: "primary.main",
                                                    flex: "0 0 auto"
                                                }}
                                            />

                                            <Typography
                                                sx={{
                                                    fontWeight: 800,
                                                    color: "text.primary",
                                                    overflow: accessibilityMode ? "visible" : "hidden",
                                                    textOverflow: accessibilityMode ? "clip" : "ellipsis",
                                                    whiteSpace: accessibilityMode ? "normal" : "nowrap",
                                                    overflowWrap: "anywhere"
                                                }}
                                            >
                                                {contact.nome}
                                            </Typography>
                                        </Box>

                                        <Box
                                            display="flex"
                                            alignItems="center"
                                            gap={1}
                                            mt={0.75}
                                            sx={{ minWidth: 0 }}
                                        >
                                            <PhoneOutlinedIcon
                                                sx={{
                                                    fontSize: 20,
                                                    color: "secondary.main",
                                                    flex: "0 0 auto"
                                                }}
                                            />

                                            <Typography
                                                sx={{
                                                    color: "text.secondary",
                                                    overflow: accessibilityMode ? "visible" : "hidden",
                                                    textOverflow: accessibilityMode ? "clip" : "ellipsis",
                                                    whiteSpace: accessibilityMode ? "normal" : "nowrap",
                                                    overflowWrap: "anywhere"
                                                }}
                                            >
                                                {formatPhone(contact.telefone)}
                                            </Typography>
                                        </Box>

                                        <Box
                                            display="flex"
                                            alignItems="center"
                                            gap={1}
                                            mt={0.75}
                                            sx={{ minWidth: 0 }}
                                        >
                                            <EmailOutlinedIcon
                                                sx={{
                                                    fontSize: 20,
                                                    color: "secondary.main",
                                                    flex: "0 0 auto"
                                                }}
                                            />
                                            <Typography
                                                sx={{
                                                    color: "text.secondary",
                                                    overflow: accessibilityMode ? "visible" : "hidden",
                                                    textOverflow: accessibilityMode ? "clip" : "ellipsis",
                                                    whiteSpace: accessibilityMode ? "normal" : "nowrap",
                                                    overflowWrap: "anywhere"
                                                }}
                                            >
                                                {contact.email}
                                            </Typography>
                                        </Box>

                                        <Box display="flex" flexWrap="wrap" gap={0.75} mt={1.25}>
                                            {contact.canalEmail && (
                                                <Chip
                                                    size="small"
                                                    label={t("reports.emergencyContacts.emailChannel")}
                                                    color="primary"
                                                    variant="outlined"
                                                />
                                            )}
                                            {contact.canalSms && (
                                                <Chip
                                                    size="small"
                                                    label={t("reports.emergencyContacts.smsChannel")}
                                                    color="secondary"
                                                    variant="outlined"
                                                />
                                            )}
                                            {contact.receberAlertaSinaisVitaisCritico && (
                                                <Chip
                                                    size="small"
                                                    label={t("reports.emergencyContacts.criticalAlerts")}
                                                    sx={{
                                                        color: "#b91c1c",
                                                        borderColor: "rgba(220, 38, 38, 0.35)",
                                                        bgcolor: "rgba(220, 38, 38, 0.06)"
                                                    }}
                                                    variant="outlined"
                                                />
                                            )}
                                        </Box>
                                    </Box>

                                    {canEdit && (
                                        <Box display="flex" gap={1} flexShrink={0} flexWrap="wrap">
                                            {accessibilityMode ? (
                                                <>
                                                    <Button
                                                        onClick={() => startEdit(contact)}
                                                        startIcon={<EditOutlinedIcon />}
                                                        sx={{ ...actionButtonSx.edit, ...accessibleActionSx }}
                                                    >
                                                        {t("reports.profile.edit")}
                                                    </Button>
                                                    <Button
                                                        onClick={() => {
                                                            setSelectedContactId(contact.id);
                                                            setOpenDeleteDialog(true);
                                                        }}
                                                        startIcon={<DeleteOutlineIcon />}
                                                        sx={{ ...actionButtonSx.delete, ...accessibleActionSx }}
                                                    >
                                                        {t("reports.emergencyContacts.remove")}
                                                    </Button>
                                                </>
                                            ) : (
                                                <>
                                                    <Tooltip title={t("reports.profile.edit")}>
                                                        <IconButton
                                                            onClick={() => startEdit(contact)}
                                                            size="small"
                                                            aria-label={t("reports.profile.edit")}
                                                            sx={actionButtonSx.edit}
                                                        >
                                                            <EditOutlinedIcon fontSize="small" />
                                                        </IconButton>
                                                    </Tooltip>

                                                    <Tooltip title={t("reports.emergencyContacts.remove")}>
                                                        <IconButton
                                                            onClick={() => {
                                                                setSelectedContactId(contact.id);
                                                                setOpenDeleteDialog(true);
                                                            }}
                                                            size="small"
                                                            aria-label={t("reports.emergencyContacts.remove")}
                                                            sx={actionButtonSx.delete}
                                                        >
                                                            <DeleteOutlineIcon fontSize="small" />
                                                        </IconButton>
                                                    </Tooltip>
                                                </>
                                            )}
                                        </Box>
                                    )}
                                </Box>
                            )}
                        </Paper>
                    );
                })}
            </Box>

            {!loading && contacts.length === 0 && !adding && (
                <Box
                    sx={{
                        width: "100%",
                        py: 4,
                        textAlign: "center"
                    }}
                >
                    <Typography color="text.secondary">
                        {t("reports.emergencyContacts.noContacts")}
                    </Typography>
                </Box>
            )}

            {adding && (
                <Paper
                    elevation={0}
                    sx={{
                        mt: 2,
                        p: 2,
                        borderRadius: 3,
                        border: "1px solid",
                        borderColor: vitta.border,
                        background: isDark ? "rgba(220, 252, 231, 0.06)" : "rgba(240, 253, 244, 0.42)",
                        minWidth: 0
                    }}
                >
                    <Grid container spacing={1.5}>
                        <Grid item xs={12} md={6} lg={12}>
                            <InputUI
                                label={t("reports.profile.name")}
                                placeholder={t("reports.profile.nameExample")}
                                error={hasError.nome}
                                helperText={accessibilityMode && hasError.nome ? t("reports.emergencyContacts.shortName") : undefined}
                                sx={accessibleInputSx}
                                fullWidth
                                value={formData.nome}
                                onChange={(event) => {
                                    setHasError({
                                        ...hasError,
                                        nome: false
                                    });

                                    setFormData({
                                        ...formData,
                                        nome: event.target.value
                                    });
                                }}
                            />
                        </Grid>

                        <Grid item xs={12} md={6} lg={12}>
                            <InputUI
                                label={t("reports.profile.phone")}
                                error={hasError.telefone}
                                helperText={accessibilityMode && hasError.telefone ? t("reports.emergencyContacts.invalidPhone") : undefined}
                                sx={accessibleInputSx}
                                fullWidth
                                placeholder={t("reports.profile.phoneExample")}
                                limit={15}
                                value={formatPhone(formData.telefone)}
                                onChange={(event) => {
                                    const rawValue =
                                        event.target.value.replace(/\D/g, "");

                                    setHasError({
                                        ...hasError,
                                        telefone: false
                                    });

                                    setFormData({
                                        ...formData,
                                        telefone: rawValue
                                    });
                                }}
                            />
                        </Grid>

                        <Grid item xs={12}>
                            <InputUI
                                label={t("reports.emergencyContacts.email")}
                                type="email"
                                error={hasError.email}
                                helperText={accessibilityMode && hasError.email ? t("reports.emergencyContacts.invalidEmail") : undefined}
                                sx={accessibleInputSx}
                                fullWidth
                                placeholder={t("reports.profile.emailExample")}
                                value={formData.email}
                                onChange={(event) => {
                                    setHasError({
                                        ...hasError,
                                        email: false
                                    });

                                    setFormData({
                                        ...formData,
                                        email: event.target.value
                                    });
                                }}
                            />
                        </Grid>
                    </Grid>

                    <AlertPreferences
                        data={formData}
                        t={t}
                        accessibilityMode={accessibilityMode}
                        channelError={hasError.channel}
                        onChange={(field, value) => {
                            setFormData((current) => ({
                                ...current,
                                [field]: value
                            }));
                            if ((field === "canalEmail" || field === "canalSms") && value) {
                                setHasError((current) => ({ ...current, channel: false }));
                            }
                        }}
                    />

                    <Box display="flex" justifyContent="flex-end" gap={1} mt={1} flexWrap="wrap">
                        {accessibilityMode ? (
                            <>
                                <Button
                                    onClick={() => {
                                        setAdding(false);
                                        resetForm();
                                    }}
                                    startIcon={<CloseIcon />}
                                    sx={{ ...actionButtonSx.cancel, ...accessibleActionSx }}
                                >
                                    {t("reports.profile.cancel")}
                                </Button>
                                <Button
                                    onClick={handleSaveContact}
                                    startIcon={<CheckIcon />}
                                    sx={{ ...actionButtonSx.save, ...accessibleActionSx }}
                                >
                                    {t("reports.emergencyContacts.saveContact")}
                                </Button>
                            </>
                        ) : (
                            <>
                                <Tooltip title={t("reports.profile.cancel")}>
                                    <IconButton
                                        onClick={() => {
                                            setAdding(false);
                                            resetForm();
                                        }}
                                        aria-label={t("reports.profile.cancel")}
                                        sx={actionButtonSx.cancel}
                                    >
                                        <CloseIcon />
                                    </IconButton>
                                </Tooltip>

                                <Tooltip title={t("reports.emergencyContacts.saveContact")}>
                                    <IconButton
                                        onClick={handleSaveContact}
                                        aria-label={t("reports.emergencyContacts.saveContact")}
                                        sx={actionButtonSx.save}
                                    >
                                        <CheckIcon />
                                    </IconButton>
                                </Tooltip>
                            </>
                        )}
                    </Box>
                </Paper>
            )}

            <DialogUI
                open={openDeleteDialog}
                onClose={() => {
                    setOpenDeleteDialog(false);
                    setSelectedContactId(null);
                }}
                title={t("reports.emergencyContacts.removeContact")}
                onConfirm={confirmDeleteContact}
                confirmText={t("reports.emergencyContacts.remove")}
                cancelText={t("reports.profile.cancel")}
            >
                <Typography>
                    {t("reports.emergencyContacts.removeQuestion")}
                </Typography>
            </DialogUI>
        </Paper>
    );
}
