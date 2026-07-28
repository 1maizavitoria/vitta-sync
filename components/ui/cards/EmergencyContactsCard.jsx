import { useEffect, useState } from "react";

import {
    Box,
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

const emptyContact = {
    nome: "",
    telefone: ""
};

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
        telefone: false
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

    function resetErrors() {
        setHasError({
            nome: false,
            telefone: false
        });
    }

    function resetForm() {
        setFormData(emptyContact);
        resetErrors();
    }

    function validateContact(data) {
        const newErrors = {
            nome: false,
            telefone: false
        };

        if (data.nome.trim().length < 5) {
            newErrors.nome = true;
            showAlert("warning", t("reports.emergencyContacts.shortName"));
        }

        if (!isValidPhone(data.telefone)) {
            newErrors.telefone = true;
            showAlert("warning", t("reports.emergencyContacts.invalidPhone"));
        }

        setHasError(newErrors);

        return !(newErrors.nome || newErrors.telefone);
    }

    function startEdit(contact) {
        setAdding(false);
        setEditingId(contact.id);
        setEditData({
            nome: contact.nome,
            telefone: contact.telefone
        });
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
            telefone: false
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
                alignItems="center"
                justifyContent="space-between"
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
                            overflowWrap: "anywhere"
                        }}
                    >
                        {t("reports.emergencyContacts.title")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            fontSize: "0.9rem",
                            mt: 0.25,
                            overflowWrap: "anywhere"
                        }}
                    >
                        {t("reports.emergencyContacts.description")}
                    </Typography>
                </Box>

                {canEdit && !adding && contacts.length < 3 && (
                    <Tooltip title={t("reports.emergencyContacts.add")}>
                        <IconButton
                            onClick={() => {
                                setAdding(true);
                                setEditingId(null);
                                resetForm();
                            }}
                            sx={actionButtonSx.add}
                        >
                            <AddIcon />
                        </IconButton>
                    </Tooltip>
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
                                                value={editData.nome}
                                                error={hasError.nome}
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
                                                placeholder="(11) 99999-9999"
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
                                    </Grid>

                                    <Box display="flex" justifyContent="flex-end" gap={1} mt={1}>
                                        <Tooltip title={t("reports.profile.cancel")}>
                                            <IconButton
                                                onClick={() => {
                                                    setEditingId(null);
                                                    resetErrors();
                                                }}
                                                sx={actionButtonSx.cancel}
                                            >
                                                <CloseIcon />
                                            </IconButton>
                                        </Tooltip>

                                        <Tooltip title={t("reports.profile.save")}>
                                            <IconButton
                                                onClick={() => saveEdit(contact.id)}
                                                sx={actionButtonSx.save}
                                            >
                                                <CheckIcon />
                                            </IconButton>
                                        </Tooltip>
                                    </Box>
                                </Box>
                            ) : (
                                <Box
                                    display="flex"
                                    justifyContent="space-between"
                                    alignItems="flex-start"
                                    gap={2}
                                    sx={{ minWidth: 0 }}
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
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                    whiteSpace: "nowrap"
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
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                    whiteSpace: "nowrap"
                                                }}
                                            >
                                                {formatPhone(contact.telefone)}
                                            </Typography>
                                        </Box>
                                    </Box>

                                    {canEdit && (
                                        <Box display="flex" gap={1} flexShrink={0}>
                                            <Tooltip title={t("reports.profile.edit")}>
                                                <IconButton
                                                    onClick={() => startEdit(contact)}
                                                    size="small"
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
                                                    sx={actionButtonSx.delete}
                                                >
                                                    <DeleteOutlineIcon fontSize="small" />
                                                </IconButton>
                                            </Tooltip>
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
                                error={hasError.nome}
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
                                fullWidth
                                placeholder="(11) 99999-9999"
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
                    </Grid>

                    <Box display="flex" justifyContent="flex-end" gap={1} mt={1}>
                        <Tooltip title={t("reports.profile.cancel")}>
                            <IconButton
                                onClick={() => {
                                    setAdding(false);
                                    resetForm();
                                }}
                                sx={actionButtonSx.cancel}
                            >
                                <CloseIcon />
                            </IconButton>
                        </Tooltip>

                        <Tooltip title={t("reports.emergencyContacts.saveContact")}>
                            <IconButton
                                onClick={handleSaveContact}
                                sx={actionButtonSx.save}
                            >
                                <CheckIcon />
                            </IconButton>
                        </Tooltip>
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
