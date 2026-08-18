import { Box, Typography, IconButton, Button, TextField, Stack, Chip, Skeleton, Tabs, Tab } from "@mui/material";
import { useTheme } from "@mui/material/styles";

import { useCallback, useEffect, useState } from "react";

import { useAlert } from "../../hooks/useAlert";
import { getLinksByPatientId, removeLink, generateLinkCode, joinWithCode, sendInviteEmail } from "../../services/linkService";

import ButtonUI from "../../components/ui/Button";
import DialogUI from "../../components/ui/Dialog";
import InputUI from "../../components/ui/Input";

import AddIcon from "@mui/icons-material/Add";
import CloseIcon from "@mui/icons-material/Close";
import ContentCopyIcon from "@mui/icons-material/ContentCopy";
import DeleteIcon from "@mui/icons-material/Delete";

import { useNavigate } from "react-router-dom";

import MemberCard from "../../components/ui/cards/MemberCard";

import { usePatient } from "../../context/PatientContext";

import AutocompleteUI from "../../components/ui/Autocomplete";

import {
    funcoesGrupo, funcoesMedico, getResponsavelStyle, translateFunctionOptions,
    getMedicoStyle
} from "../../utils/validators/userFunction";
import { useI18n } from "../../src/i18n";

export default function PatientHub() {

    const navigate = useNavigate();
    const { showAlert } = useAlert();
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();

    const {
        selectedPatient,
        setSelectedPatient,
        addPatient,
        removePatient
    } = usePatient();

    const [links, setLinks] = useState([]);
    const [openModal, setOpenModal] = useState(false);
    const [generatedCode, setGeneratedCode] = useState("");
    const [generatedLink, setGeneratedLink] = useState("");
    const [openJoinModal, setOpenJoinModal] = useState(false);

    const [joinCode, setJoinCode] = useState("");
    const [openRemoveDialog, setOpenRemoveDialog] = useState(false);
    const [selectedLinkId, setSelectedLinkId] = useState(null);
    const [loading, setLoading] = useState(false);

    const [emailInput, setEmailInput] = useState("");
    const [emails, setEmails] = useState([]);
    const [selectedTab, setSelectedTab] = useState(0);
    const [isLeavingGroup, setIsLeavingGroup] = useState(false);
    const [sendingEmails, setSendingEmails] = useState(false);
    const userType = localStorage.getItem("tipo");
    const [funcao, setFuncao] = useState("");

    const [errorFuncao, setErrorFuncao] = useState(false);

    const canInvite =
        ["paciente", "responsavel"]
            .includes(
                userType?.toLowerCase()
            );

    const precisaEscolherFuncao =
        ["responsavel", "saude"]
            .includes(
                userType?.toLowerCase()
            );

    const baseOpcoesFuncao =
        userType?.toLowerCase() === "saude"
            ? funcoesMedico
            : funcoesGrupo;

    const opcoesFuncao = translateFunctionOptions(baseOpcoesFuncao, t);

    const loadLinks = useCallback(async () => {
        if (!selectedPatient?.id) {
            return;
        }

        try {

            setLoading(true);

            const data =
                await getLinksByPatientId(selectedPatient?.id);

            setLinks(data);

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                t("patientHub.alerts.loadLinksError")
            );

        } finally {

            setLoading(false);
        }
    }, [selectedPatient?.id, showAlert, t]);

    async function handleRemoveLink(id) {
        try {

            await removeLink(id);

            setLinks(prev =>
                prev.filter(
                    link => link.id !== id
                )
            );

            if (isLeavingGroup) {

                removePatient(selectedPatient.id);

                setSelectedPatient(null);
            }

            showAlert(
                "success",
                t("patientHub.alerts.linkRemovedSuccess")
            );

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                t("patientHub.alerts.removeLinkError")
            );
        }
    }

    async function confirmRemoveLink() {

        if (!selectedLinkId) {
            return;
        }

        await handleRemoveLink(
            selectedLinkId
        );

        setOpenRemoveDialog(false);

        setSelectedLinkId(null);
    }

    async function handleGenerateCode() {
        if (!selectedPatient) {

            showAlert(
                "warning",
                t("patientHub.alerts.selectPatient")
            );

            return;
        }

        try {

            const data = await generateLinkCode(selectedPatient.id);
            setSelectedTab(0);
            setGeneratedCode(data.codigo);
            setGeneratedLink(data.link);
            setOpenModal(true);

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                t("patientHub.alerts.generateCodeError")
            );
        }
    }

    async function handleCopy(text) {

        try {

            await navigator.clipboard.writeText(text);

            showAlert(
                "success",
                t("patientHub.alerts.copiedSuccess")
            );

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                t("patientHub.alerts.copyError")
            );
        }
    }

    async function handleJoinWithCode() {

        if (
            precisaEscolherFuncao
            &&
            !funcao
        ) {

            setErrorFuncao(true);

            showAlert(
                "warning",
                t("patientHub.alerts.selectRole")
            );

            return;
        }

        try {

            const patient =
                await joinWithCode(
                    joinCode,
                    funcao
                );

            addPatient(patient);

            setSelectedPatient(patient);

            showAlert(
                "success",
                t("patientHub.alerts.linkCreatedSuccess")
            );

            setOpenJoinModal(false);

            setJoinCode("");

            setFuncao("");

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                t("patientHub.alerts.invalidCode")
            );
        }
    }

    function handleAddEmail() {

        const formattedEmail =
            emailInput.trim().toLowerCase();

        if (!formattedEmail) {
            return;
        }

        const emailExists =
            emails.includes(formattedEmail);

        if (emailExists) {

            showAlert(
                "warning",
                t("patientHub.alerts.emailAlreadyAdded")
            );

            return;
        }

        setEmails((prev) => [
            ...prev,
            formattedEmail
        ]);

        setEmailInput("");
    }

    function handleRemoveEmail(email) {

        setEmails((prev) =>
            prev.filter((item) =>
                item !== email
            )
        );
    }

    async function handleSendEmails() {

        if (emails.length === 0) {

            showAlert(
                "warning",
                t("patientHub.alerts.addAtLeastOneEmail")
            );

            return;
        }

        try {

            setSendingEmails(true);

            await Promise.all(

                emails.map((email) =>

                    sendInviteEmail(
                        email,
                        generatedCode
                    )
                )
            );

            showAlert(
                "success",
                t("patientHub.alerts.invitesSentSuccess")
            );

            setEmails([]);

            setEmailInput("");

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                t("patientHub.alerts.sendInvitesError")
            );

        } finally {

            setSendingEmails(false);
        }
    }

    async function handleOpenPerfil() {
        try {
            // await refreshPatients();
            navigate("/reports")
        } catch (error) {
            console.error(error);
        }
    }

    function getTypeColor(type, funcao) {

        switch (type?.toLowerCase()) {

            case "paciente":
                return {
                    background: isDark ? "rgba(14, 165, 233, 0.14)" : "#e3f2fd",
                    color: isDark ? "#7dd3fc" : "#0369a1",
                    label: t("userTypes.saude")
                };

            case "saude":
                return getMedicoStyle(funcao, t);

            case "responsavel":

                return getResponsavelStyle(funcao, t);



            default:
                return {
                    background: isDark ? "rgba(220, 252, 231, 0.1)" : "#eeeeee",
                    color: isDark ? "text.secondary" : "#616161",
                    label: type
                };
        }
    }

    const pacientes = links.filter(
        (link) =>
            link.tipo?.toLowerCase() === "paciente"
    );

    const responsaveis = links.filter(
        (link) =>
            link.tipo?.toLowerCase() === "responsavel"
    );

    const medicos = links.filter(
        (link) =>
            link.tipo?.toLowerCase() === "saude"
    );

    const isPaciente = userType?.toLowerCase() === "paciente";

    const myLink = links.find((link) => {
        return (
            link.tipo?.toLowerCase()
            === userType?.toLowerCase()
        );
    });

    const panelSx = {
        backgroundColor: "background.paper",
        borderRadius: 3,
        minWidth: 0,
        overflow: "hidden",
        border: "1px solid",
        borderColor: vitta.border,
        boxShadow: vitta.shadow
    };

    const textWrapSx = {
        minWidth: 0,
        overflowWrap: "anywhere",
        wordBreak: "break-word"
    };

    const supportingTextSx = {
        ...textWrapSx,
        color: "text.secondary"
    };

    const moduleCardSx = {
        ...panelSx,
        padding: 3,
        cursor: "pointer",
        minWidth: 0,
        minHeight: 160,
        transition: "transform .2s ease, box-shadow .2s ease, border-color .2s ease",
        "&:hover": {
            transform: "translateY(-4px)",
            borderColor: vitta.borderStrong,
            boxShadow: vitta.shadow
        }
    };

    function canRemove(targetType) {

        const currentUser =
            userType?.toLowerCase();

        const target =
            targetType?.toLowerCase();

        // paciente remove todos
        if (currentUser === "paciente") {
            return target !== "paciente";
        }

        // responsável remove apenas médicos
        if (currentUser === "responsavel") {
            return target === "saude";
        }

        // médico não remove ninguém
        return false;
    }

    useEffect(() => {

        if (!selectedPatient?.id) {

            setLinks([]);

            return;
        }

        loadLinks();

    }, [selectedPatient, loadLinks]);

    return (

        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "background.default",
                background: vitta.pageBackground,
                color: "text.primary",
                px: { xs: 2, md: 4 },
                py: { xs: 3, md: 4 },
                overflowX: "hidden"
            }}
        >

            <Box
                display="flex"

                flexDirection={{
                    xs: "column",
                    md: "row"
                }}

                justifyContent="space-between"

                alignItems={{
                    xs: "flex-start",
                    md: "center"
                }}

                gap={3}

                mb={4}
            >

                {selectedPatient &&
                    <Box
                        sx={{
                            minWidth: 0,
                            width: { xs: "100%", md: "auto" }
                        }}
                    >

                        <Typography
                            variant="h4"
                            sx={{
                                fontWeight: 800,
                                color: "text.primary",
                                letterSpacing: 0,
                                ...textWrapSx
                            }}
                        >
                            {t("patientHub.groupTitle")} {selectedPatient?.nome || t("userTypes.paciente")}
                        </Typography>

                        <Typography
                            sx={{
                                color: "text.secondary",
                                mt: 0.5,
                                ...textWrapSx
                            }}
                        >
                            {t("patientHub.groupSubtitle")}
                        </Typography>

                    </Box>}

                <Box
                    sx={{
                        ...panelSx,
                        width: { xs: "100%", md: 420 },
                        maxWidth: "100%",
                        padding: 3
                    }}
                >

                    <Typography
                        variant="h6"
                        sx={{
                            fontWeight: 800,
                            mb: 1
                        }}
                    >
                        {t("patientHub.groupActions")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            mb: 3,
                            ...textWrapSx
                        }}
                    >
                        {t("patientHub.groupActionsDescription")}
                    </Typography>

                    <Box
                        display="flex"
                        flexWrap="wrap"
                        gap={2}
                    >

                        {canInvite && (

                            <ButtonUI
                                onClick={handleGenerateCode}
                                sx={{
                                    textTransform: "none",
                                    minWidth: 0,
                                    whiteSpace: "normal",
                                    width: { xs: "100%", sm: "auto" },
                                }}
                            >
                                {t("patientHub.inviteParticipant")}
                            </ButtonUI>
                        )}

                        {userType?.toLowerCase() !==
                            "paciente" && (

                                <Button
                                    variant="contained"
                                    onClick={() =>
                                        setOpenJoinModal(true)
                                    }
                                    sx={{
                                        borderRadius: 2,
                                        textTransform: "none",
                                        fontWeight: 800,
                                        px: 2.2,
                                        py: 1.05,
                                        minWidth: 0,
                                        whiteSpace: "normal",
                                        width: { xs: "100%", sm: "auto" },
                                        zIndex: 0,
                                        color: isDark ? "#dcfce7" : "#064e3b",
                                        background: isDark
                                            ? "linear-gradient(135deg, rgba(34, 197, 94, 0.24) 0%, rgba(14, 165, 233, 0.18) 100%)"
                                            : "linear-gradient(135deg, #dcfce7 0%, #e0f2fe 100%)",
                                        border: "1px solid",
                                        borderColor: isDark ? "rgba(134, 239, 172, 0.32)" : "rgba(22, 163, 74, 0.28)",
                                        boxShadow: isDark ? "0 12px 24px rgba(0, 0, 0, 0.18)" : "0 12px 24px rgba(22, 163, 74, 0.14)",
                                        transition: "background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease",
                                        "&:hover": {
                                            background: isDark
                                                ? "linear-gradient(135deg, rgba(34, 197, 94, 0.32) 0%, rgba(14, 165, 233, 0.24) 100%)"
                                                : "linear-gradient(135deg, #bbf7d0 0%, #bae6fd 100%)",
                                            borderColor: isDark ? "rgba(134, 239, 172, 0.44)" : "rgba(22, 163, 74, 0.42)",
                                            boxShadow: isDark ? "0 14px 28px rgba(0, 0, 0, 0.26)" : "0 14px 28px rgba(22, 163, 74, 0.22)",
                                            transform: "translateY(-1px)"
                                        },
                                        "&:active": {
                                            transform: "translateY(0)"
                                        }
                                    }}
                                >
                                    {t("patientHub.joinWithCode")}
                                </Button>
                            )}

                        {!isPaciente && myLink && (

                            <Button
                                variant="contained"
                                color="error"
                                onClick={() => {

                                    setIsLeavingGroup(true);

                                    setSelectedLinkId(
                                        myLink.id
                                    );

                                    setOpenRemoveDialog(
                                        true
                                    );
                                }}
                                sx={{
                                    borderRadius: 2,
                                    textTransform: "none",
                                    fontWeight: 800,
                                    px: 2.2,
                                    py: 1.05,
                                    minWidth: 0,
                                    whiteSpace: "normal",
                                    width: { xs: "100%", sm: "auto" },
                                    color: isDark ? "#fecaca" : "#7f1d1d",
                                    background: isDark
                                        ? "rgba(248, 113, 113, 0.16)"
                                        : "rgba(254, 226, 226, 0.95)",
                                    border: "1px solid",
                                    borderColor: isDark ? "rgba(248, 113, 113, 0.3)" : "rgba(239, 68, 68, 0.28)",
                                    boxShadow: isDark ? "0 12px 24px rgba(0, 0, 0, 0.16)" : "0 12px 24px rgba(239, 68, 68, 0.12)",
                                    transition: "background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease",
                                    "&:hover": {
                                        background: isDark
                                            ? "rgba(248, 113, 113, 0.24)"
                                            : "rgba(254, 202, 202, 0.95)",
                                        borderColor: isDark ? "rgba(248, 113, 113, 0.42)" : "rgba(239, 68, 68, 0.4)",
                                        boxShadow: isDark ? "0 14px 28px rgba(0, 0, 0, 0.24)" : "0 14px 28px rgba(239, 68, 68, 0.18)",
                                        transform: "translateY(-1px)"
                                    },
                                    "&:active": {
                                        transform: "translateY(0)"
                                    }
                                }}
                            >
                                {t("patientHub.leaveGroup")}
                            </Button>
                        )}


                    </Box>

                </Box>

            </Box>

            <Box
                display="flex"
                flexDirection="column"
                gap={2}
                sx={{
                    minWidth: 0
                }}
            >
                {loading ? (
                    <Box
                        display="flex"
                        flexDirection="column"
                        gap={2}
                    >

                        {[1, 2, 3].map((item) => (

                            <Box
                                key={item}
                                sx={{
                                    ...panelSx,
                                    padding: 3,
                                }}
                            >

                                <Box
                                    display="flex"
                                    alignItems="center"
                                    gap={2}
                                >

                                    <Skeleton
                                        variant="rounded"
                                        width={60}
                                        height={60}
                                        sx={{
                                            borderRadius: "18px"
                                        }}
                                    />

                                    <Box
                                        width="100%"
                                        sx={{
                                            minWidth: 0
                                        }}
                                    >

                                        <Skeleton
                                            width="40%"
                                            height={30}
                                        />

                                        <Skeleton
                                            width={90}
                                            height={30}
                                        />

                                        <Skeleton
                                            width="60%"
                                        />

                                    </Box>

                                </Box>

                            </Box>

                        ))}

                    </Box>

                ) : links.length === 0 ? (

                    <Box
                        sx={{
                            ...panelSx,
                            padding: { xs: 3, sm: 4, md: 6 },
                            textAlign: "center",
                        }}
                    >

                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 600,
                                mb: 1,
                                ...textWrapSx
                            }}
                        >
                            {t("patientHub.noLinksTitle")}
                        </Typography>

                        <Typography
                            sx={{
                                color: "text.secondary",
                                mb: 3,
                                ...textWrapSx
                            }}
                        >
                            {t("patientHub.noLinksDescription")}
                        </Typography>

                    </Box>

                ) : (

                    <>
                        <Box
                        sx={{
                            ...panelSx,
                            padding: { xs: 2, sm: 3, md: 4 },
                            mb: 5
                        }}
                        >
                            {pacientes.length > 0 && (

                                <Box mb={6}>

                                    <Typography
                                        variant="h6"
                                        sx={{
                                            fontWeight: 700,
                                            mb: 2,
                                            color: "text.primary",
                                            ...textWrapSx
                                        }}
                                    >
                                        {t("patientHub.patientSection")}
                                    </Typography>

                                    <Box
                                        display="flex"
                                        flexDirection="column"
                                        gap={2}
                                    >

                                        {pacientes.map((link) => {

                                            const typeStyle =
                                                getTypeColor(link.tipo);

                                            return (

                                                <MemberCard
                                                    link={link}
                                                    typeStyle={typeStyle}
                                                    highlight
                                                    hideRemove
                                                />
                                            );
                                        })}

                                    </Box>

                                </Box>
                            )}

                            {responsaveis.length > 0 && (

                                <Box mb={4}>

                                    <Typography
                                        variant="h6"
                                        sx={{
                                            fontWeight: 700,
                                            mb: 2,
                                            color: "text.primary",
                                            ...textWrapSx
                                        }}
                                    >
                                        {t("patientHub.guardiansSection")}
                                    </Typography>

                                    <Box
                                        display="flex"
                                        flexDirection="column"
                                        gap={2}
                                    >

                                        {responsaveis.map((link) => {

                                            const typeStyle =
                                                getTypeColor(link.tipo, link.funcao);

                                            return (

                                                <MemberCard
                                                    link={link}
                                                    typeStyle={typeStyle}
                                                    onRemove={
                                                        canRemove(link.tipo)
                                                            ? () => {

                                                                setSelectedLinkId(link.id);
                                                                setIsLeavingGroup(false);
                                                                setOpenRemoveDialog(true);
                                                            }
                                                            : null
                                                    }
                                                />
                                            );
                                        })}

                                    </Box>

                                </Box>
                            )}

                            {medicos.length > 0 && (

                                <Box mb={4}>

                                    <Typography
                                        variant="h6"
                                        sx={{
                                            fontWeight: 700,
                                            mb: 2,
                                            color: "text.primary",
                                            ...textWrapSx
                                        }}
                                    >
                                        {t("patientHub.doctorsSection")}
                                    </Typography>

                                    <Box
                                        display="flex"
                                        flexDirection="column"
                                        gap={2}
                                    >

                                        {medicos.map((link) => {

                                            const typeStyle =
                                                getTypeColor(
                                                    link.tipo,
                                                    link.funcao
                                                );

                                            return (

                                                <MemberCard
                                                    link={link}
                                                    typeStyle={typeStyle}
                                                    onRemove={
                                                        canRemove(link.tipo)
                                                            ? () => {

                                                                setSelectedLinkId(link.id);
                                                                setIsLeavingGroup(false);
                                                                setOpenRemoveDialog(true);
                                                            }
                                                            : null
                                                    }
                                                />
                                            );
                                        })}

                                    </Box>

                                </Box>
                            )}
                        </Box>

                    </>
                )}

                {selectedPatient &&
                    <Box
                        mt={6}
                        sx={{
                            minWidth: 0
                        }}
                    >

                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 800,
                                color: "text.primary",
                                mb: 3,
                                ...textWrapSx
                            }}
                        >
                            {t("patientHub.modules")}
                        </Typography>

                        <Box
                            display="grid"
                            gridTemplateColumns={{
                                xs: "minmax(0, 1fr)",
                                sm: "repeat(2, minmax(0, 1fr))",
                                lg: "repeat(3, minmax(0, 1fr))"
                            }}
                            gap={3}
                            sx={{
                                minWidth: 0
                            }}
                        >

                            <Box
                                onClick={() => navigate("/health-tracker")}
                                sx={moduleCardSx}
                            >

                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 1,
                                        ...textWrapSx
                                    }}
                                >
                                    {t("nav.records")}
                                </Typography>

                                <Typography
                                    sx={supportingTextSx}
                                >
                                    {t("patientHub.recordsDescription")}
                                </Typography>

                            </Box>

                            <Box
                                onClick={() => handleOpenPerfil()}
                                sx={moduleCardSx}
                            >

                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 1,
                                        ...textWrapSx
                                    }}
                                >
                                    {t("nav.information")}
                                </Typography>

                                <Typography
                                    sx={supportingTextSx}
                                >
                                    {t("patientHub.informationDescription")}
                                </Typography>

                            </Box>

                            <Box
                                onClick={() => navigate("/documents")}
                                sx={moduleCardSx}
                            >

                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 1,
                                        ...textWrapSx
                                    }}
                                >
                                    {t("nav.documents")}
                                </Typography>

                                <Typography
                                    sx={supportingTextSx}
                                >
                                    {t("patientHub.documentsDescription")}
                                </Typography>

                            </Box>

                            <Box
                                onClick={() => navigate("/dashboard")}
                                sx={moduleCardSx}
                            >

                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 1,
                                        ...textWrapSx
                                    }}
                                >
                                    {t("patientHub.dashboard")}
                                </Typography>

                                <Typography
                                    sx={supportingTextSx}
                                >
                                    {t("patientHub.dashboardDescription")}
                                </Typography>

                            </Box>

                            <Box
                                onClick={() => navigate("/goals")}
                                sx={moduleCardSx}
                            >

                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 1,
                                        ...textWrapSx
                                    }}
                                >
                                    {t("nav.goals")}
                                </Typography>

                                <Typography
                                    sx={supportingTextSx}
                                >
                                    {t("patientHub.goalsDescription")}
                                </Typography>

                            </Box>

                            <Box
                                onClick={() => navigate("/activity")}
                                sx={moduleCardSx}
                            >

                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 1,
                                        ...textWrapSx
                                    }}
                                >
                                    {t("nav.activity")}
                                </Typography>

                                <Typography
                                    sx={supportingTextSx}
                                >
                                    {t("patientHub.activityDescription")}
                                </Typography>

                            </Box>

                        </Box>
                    </Box>
                }

            </Box>

            <DialogUI
                open={openModal}
                onClose={() => setOpenModal(false)}
                title={t("patientHub.generatedTitle")}
                disabledConfirm
            >
                <Tabs
                    value={selectedTab}
                    onChange={(event, newValue) =>
                        setSelectedTab(newValue)
                    }
                    sx={{
                        mb: 3
                    }}
                >

                    <Tab label={t("patientHub.codeTab")} />

                    <Tab label={t("patientHub.emailTab")} />

                </Tabs>

                <Stack spacing={3} mt={1}>

                    {selectedTab === 0 && (
                        <Box>
                            <Box>

                                <Typography
                                    variant="body2"
                                    sx={{
                                        mb: 1,
                                        color: "text.secondary"
                                    }}
                                >
                                    {t("patientHub.codeTab")}
                                </Typography>

                                <Box
                                    sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "space-between",
                                        gap: 1,
                                        minWidth: 0,

                                        backgroundColor: isDark ? "rgba(220, 252, 231, 0.08)" : "#f1f1f1",
                                        border: "1px solid",
                                        borderColor: vitta.border,

                                        borderRadius: "16px",

                                        padding: "12px 16px"
                                    }}
                                >

                                    <Typography
                                        sx={{
                                            fontWeight: 600,
                                            letterSpacing: 1,
                                            ...textWrapSx
                                        }}
                                    >
                                        {generatedCode}
                                    </Typography>

                                    <IconButton
                                        onClick={() =>
                                            handleCopy(generatedCode)
                                        }
                                    >
                                        <ContentCopyIcon />
                                    </IconButton>

                                </Box>

                            </Box>


                            <Box>

                                <Typography
                                    variant="body2"
                                    sx={{
                                        mb: 1,
                                        color: "text.secondary"
                                    }}
                                >
                                    Link
                                </Typography>

                                <Box
                                    sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "space-between",
                                        gap: 1,
                                        minWidth: 0,

                                        backgroundColor: isDark ? "rgba(220, 252, 231, 0.08)" : "#f1f1f1",
                                        border: "1px solid",
                                        borderColor: vitta.border,

                                        borderRadius: "16px",

                                        padding: "12px 16px"
                                    }}
                                >

                                    <Typography
                                        sx={{
                                            overflow: "hidden",
                                            textOverflow: "ellipsis",
                                            whiteSpace: "nowrap",
                                            minWidth: 0,
                                            flex: 1
                                        }}
                                    >
                                        {generatedLink}
                                    </Typography>

                                    <IconButton
                                        onClick={() =>
                                            handleCopy(generatedLink)
                                        }
                                    >
                                        <ContentCopyIcon />
                                    </IconButton>

                                </Box>

                            </Box>
                        </Box>
                    )}

                    {selectedTab === 1 && (
                        <Box mt={4}>

                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 600,
                                    mb: 2
                                }}
                            >
                                {t("patientHub.sendInviteByEmail")}
                            </Typography>

                            <Box
                                display="flex"
                                flexDirection={{ xs: "column", sm: "row" }}
                                gap={2}
                                mb={2}
                            >

                                <TextField
                                    label={t("patientHub.email")}
                                    value={emailInput}
                                    onChange={(e) =>
                                        setEmailInput(e.target.value)
                                    }
                                    fullWidth
                                    size="small"
                                />

                                <Button
                                    variant="contained"
                                    onClick={handleAddEmail}
                                    sx={{
                                        minWidth: "56px",
                                        width: { xs: "100%", sm: "auto" },
                                        borderRadius: "14px"
                                    }}
                                >
                                    <AddIcon />
                                </Button>

                            </Box>

                            <Stack
                                direction="row"
                                flexWrap="wrap"
                                gap={1}
                            >

                                {emails.map((email) => (
                                    <Chip
                                        key={email}
                                        label={email}
                                        onDelete={() =>
                                            handleRemoveEmail(email)
                                        }
                                        deleteIcon={<CloseIcon />}
                                        sx={{
                                            borderRadius: "10px",
                                            maxWidth: "100%",
                                            "& .MuiChip-label": {
                                                overflow: "hidden",
                                                textOverflow: "ellipsis"
                                            }
                                        }}
                                    />
                                ))}

                            </Stack>

                            {emails.length > 0 && (

                                <ButtonUI
                                    variant="contained"
                                    onClick={handleSendEmails}
                                    disabled={sendingEmails}
                                    sx={{
                                        mt: 3,
                                        borderRadius: "14px",
                                        textTransform: "none",
                                        fontWeight: 600,
                                        width: { xs: "100%", sm: "auto" }
                                    }}
                                >

                                    {
                                        sendingEmails
                                            ? t("patientHub.sendingInvites")
                                            : t("patientHub.sendInvites")
                                    }

                                </ButtonUI>

                            )}

                        </Box>
                    )}
                </Stack>

            </DialogUI>

            <DialogUI
                open={openJoinModal}
                onClose={() => setOpenJoinModal(false)}
                title={t("patientHub.joinWithCode")}
                onConfirm={handleJoinWithCode}
                confirmText={t("nav.enter")}
            >

                {precisaEscolherFuncao && (<AutocompleteUI
                    label={t("patientHub.roleInGroup")}
                    options={opcoesFuncao}
                    error={errorFuncao}
                    value={
                        opcoesFuncao.find(
                            (option) =>
                                option.value === funcao
                        ) || null
                    }
                    onChange={(newValue) => {
                        setFuncao(newValue?.value || "");
                        setErrorFuncao(false);

                    }}
                    renderOption={(props, option) => (
                        <li {...props}>
                            <Box>
                                <Typography
                                    sx={{
                                        fontWeight: 600,
                                        ...textWrapSx
                                    }}
                                >
                                    {option.label}
                                </Typography>

                                <Typography
                                    variant="body2"
                                    sx={{
                                        color: "text.secondary",
                                        ...textWrapSx
                                    }}
                                >
                                    {option.descricao}
                                </Typography>
                            </Box>
                        </li>
                    )}
                />)}

                <InputUI
                    label={t("patientHub.invitationCode")}
                    value={joinCode}
                    onChange={(e) =>
                        setJoinCode(e.target.value)
                    }
                />

            </DialogUI>

            <DialogUI
                open={openRemoveDialog}
                onClose={() => {

                    setOpenRemoveDialog(false);

                    setSelectedLinkId(null);

                    setIsLeavingGroup(false);
                }}

                title={
                    isLeavingGroup
                        ? t("patientHub.leaveGroup")
                        : t("patientHub.removeLink")
                }

                onConfirm={confirmRemoveLink}

                confirmText={
                    isLeavingGroup
                        ? t("patientHub.leaveGroup")
                        : t("common.remove")
                }

                cancelText={t("common.cancel")}
            >

                <Typography>

                    {
                        isLeavingGroup
                            ? t("patientHub.leaveQuestion")
                            : t("patientHub.removeQuestion")
                    }

                </Typography>

            </DialogUI>

        </Box>
    );
}
