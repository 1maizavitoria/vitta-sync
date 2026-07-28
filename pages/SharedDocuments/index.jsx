import { useEffect, useState } from "react";

import {
    Avatar,
    Box,
    Dialog,
    DialogContent,
    Grid,
    IconButton,
    Paper,
    Tooltip,
    Typography
} from "@mui/material";
import { useTheme } from "@mui/material/styles";

import CloseIcon from "@mui/icons-material/Close";
import DescriptionIcon from "@mui/icons-material/Description";
import DownloadIcon from "@mui/icons-material/Download";
import ImageIcon from "@mui/icons-material/Image";
import OpenInNewIcon from "@mui/icons-material/OpenInNew";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";

import { usePatient } from "../../context/PatientContext";
import { useI18n } from "../../src/i18n";
import {
    downloadDocument,
    getPatientDocuments,
    viewDocument
} from "../../services/documentService";

function getInitials(name) {
    if (!name) return "";

    const parts = name.trim().split(" ").filter(Boolean);

    if (parts.length === 1) {
        return parts[0][0].toUpperCase();
    }

    return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
}

function getFileType(extension) {
    if (!extension) return "unknown";
    return extension.replace(".", "").toLowerCase();
}

function renderFileIcon(extension) {
    const type = getFileType(extension);

    if (type === "pdf") {
        return <PictureAsPdfIcon />;
    }

    if (["png", "jpg", "jpeg"].includes(type)) {
        return <ImageIcon />;
    }

    return <DescriptionIcon />;
}

export default function SharedDocuments() {
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";
    const { t } = useI18n();
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [openViewer, setOpenViewer] = useState(false);
    const [selectedDoc, setSelectedDoc] = useState(null);
    const [viewerUrl, setViewerUrl] = useState("");

    const { selectedPatient } = usePatient();
    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const isPatientUser = userType === "paciente";

    const subtitle = isPatientUser
        ? t("documents.shared.patientDescription")
        : t("documents.shared.linkedPatientDescription");

    const emptyDescription = isPatientUser
        ? t("documents.shared.patientEmptyDescription")
        : t("documents.shared.linkedPatientEmptyDescription");

    function handleCloseViewer() {
        if (viewerUrl) {
            URL.revokeObjectURL(viewerUrl);
        }

        setViewerUrl("");
        setSelectedDoc(null);
        setOpenViewer(false);
    }

    async function handleOpen(doc) {
        try {
            const url = await viewDocument(doc.id, doc.extensao);

            setViewerUrl(url);
            setSelectedDoc(doc);
            setOpenViewer(true);
        } catch (error) {
            console.error("Erro ao visualizar documento:", error);
        }
    }

    useEffect(() => {
        if (!selectedPatient) return;

        async function load() {
            try {
                setLoading(true);
                const data = await getPatientDocuments(selectedPatient.cpf);
                setDocuments(data);
            } catch (error) {
                console.error("Erro ao carregar documentos:", error);
                setDocuments([]);
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [selectedPatient]);

    useEffect(() => {
        return () => {
            if (viewerUrl) {
                URL.revokeObjectURL(viewerUrl);
            }
        };
    }, [viewerUrl]);

    return (
        <Box sx={{ minWidth: 0 }}>
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
                        {t("documents.shared.title")}
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

            {loading && (
                <Typography color="text.secondary">
                    {t("documents.shared.loading")}
                </Typography>
            )}

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
                        {t("documents.shared.noFiles")}
                    </Typography>

                    <Typography color="text.secondary">
                        {emptyDescription}
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
                                    {getInitials(doc.medicoNome)}
                                </Avatar>

                                <Box flex={1} sx={{ minWidth: 0 }}>
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
                                        {doc.medicoNome}
                                    </Typography>

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
                                                bgcolor: isDark ? "rgba(34, 197, 94, 0.12)" : "rgba(22, 163, 74, 0.1)",
                                                color: "primary.main",
                                                display: "flex",
                                                alignItems: "center",
                                                justifyContent: "center",
                                                flex: "0 0 auto"
                                            }}
                                        >
                                            {renderFileIcon(doc.extensao)}
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
                                            {doc.nomeOriginal || t("documents.shared.oldFile")}
                                        </Typography>

                                        <Tooltip title={t("documents.shared.view")}>
                                            <IconButton
                                                onClick={() => handleOpen(doc)}
                                                size="small"
                                                sx={{
                                                    color: "secondary.main",
                                                    bgcolor: isDark ? "rgba(14, 165, 233, 0.12)" : "rgba(15, 118, 110, 0.08)",
                                                    border: "1px solid",
                                                    borderColor: isDark ? "rgba(14, 165, 233, 0.22)" : "rgba(15, 118, 110, 0.16)"
                                                }}
                                            >
                                                <OpenInNewIcon fontSize="small" />
                                            </IconButton>
                                        </Tooltip>

                                        <Tooltip title={t("documents.shared.download")}>
                                            <IconButton
                                                onClick={() =>
                                                    downloadDocument(doc.id, doc.nomeOriginal)
                                                }
                                                size="small"
                                                sx={{
                                                    color: "primary.main",
                                                    bgcolor: isDark ? "rgba(34, 197, 94, 0.12)" : "rgba(22, 163, 74, 0.08)",
                                                    border: "1px solid",
                                                    borderColor: vitta.borderStrong
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

            <Dialog
                open={openViewer}
                onClose={handleCloseViewer}
                maxWidth="lg"
                fullWidth
            >
                <DialogContent
                    sx={{
                        p: 0,
                        height: "90vh",
                        position: "relative",
                        bgcolor: "background.default"
                    }}
                >
                    <IconButton
                        onClick={handleCloseViewer}
                        sx={{
                            position: "absolute",
                            right: 12,
                            top: 12,
                            zIndex: 10,
                            bgcolor: "background.paper",
                            border: "1px solid",
                            borderColor: "divider",
                            boxShadow: vitta.shadow,

                            "&:hover": {
                                bgcolor: isDark ? "rgba(220, 252, 231, 0.08)" : "#f5f5f5"
                            }
                        }}
                    >
                        <CloseIcon />
                    </IconButton>

                    {selectedDoc && getFileType(selectedDoc.extensao) === "pdf" ? (
                        <object
                            data={viewerUrl}
                            type="application/pdf"
                            width="100%"
                            height="100%"
                        >
                            <Typography p={4}>
                                {t("documents.shared.pdfUnavailable")}
                            </Typography>
                        </object>
                    ) : (
                        <Box
                            display="flex"
                            justifyContent="center"
                            alignItems="center"
                            height="100%"
                            p={2}
                        >
                            {viewerUrl && (
                                <img
                                    src={viewerUrl}
                                    alt={t("documents.shared.imageAlt")}
                                    style={{
                                        maxWidth: "100%",
                                        maxHeight: "100%",
                                        objectFit: "contain"
                                    }}
                                />
                            )}
                        </Box>
                    )}
                </DialogContent>
            </Dialog>
        </Box>
    );
}
