import { useEffect, useState } from "react";

import { Box, Grid, IconButton, Paper, Tooltip, Typography } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";

import { usePatient } from "../../context/PatientContext";
import { useAlert } from "../../hooks/useAlert";
import { logout } from "../../services/authService";
import { deleteUser, editUser, getUserByCpf } from "../../services/userService";
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { formatPhone, isValidPhone } from "../../utils/formatters/formatPhone";
import { getDateLimit, isUnder18 } from "../../utils/validators/dateValidator";
import DatePickerUI from "./DatePicker";
import InputUI from "./Input";
import { useI18n } from "../../src/i18n";

const emptyForm = {
    nome: "",
    dataNascimento: "",
    cpf: "",
    email: "",
    tipo: "",
    conselho: "",
    telefone: "",
    pesoInicial: "",
    altura: "",
    funcaoResponsavel: ""
};

const iconButtonSx = {
    edit: {
        color: "#0f766e",
        bgcolor: "rgba(15, 118, 110, 0.08)",
        border: "1px solid rgba(15, 118, 110, 0.16)"
    },
    save: {
        color: "#ffffff",
        bgcolor: "#16a34a",
        border: "1px solid rgba(22, 163, 74, 0.2)",
        "&:hover": { bgcolor: "#15803d" }
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
    }
};

export default function Perfil({ view = "patient" }) {
    const { selectedPatient } = usePatient();
    const { showAlert } = useAlert();
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();

    const [editing, setEditing] = useState(false);
    const [error, setError] = useState(false);
    const [errorEmail, setErrorEmail] = useState(false);
    const [errorName, setErrorName] = useState(false);
    const [formData, setFormData] = useState(emptyForm);

    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const loggedCpf = localStorage.getItem("CPF");
    const isProfileView = view === "profile";

    const targetCpf =
        isProfileView || userType === "paciente"
            ? loggedCpf
            : selectedPatient?.cpf;

    const canEdit =
        isProfileView
            ? userType !== "saude"
            : userType === "paciente" && loggedCpf === targetCpf;

    const canManage = canEdit || loggedCpf === targetCpf;
    const actionButtonSx = {
        edit: {
            color: "secondary.main",
            bgcolor: isDark ? "rgba(14, 165, 233, 0.12)" : "rgba(15, 118, 110, 0.08)",
            border: "1px solid",
            borderColor: isDark ? "rgba(14, 165, 233, 0.22)" : "rgba(15, 118, 110, 0.16)"
        },
        save: {
            color: "#ffffff",
            bgcolor: "primary.main",
            border: "1px solid",
            borderColor: vitta.borderStrong,
            "&:hover": { bgcolor: "primary.dark" }
        },
        cancel: iconButtonSx.cancel,
        delete: iconButtonSx.delete
    };

    const title =
        isProfileView
            ? t("reports.profile.profileTitle")
            : userType === "paciente"
                ? t("reports.profile.myDataTitle")
                : `${t("reports.profile.patientInfoTitle")} ${selectedPatient?.nome || t("reports.profile.patientFallback")}`;

    const subtitle =
        isProfileView
            ? t("reports.profile.profileSubtitle")
            : userType === "paciente"
                ? t("reports.profile.myDataSubtitle")
                : t("reports.profile.patientSubtitle");

    const handleChange = (campo) => (value) => {
        setFormData((prev) => ({
            ...prev,
            [campo]: value,
            ...(campo === "tipo" && { dataNascimento: getDateLimit(value) })
        }));
    };

    function resetErrors() {
        setError(false);
        setErrorEmail(false);
        setErrorName(false);
    }

    function closeEditing() {
        setEditing(false);
        resetErrors();
    }

    function canSave() {
        if (!formData.nome || !formData.email || !formData.dataNascimento) {
            showAlert("error", t("messages.fillAll"));
            setError(true);
            return false;
        }

        if (formData.nome.length < 5) {
            setErrorName(true);
            showAlert("error", t("messages.shortName"));
            return false;
        }

        if (!isValidEmail(formData.email)) {
            setErrorEmail(true);
            showAlert("error", t("messages.invalidEmail"));
            return false;
        }

        if (!isValidPhone(formData.telefone)) {
            showAlert("error", t("messages.invalidPhone"));
            return false;
        }

        if (formData.tipo === "paciente") {
            if (Number(formData.pesoInicial) <= 0) {
                showAlert("error", t("messages.invalidWeight"));
                return false;
            }

            if (Number(formData.altura) <= 0) {
                showAlert("error", t("messages.invalidHeight"));
                return false;
            }
        }

        if (
            (formData?.tipo === "responsavel" || formData?.tipo === "saude") &&
            isUnder18(formData.dataNascimento)
        ) {
            showAlert("error", `"${formData.tipo}" ${t("reports.profile.adultRequired")}`);
            setError(true);
            return false;
        }

        resetErrors();
        return true;
    }

    async function handleChangeSave() {
        if (!canSave()) return;

        const data = {
            nome: formData.nome,
            email: formData.email,
            dataNascimento: formData.dataNascimento,
            telefone: formData.telefone,
            ...(formData.tipo === "paciente" && {
                pesoInicial: Number(formData.pesoInicial),
                altura: Number(formData.altura),
            }),
            ...(formData.tipo === "responsavel" && {
                funcaoResponsavel: formData.funcaoResponsavel,
            }),
            ...(formData.tipo === "saude" && {
                conselho: formData.conselho,
            }),
        };

        try {
            await editUser(formData.cpf, data);
            setEditing(false);
            showAlert("success", t("reports.profile.updated"));
        } catch (saveError) {
            setEditing(true);
            showAlert("error", t("reports.profile.saveError"));
            console.error("Erro ao salvar dados:", saveError);
        }
    }

    async function handleDelete() {
        const confirmed = window.confirm(
            t("reports.profile.deleteConfirmation")
        );

        if (!confirmed) return;

        try {
            await deleteUser(formData.cpf);
            showAlert("success", t("reports.profile.accountDeleted"));
        } catch (deleteError) {
            showAlert("error", t("reports.profile.deleteError"));
            console.error(deleteError);
            return;
        }

        try {
            await logout();
        } catch (logoutError) {
            console.error("Erro no logout:", logoutError);
        }
    }

    useEffect(() => {
        if (!targetCpf) return;

        async function fetchUser() {
            try {
                const data = await getUserByCpf({
                    CPF: targetCpf
                });

                setFormData({
                    nome: data.nome || "",
                    telefone: data.telefone || "",
                    pesoInicial: data.pesoInicial || "",
                    altura: data.altura || "",
                    dataNascimento: data.dataNascimento || "",
                    cpf: data.cpf || "",
                    email: data.email || "",
                    tipo: data.tipo || "",
                    conselho: data.conselho || "",
                    funcaoResponsavel: data.funcaoResponsavel || "",
                });
            } catch (fetchError) {
                console.error(fetchError);
            }
        }

        fetchUser();
    }, [targetCpf]);

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
                flexDirection={{ xs: "column", sm: "row" }}
                justifyContent="space-between"
                alignItems={{ xs: "flex-start", sm: "center" }}
                gap={2}
                mb={3}
                sx={{ minWidth: 0 }}
            >
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 1.5,
                        minWidth: 0
                    }}
                >
                    <Box
                        sx={{
                            width: 46,
                            height: 46,
                            borderRadius: 2,
                            background: isDark
                                ? "linear-gradient(135deg, rgba(34, 197, 94, 0.18) 0%, rgba(14, 165, 233, 0.14) 100%)"
                                : "linear-gradient(135deg, #dcfce7 0%, #e0f2fe 100%)",
                            color: isDark ? "#bbf7d0" : "#166534",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            flex: "0 0 auto"
                        }}
                    >
                        <PersonOutlineIcon />
                    </Box>

                    <Box sx={{ minWidth: 0 }}>
                        <Typography
                            sx={{
                                fontWeight: 800,
                                color: "text.primary",
                                fontSize: { xs: "1.25rem", md: "1.45rem" },
                                overflowWrap: "anywhere"
                            }}
                        >
                            {title}
                        </Typography>

                        <Typography
                            sx={{
                                color: "text.secondary",
                                fontSize: "0.9rem",
                                mt: 0.25,
                                overflowWrap: "anywhere"
                            }}
                        >
                            {subtitle}
                        </Typography>
                    </Box>
                </Box>

                {canManage && (
                    <Box display="flex" gap={1} flexShrink={0}>
                        {!editing && (
                            <Tooltip title={t("reports.profile.edit")}>
                                <IconButton
                                    onClick={() => setEditing(true)}
                                    sx={actionButtonSx.edit}
                                >
                                    <EditOutlinedIcon />
                                </IconButton>
                            </Tooltip>
                        )}

                        {editing && (
                            <>
                                <Tooltip title={t("reports.profile.deleteAccount")}>
                                    <IconButton
                                        onClick={handleDelete}
                                        sx={actionButtonSx.delete}
                                    >
                                        <DeleteOutlineIcon />
                                    </IconButton>
                                </Tooltip>

                                <Tooltip title={t("reports.profile.cancel")}>
                                    <IconButton
                                        onClick={closeEditing}
                                        sx={actionButtonSx.cancel}
                                    >
                                        <CloseIcon />
                                    </IconButton>
                                </Tooltip>

                                <Tooltip title={t("reports.profile.save")}>
                                    <IconButton
                                        onClick={handleChangeSave}
                                        sx={actionButtonSx.save}
                                    >
                                        <CheckIcon />
                                    </IconButton>
                                </Tooltip>
                            </>
                        )}
                    </Box>
                )}
            </Box>

            <Grid container spacing={2}>
                <Grid item xs={12} md={6}>
                    <InputUI
                        label={t("reports.profile.name")}
                        value={formData.nome}
                        onChange={(event) => {
                            handleChange("nome")(event.target.value);
                            setErrorName(false);
                        }}
                        disabled={!editing}
                        fullWidth
                        error={(error && !formData.nome) || errorName}
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <InputUI
                        label={t("reports.profile.phone")}
                        limit={15}
                        value={formatPhone(formData.telefone)}
                        onChange={(event) => {
                            const rawValue = event.target.value.replace(/\D/g, "");
                            handleChange("telefone")(rawValue);
                        }}
                        disabled={!editing}
                        fullWidth
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <DatePickerUI
                        label={t("reports.profile.birthDate")}
                        value={formData.dataNascimento}
                        onChange={(value) =>
                            setFormData({
                                ...formData,
                                dataNascimento: value
                            })
                        }
                        disabled={!editing}
                        fullWidth
                        error={error && !formData.dataNascimento}
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <InputUI
                        label={t("reports.profile.email")}
                        value={formData.email}
                        onChange={(event) => {
                            handleChange("email")(event.target.value);
                            setErrorEmail(false);
                        }}
                        disabled={!editing}
                        fullWidth
                        error={(error && !formData.email) || errorEmail}
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <InputUI
                        label={t("reports.profile.cpf")}
                        value={formData.cpf}
                        disabled
                        fullWidth
                    />
                </Grid>

                {formData.tipo === "saude" && (
                    <Grid item xs={12} md={6}>
                        <InputUI
                            label={t("reports.profile.council")}
                            value={formData.conselho}
                            onChange={(event) =>
                                handleChange("conselho")(event.target.value)
                            }
                            disabled={!editing}
                            fullWidth
                            error={error}
                        />
                    </Grid>
                )}

                {formData.tipo === "paciente" && (
                    <Grid item xs={12} md={6}>
                        <InputUI
                            label={t("reports.profile.initialWeight")}
                            type="number"
                            value={formData.pesoInicial}
                            onChange={(event) =>
                                handleChange("pesoInicial")(event.target.value)
                            }
                            disabled={!editing}
                            fullWidth
                        />
                    </Grid>
                )}

                {formData.tipo === "paciente" && (
                    <Grid item xs={12} md={6}>
                        <InputUI
                            label={t("reports.profile.height")}
                            type="number"
                            value={formData.altura}
                            onChange={(event) =>
                                handleChange("altura")(event.target.value)
                            }
                            disabled={!editing}
                            fullWidth
                        />
                    </Grid>
                )}
            </Grid>
        </Paper>
    );
}
