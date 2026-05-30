import { Box, Typography, IconButton, Button, TextField, Stack, Chip, Skeleton, Tabs, Tab } from "@mui/material";

import { useEffect, useState } from "react";

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

import { funcoesGrupo, funcoesMedico } from "../../utils/validators/userFunction";

export default function PatientHub() {
    const { showAlert } = useAlert();

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
    const navigate = useNavigate();
    const [funcao, setFuncao] = useState("");

    const [errorFuncao, setErrorFuncao] = useState(false);

    const precisaEscolherFuncao =
        ["responsavel", "saude"]
            .includes(
                userType?.toLowerCase()
            );

    const opcoesFuncao =
        userType?.toLowerCase() === "saude"
            ? funcoesMedico
            : funcoesGrupo;

    function getResponsavelStyle(funcao) {

        switch (funcao?.toLowerCase()) {

            case "cuidador":
                return {
                    background: "#fff3e0",
                    color: "#e65100",
                    label: "Cuidador"
                };

            case "responsavel_legal":
                return {
                    background: "#fce4ec",
                    color: "#c2185b",
                    label: "Responsável Legal"
                };

            case "acompanhante":
                return {
                    background: "#ede7f6",
                    color: "#5e35b1",
                    label: "Acompanhante"
                };

            case "contato_emergencia":
                return {
                    background: "#ffebee",
                    color: "#c62828",
                    label: "Contato de Emergência"
                };

            case "tutor":
                return {
                    background: "#e0f7fa",
                    color: "#00838f",
                    label: "Tutor"
                };

            default:
                return {
                    background: "#eeeeee",
                    color: "#616161",
                    label: "Responsável"
                };
        }
    }

    function getMedicoStyle(funcao) {

        switch (funcao?.toLowerCase()) {

            case "medico_principal":
                return {
                    background: "#e3f2fd",
                    color: "#1565c0",
                    label: "Médico Principal"
                };

            case "especialista":
                return {
                    background: "#e8f5e9",
                    color: "#2e7d32",
                    label: "Especialista"
                };

            case "consultor":
                return {
                    background: "#fff8e1",
                    color: "#f9a825",
                    label: "Consultor"
                };

            case "acompanhamento_clinico":
                return {
                    background: "#e0f2f1",
                    color: "#00695c",
                    label: "Acompanhamento Clínico"
                };

            case "equipe_assistencial":
                return {
                    background: "#ede7f6",
                    color: "#4527a0",
                    label: "Equipe Assistencial"
                };

            default:
                return {
                    background: "#eeeeee",
                    color: "#616161",
                    label: "Profissional de Saúde"
                };
        }
    }

    async function loadLinks() {
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
                "Erro ao carregar vínculos"
            );

        } finally {

            setLoading(false);
        }
    }

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
                "Vínculo removido com sucesso"
            );

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Erro ao remover vínculo"
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

        try {

            const data = await generateLinkCode();
            setSelectedTab(0);
            setGeneratedCode(data.codigo);
            setGeneratedLink(data.link);
            setOpenModal(true);

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Erro ao gerar código"
            );
        }
    }

    async function handleCopy(text) {

        try {

            await navigator.clipboard.writeText(text);

            showAlert(
                "success",
                "Copiado com sucesso"
            );

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Erro ao copiar"
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
                "Selecione uma função no grupo"
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
                "Vínculo criado com sucesso"
            );

            setOpenJoinModal(false);

            setJoinCode("");

            setFuncao("");

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Código inválido"
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
                "Email já adicionado"
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
                "Adicione pelo menos um email"
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
                "Convites enviados com sucesso"
            );

            setEmails([]);

            setEmailInput("");

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Erro ao enviar convites"
            );

        } finally {

            setSendingEmails(false);
        }
    }

    async function handleOpenPerfil() {
        try {
            await refreshPatients();
            navigate("/reports")
        } catch (error) {
            console.error(error);
        }
    }

    function getTypeColor(type, funcao) {

        switch (type?.toLowerCase()) {

            case "paciente":
                return {
                    background: "#e3f2fd",
                    color: "#c6d219",
                    label: "Saúde"
                };

            case "saude":
                return getMedicoStyle(funcao);

            case "responsavel":

                return getResponsavelStyle(funcao);



            default:
                return {
                    background: "#eeeeee",
                    color: "#616161",
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

    }, [selectedPatient]);

    useEffect(() => {

        if (!selectedPatient?.id) {
            return;
        }

        loadLinks();

    }, [selectedPatient]);

    return (

        <Box
            p={4}
            sx={{
                minHeight: "100vh",
                backgroundColor: "#f8fafc"
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
                    <Box>

                        <Typography
                            variant="h4"
                            sx={{
                                fontWeight: 700
                            }}
                        >
                            Grupo de {selectedPatient?.nome || "Paciente"}
                        </Typography>

                        <Typography
                            sx={{
                                color: "#777"
                            }}
                        >
                            Pessoas que acompanham o paciente
                        </Typography>

                    </Box>}

                <Box
                    sx={{
                        backgroundColor: "#fff",
                        borderRadius: "24px",
                        padding: 4,
                        mb: 5,
                        boxShadow:
                            "0px 4px 20px rgba(0,0,0,0.06)"
                    }}
                >

                    <Typography
                        variant="h6"
                        sx={{
                            fontWeight: 700,
                            mb: 1
                        }}
                    >
                        Ações do grupo
                    </Typography>

                    <Typography
                        sx={{
                            color: "#777",
                            mb: 3
                        }}
                    >
                        Gerencie convites e participação no grupo.
                    </Typography>

                    <Box
                        display="flex"
                        flexWrap="wrap"
                        gap={2}
                    >

                        {userType?.toLowerCase() ===
                            "paciente" && (

                                <Button
                                    variant="contained"
                                    onClick={handleGenerateCode}
                                    sx={{
                                        borderRadius: "14px",
                                        textTransform: "none",
                                        fontWeight: 600
                                    }}
                                >
                                    Convidar participante
                                </Button>
                            )}

                        {userType?.toLowerCase() !==
                            "paciente" && (

                                <Button
                                    variant="outlined"
                                    onClick={() =>
                                        setOpenJoinModal(true)
                                    }
                                    sx={{
                                        borderRadius: "14px",
                                        textTransform: "none",
                                        fontWeight: 600
                                    }}
                                >
                                    Entrar com código
                                </Button>
                            )}

                        {!isPaciente && myLink && (

                            <Button
                                variant="outlined"
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
                                    borderRadius: "14px",
                                    textTransform: "none",
                                    fontWeight: 600
                                }}
                            >
                                Sair do grupo
                            </Button>
                        )}


                    </Box>

                </Box>

            </Box>

            <Box
                display="flex"
                flexDirection="column"
                gap={2}
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
                                    backgroundColor: "#fff",

                                    borderRadius: "24px",

                                    padding: 3,

                                    boxShadow:
                                        "0px 4px 15px rgba(0,0,0,0.08)"
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

                                    <Box width="100%">

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
                            backgroundColor: "#fff",

                            borderRadius: "24px",

                            padding: 6,

                            textAlign: "center",

                            boxShadow:
                                "0px 4px 15px rgba(0,0,0,0.08)"
                        }}
                    >

                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 600,
                                mb: 1
                            }}
                        >
                            Nenhum vínculo encontrado
                        </Typography>

                        <Typography
                            sx={{
                                color: "#777",
                                mb: 3
                            }}
                        >
                            Gere um código ou entre com um
                            código para criar vínculos.
                        </Typography>

                    </Box>

                ) : (

                    <>
                        <Box
                            sx={{
                                backgroundColor: "#ffffff",
                                borderRadius: "28px",
                                padding: 4,
                                boxShadow:
                                    "0px 4px 20px rgba(0,0,0,0.06)",
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
                                            color: "#444"
                                        }}
                                    >
                                        Paciente
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
                                            color: "#444"
                                        }}
                                    >
                                        Responsáveis
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
                                            color: "#444"
                                        }}
                                    >
                                        Médicos
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

                <Box mt={6}>

                    <Typography
                        variant="h5"
                        sx={{
                            fontWeight: 700,
                            mb: 3
                        }}
                    >
                        Módulos
                    </Typography>

                    <Box
                        display="grid"
                        gridTemplateColumns={{
                            xs: "1fr",
                            md: "repeat(3, 1fr)"
                        }}
                        gap={3}
                    >

                        <Box
                            onClick={() => navigate("/health-tracker")}
                            sx={{
                                backgroundColor: "#fff",
                                borderRadius: "24px",
                                padding: 4,
                                cursor: "pointer",
                                transition: ".2s",
                                boxShadow:
                                    "0px 4px 15px rgba(0,0,0,0.08)",

                                "&:hover": {
                                    transform: "translateY(-4px)"
                                }
                            }}
                        >

                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700,
                                    mb: 1
                                }}
                            >
                                Registros
                            </Typography>

                            <Typography
                                sx={{
                                    color: "#777"
                                }}
                            >
                                Hábitos, sintomas, sinais vitais e lembretes.
                            </Typography>

                        </Box>

                        <Box
                            onClick={() => handleOpenPerfil()}
                            sx={{
                                backgroundColor: "#fff",
                                borderRadius: "24px",
                                padding: 4,
                                cursor: "pointer",
                                transition: ".2s",
                                boxShadow:
                                    "0px 4px 15px rgba(0,0,0,0.08)",

                                "&:hover": {
                                    transform: "translateY(-4px)"
                                }
                            }}
                        >

                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700,
                                    mb: 1
                                }}
                            >
                                Informações
                            </Typography>

                            <Typography
                                sx={{
                                    color: "#777"
                                }}
                            >
                                Dados gerais e informações do paciente.
                            </Typography>

                        </Box>

                        <Box
                            onClick={() => navigate("/documents")}
                            sx={{
                                backgroundColor: "#fff",
                                borderRadius: "24px",
                                padding: 4,
                                cursor: "pointer",
                                transition: ".2s",
                                boxShadow:
                                    "0px 4px 15px rgba(0,0,0,0.08)",

                                "&:hover": {
                                    transform: "translateY(-4px)"
                                }
                            }}
                        >

                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700,
                                    mb: 1
                                }}
                            >
                                Documentos
                            </Typography>

                            <Typography
                                sx={{
                                    color: "#777"
                                }}
                            >
                                Documentação médica e arquivos relacionados.
                            </Typography>

                        </Box>

                        <Box
                            sx={{
                                backgroundColor: "#fff",
                                borderRadius: "24px",
                                padding: 4,
                                opacity: .6,
                                boxShadow:
                                    "0px 4px 15px rgba(0,0,0,0.08)"
                            }}
                        >

                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700,
                                    mb: 1
                                }}
                            >
                                Dashboard
                            </Typography>

                            <Typography
                                sx={{
                                    color: "#777"
                                }}
                            >
                                Visualizações e métricas futuras.
                            </Typography>

                        </Box>

                        <Box
                            onClick={() => navigate("/activity")}
                            sx={{
                                backgroundColor: "#fff",
                                borderRadius: "24px",
                                padding: 4,
                                cursor: "pointer",
                                transition: ".2s",
                                boxShadow:
                                    "0px 4px 15px rgba(0,0,0,0.08)",

                                "&:hover": {
                                    transform: "translateY(-4px)"
                                }
                            }}
                        >

                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700,
                                    mb: 1
                                }}
                            >
                                Atividade
                            </Typography>

                            <Typography
                                sx={{
                                    color: "#777"
                                }}
                            >
                                Histórico de atividades e notificações do grupo.
                            </Typography>

                        </Box>

                    </Box>
                </Box>
            </Box>

            <DialogUI
                open={openModal}
                onClose={() => setOpenModal(false)}
                title="Link e Código Gerados"
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

                    <Tab label="Código" />

                    <Tab label="Enviar por Email" />

                </Tabs>

                <Stack spacing={3} mt={1}>

                    {selectedTab === 0 && (
                        <Box>
                            <Box>

                                <Typography
                                    variant="body2"
                                    sx={{
                                        mb: 1,
                                        color: "#666"
                                    }}
                                >
                                    Código
                                </Typography>

                                <Box
                                    sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "space-between",

                                        backgroundColor: "#f1f1f1",

                                        borderRadius: "16px",

                                        padding: "12px 16px"
                                    }}
                                >

                                    <Typography
                                        sx={{
                                            fontWeight: 600,
                                            letterSpacing: 1
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
                                        color: "#666"
                                    }}
                                >
                                    Link
                                </Typography>

                                <Box
                                    sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "space-between",

                                        backgroundColor: "#f1f1f1",

                                        borderRadius: "16px",

                                        padding: "12px 16px"
                                    }}
                                >

                                    <Typography
                                        sx={{
                                            overflow: "hidden",
                                            textOverflow: "ellipsis",
                                            whiteSpace: "nowrap",
                                            maxWidth: "90%"
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
                                Enviar convite por email
                            </Typography>

                            <Box
                                display="flex"
                                gap={2}
                                mb={2}
                            >

                                <TextField
                                    label="Email"
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
                                            borderRadius: "10px"
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
                                        fontWeight: 600
                                    }}
                                >

                                    {
                                        sendingEmails
                                            ? "Enviando convites..."
                                            : "Enviar Convites"
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
                title="Entrar com código"
                onConfirm={handleJoinWithCode}
                confirmText="Entrar"
            >

                {precisaEscolherFuncao && (<AutocompleteUI
                    label="Função no grupo"
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
                                        fontWeight: 600
                                    }}
                                >
                                    {option.label}
                                </Typography>

                                <Typography
                                    variant="body2"
                                    sx={{
                                        color: "#777"
                                    }}
                                >
                                    {option.descricao}
                                </Typography>
                            </Box>
                        </li>
                    )}
                />)}

                <InputUI
                    label="Código de convite"
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
                        ? "Sair do grupo"
                        : "Remover vínculo"
                }

                onConfirm={confirmRemoveLink}

                confirmText={
                    isLeavingGroup
                        ? "Sair do grupo"
                        : "Remover"
                }

                cancelText="Cancelar"
            >

                <Typography>

                    {
                        isLeavingGroup
                            ? "Deseja sair deste grupo?"
                            : "Deseja remover este participante?"
                    }

                </Typography>

            </DialogUI>

        </Box>
    );
}