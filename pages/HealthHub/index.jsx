import { useEffect, useRef, useState } from "react";

import {
    Avatar,
    Box,
    Button,
    Grid,
    IconButton,
    Paper,
    Tooltip,
    Typography
} from "@mui/material";
import { useTheme } from "@mui/material/styles";

import CloseIcon from "@mui/icons-material/Close";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import DownloadIcon from "@mui/icons-material/Download";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import UploadFileIcon from "@mui/icons-material/UploadFile";

import InputUI from "../../components/ui/Input";
import { usePatient } from "../../context/PatientContext";
import { useI18n } from "../../src/i18n";
import {
    deleteDocument,
    downloadDocument,
    getDoctorDocuments,
    uploadDocument
} from "../../services/documentService";

function getInitials(name) {
    if (!name) return "";

    const parts = name.trim().split(" ").filter(Boolean);

    if (parts.length === 1) {
        return parts[0][0].toUpperCase();
    }

    return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
}

export default function HealthHub() {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();
    const [dragging, setDragging] = useState(false);
    const [documents, setDocuments] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);
    const [nomeArquivo, setNomeArquivo] = useState("");
    const [loading, setLoading] = useState(false);

    const cpfUsuario =
        localStorage.getItem("cpf") ||
        localStorage.getItem("CPF");

    const { selectedPatient } = usePatient();
    const hasPatientSelected =
        selectedPatient &&
        selectedPatient.cpf !== cpfUsuario;

    const fileInputRef = useRef(null);

    function resetUpload() {
        setSelectedFile(null);
        setNomeArquivo("");

        if (fileInputRef.current) {
            fileInputRef.current.value = "";
        }
    }

    function handleFile(file) {
        if (!file) return;

        if (file.type === "application/pdf") {
            setSelectedFile(file);
            return;
        }

        alert(t("documents.upload.onlyPdf"));
    }

    async function loadDocuments() {
        try {
            setLoading(true);
            const data = await getDoctorDocuments();
            setDocuments(data);
        } catch (error) {
            console.error("Erro ao carregar documentos:", error);
        } finally {
            setLoading(false);
        }
    }

    async function handleUpload() {
        if (!selectedPatient || !selectedFile || !nomeArquivo.trim()) {
            return;
        }

        try {
            setLoading(true);

            const formData = new FormData();
            formData.append("arquivo", selectedFile);
            formData.append("nomeArquivo", nomeArquivo.trim());

            await uploadDocument(selectedPatient.cpf, formData);
            await loadDocuments();
            resetUpload();
        } catch (error) {
            console.error("Erro ao enviar documento:", error);
        } finally {
            setLoading(false);
        }
    }

    async function handleDelete(id) {
        try {
            await deleteDocument(id);
            setDocuments((prev) => prev.filter((item) => item.id !== id));
            resetUpload();
        } catch (error) {
            console.error("Erro ao remover documento:", error);
        }
    }

    useEffect(() => {
        loadDocuments();
    }, []);

    return (
        <Box sx={{ minWidth: 0 }}>
            <Box
                display="flex"
                flexDirection={{ xs: "column", md: "row" }}
                justifyContent="space-between"
                alignItems={{ xs: "flex-start", md: "center" }}
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
                        {t("documents.upload.title")}
                    </Typography>

                    <Typography
                        sx={{
                            color: "text.secondary",
                            fontSize: "0.9rem",
                            mt: 0.25,
                            overflowWrap: "anywhere"
                        }}
                    >
                        {t("documents.upload.description")}
                    </Typography>
                </Box>

                <Tooltip
                    title={
                        !hasPatientSelected
                            ? t("documents.upload.selectPatient")
                            : !selectedFile || !nomeArquivo.trim()
                                ? t("documents.upload.fillNameAndPdf")
                                : t("documents.upload.upload")
                    }
                >
                    <span>
                        <IconButton
                            onClick={handleUpload}
                            disabled={
                                loading ||
                                !hasPatientSelected ||
                                !selectedFile ||
                                !nomeArquivo.trim()
                            }
                            sx={{
                                color: "#ffffff",
                                bgcolor: "primary.main",
                                border: "1px solid",
                                borderColor: vitta.borderStrong,
                                "&:hover": { bgcolor: "primary.dark" },
                                "&.Mui-disabled": {
                                    bgcolor: isDark ? "rgba(220, 252, 231, 0.08)" : "#d7e4e1",
                                    color: "text.secondary"
                                }
                            }}
                        >
                            <UploadFileIcon />
                        </IconButton>
                    </span>
                </Tooltip>
            </Box>

            <Paper
                elevation={0}
                sx={{
                    p: { xs: 2.5, md: 4 },
                    borderRadius: 3,
                    border: dragging
                        ? "2px dashed"
                        : "2px dashed",
                    borderColor: dragging ? "primary.main" : vitta.borderStrong,
                    mb: 3,
                    textAlign: "center",
                    backgroundColor: dragging
                        ? isDark ? "rgba(34, 197, 94, 0.14)" : "rgba(220, 252, 231, 0.72)"
                        : isDark ? "rgba(220, 252, 231, 0.06)" : "rgba(240, 253, 244, 0.42)",
                    transition: ".2s",
                    minWidth: 0
                }}
                onDragOver={(event) => {
                    event.preventDefault();
                    setDragging(true);
                }}
                onDragLeave={() => setDragging(false)}
                onDrop={(event) => {
                    event.preventDefault();
                    setDragging(false);
                    handleFile(event.dataTransfer.files[0]);
                }}
            >
                <Box display="flex" alignItems="center" flexDirection="column" gap={2.5}>
                    <Box
                        sx={{
                            width: 58,
                            height: 58,
                            borderRadius: 3,
                            background: isDark
                                ? "linear-gradient(135deg, rgba(34, 197, 94, 0.18) 0%, rgba(14, 165, 233, 0.14) 100%)"
                                : "linear-gradient(135deg, #dcfce7 0%, #e0f2fe 100%)",
                            color: isDark ? "#bbf7d0" : "#166534",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center"
                        }}
                    >
                        <UploadFileIcon sx={{ fontSize: 32 }} />
                    </Box>

                    <Box>
                        <Typography
                            sx={{
                                fontWeight: 800,
                                color: "text.primary"
                            }}
                        >
                            {dragging ? t("documents.upload.dropPdf") : t("documents.upload.dragPdf")}
                        </Typography>

                        <Typography
                            sx={{
                                color: "text.secondary",
                                fontSize: "0.9rem",
                                mt: 0.5
                            }}
                        >
                            {t("documents.upload.manualSelect")}
                        </Typography>
                    </Box>

                    <InputUI
                        label={t("documents.upload.documentName")}
                        value={nomeArquivo}
                        onChange={(event) => setNomeArquivo(event.target.value)}
                        sx={{ maxWidth: 360 }}
                        disabled={!hasPatientSelected}
                    />

                    <Button
                        variant="outlined"
                        component="label"
                        disabled={!hasPatientSelected}
                        sx={{
                            borderRadius: 2,
                            textTransform: "none",
                            fontWeight: 800,
                            borderColor: vitta.borderStrong,
                            color: "primary.dark"
                        }}
                    >
                        {t("documents.upload.selectPdf")}
                        <input
                            ref={fileInputRef}
                            hidden
                            type="file"
                            accept="application/pdf"
                            onChange={(event) => handleFile(event.target.files[0])}
                        />
                    </Button>

                    {selectedFile && (
                        <Box
                            display="flex"
                            alignItems="center"
                            gap={1}
                            sx={{
                                maxWidth: "100%",
                                px: 1.5,
                                py: 1,
                                borderRadius: 2,
                                bgcolor: "background.paper",
                                border: "1px solid",
                                borderColor: vitta.border
                            }}
                        >
                            <PictureAsPdfIcon color="error" fontSize="small" />

                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{
                                    minWidth: 0,
                                    overflow: "hidden",
                                    textOverflow: "ellipsis",
                                    whiteSpace: "nowrap"
                                }}
                            >
                                {selectedFile.name}
                            </Typography>

                            <Tooltip title={t("documents.upload.removeFile")}>
                                <IconButton size="small" onClick={resetUpload}>
                                    <CloseIcon fontSize="small" />
                                </IconButton>
                            </Tooltip>
                        </Box>
                    )}
                </Box>
            </Paper>

            <Box
                display="flex"
                alignItems="center"
                justifyContent="space-between"
                gap={2}
                mb={2}
            >
                <Typography
                    sx={{
                        fontWeight: 800,
                        color: "text.primary",
                        fontSize: "1.05rem"
                    }}
                >
                    {t("documents.upload.sentDocuments")}
                </Typography>

                {loading && (
                    <Typography color="text.secondary" fontSize="0.9rem">
                        {t("documents.upload.updating")}
                    </Typography>
                )}
            </Box>

            {!loading && documents.length === 0 && (
                <Box
                    sx={{
                        width: "100%",
                        py: 5,
                        px: 2,
                        textAlign: "center",
                        borderRadius: 3,
                        border: "1px dashed",
                        borderColor: vitta.borderStrong,
                        bgcolor: isDark ? "rgba(220, 252, 231, 0.06)" : "rgba(240, 253, 244, 0.42)"
                    }}
                >
                    <Typography
                        sx={{
                            fontWeight: 800,
                            color: "text.primary",
                            mb: 1
                        }}
                    >
                        {t("documents.upload.noSentDocuments")}
                    </Typography>

                    <Typography color="text.secondary">
                        {t("documents.upload.sentDocumentsDescription")}
                    </Typography>
                </Box>
            )}

            <Grid container spacing={2.5}>
                {documents.map((doc) => (
                    <Grid item xs={12} md={6} key={doc.id}>
                        <Paper
                            elevation={0}
                            sx={{
                                p: 2.5,
                                borderRadius: 3,
                                border: "1px solid",
                                borderColor: vitta.border,
                                boxShadow: vitta.shadow,
                                bgcolor: "background.paper",
                                height: "100%",
                                minWidth: 0
                            }}
                        >
                            <Box display="flex" gap={2} sx={{ minWidth: 0 }}>
                                <Avatar
                                    sx={{
                                        bgcolor: "primary.main",
                                        color: "#ffffff",
                                        width: 46,
                                        height: 46,
                                        fontWeight: 800,
                                        boxShadow: isDark
                                            ? "0 10px 20px rgba(0, 0, 0, 0.24)"
                                            : "0 10px 20px rgba(22, 163, 74, 0.22)",
                                        flex: "0 0 auto"
                                    }}
                                >
                                    {getInitials(doc.pacienteNome)}
                                </Avatar>

                                <Box flex={1} sx={{ minWidth: 0 }}>
                                    <Box
                                        display="flex"
                                        justifyContent="space-between"
                                        alignItems="flex-start"
                                        gap={1.5}
                                        sx={{ minWidth: 0 }}
                                    >
                                        <Box sx={{ minWidth: 0 }}>
                                            <Typography
                                                sx={{
                                                    fontWeight: 800,
                                                    color: "text.primary",
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                    whiteSpace: "nowrap"
                                                }}
                                            >
                                                {doc.nomeArquivo}
                                            </Typography>

                                            <Typography
                                                sx={{
                                                    color: "text.secondary",
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                    whiteSpace: "nowrap"
                                                }}
                                            >
                                                {doc.pacienteNome || t("documents.upload.patientFallback")}
                                            </Typography>
                                        </Box>

                                        <Tooltip title={t("documents.upload.remove")}>
                                            <IconButton
                                                onClick={() => handleDelete(doc.id)}
                                                size="small"
                                                sx={{
                                            color: "#dc2626",
                                                    bgcolor: "rgba(220, 38, 38, 0.08)",
                                                    border: "1px solid rgba(220, 38, 38, 0.14)",
                                                    flex: "0 0 auto"
                                                }}
                                            >
                                                <DeleteOutlineIcon fontSize="small" />
                                            </IconButton>
                                        </Tooltip>
                                    </Box>

                                    <Box
                                        display="flex"
                                        alignItems="center"
                                        gap={1}
                                        mt={2}
                                        sx={{ minWidth: 0 }}
                                    >
                                        <Box
                                            sx={{
                                                width: 34,
                                                height: 34,
                                                borderRadius: 2,
                                                bgcolor: "rgba(220, 38, 38, 0.08)",
                                                color: "#dc2626",
                                                display: "flex",
                                                alignItems: "center",
                                                justifyContent: "center",
                                                flex: "0 0 auto"
                                            }}
                                        >
                                            <PictureAsPdfIcon fontSize="small" />
                                        </Box>

                                        <Typography
                                            sx={{
                                                minWidth: 0,
                                                flex: 1,
                                                overflow: "hidden",
                                                textOverflow: "ellipsis",
                                                whiteSpace: "nowrap",
                                                color: "text.primary",
                                                fontWeight: 700
                                            }}
                                        >
                                            {doc.nomeOriginal || t("documents.upload.oldFile")}
                                        </Typography>

                                        <Tooltip title={t("documents.upload.download")}>
                                            <IconButton
                                                onClick={() =>
                                                    downloadDocument(doc.id, doc.nomeOriginal)
                                                }
                                                size="small"
                                                sx={{
                                                    color: "primary.main",
                                                    bgcolor: isDark ? "rgba(34, 197, 94, 0.12)" : "rgba(22, 163, 74, 0.08)",
                                                    border: "1px solid",
                                                    borderColor: vitta.borderStrong,
                                                    flex: "0 0 auto"
                                                }}
                                            >
                                                <DownloadIcon fontSize="small" />
                                            </IconButton>
                                        </Tooltip>
                                    </Box>

                                    <Typography
                                        variant="caption"
                                        color="text.secondary"
                                        sx={{
                                            display: "block",
                                            mt: 1.25
                                        }}
                                    >
                                        {new Date(doc.dataUpload).toLocaleString("pt-BR")}
                                    </Typography>
                                </Box>
                            </Box>
                        </Paper>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
}
