import {
    Avatar, Box, Grid, Paper, Typography, IconButton, Dialog,
    DialogContent
} from "@mui/material";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import DownloadIcon from "@mui/icons-material/Download";
import { useEffect, useState } from "react";
import { getPatientDocuments, downloadDocument } from "../../services/documentService";
import { usePatient } from "../../context/PatientContext";
import ImageIcon
    from "@mui/icons-material/Image";

import DescriptionIcon
    from "@mui/icons-material/Description";

import CloseIcon
    from "@mui/icons-material/Close";

import {
    viewDocument
}
    from "../../services/documentService";

export default function SharedDocuments() {
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(false);

    const [openViewer, setOpenViewer] = useState(false);
    const [selectedDoc, setSelectedDoc] = useState(null);
    const [viewerUrl, setViewerUrl] = useState("");

    const { selectedPatient } = usePatient();

    function getFileType(extensao) {
        if (!extensao) return "unknown";
        return extensao.replace(".", "").toLowerCase();
    }

    function renderFileIcon(name) {

        const type = getFileType(name);

        if (type === "pdf") {
            return <PictureAsPdfIcon color="error" />;
        }

        if (
            type === "png"
            || type === "jpg"
            || type === "jpeg"
        ) {

            return <ImageIcon color="primary" />;
        }

        return <DescriptionIcon color="action" />;
    }

    async function handleOpen(doc) {

        try {
            const url = await viewDocument(doc.id, doc.extensao);

            setViewerUrl(url);
            setSelectedDoc(doc);
            setOpenViewer(true);

        } catch (error) {

            console.error(error);
            console.error("Erro completo:", error);
            console.error("Status:", error?.response?.status);
            console.error("Data:", error?.response?.data);
            console.log("Error keys:", Object.keys(error || {}));
        }
    }

    useEffect(() => {

        if (!selectedPatient) {
            return;
        }

        async function load() {

            try {

                setLoading(true);

                const data = await getPatientDocuments(
                    selectedPatient.cpf
                );

                setDocuments(data);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);
            }
        }

        load();

    }, [selectedPatient]);

    return (
        <Box>

            <Box mb={4}>
                <Typography variant="h4" fontWeight={700}>
                    Documentos médicos
                </Typography>

                <Typography color="text.secondary">
                    Arquivos compartilhados pelos profissionais.
                </Typography>
            </Box>

            <Grid container spacing={3}>

                {documents.map((doc) => (

                    <Grid item xs={12} md={6} key={doc.id}>

                        <Paper
                            elevation={0}
                            sx={{
                                p: 3,
                                borderRadius: 4,
                                border: "1px solid #eee"
                            }}
                        >

                            <Box display="flex" gap={2}>

                                <Avatar
                                    sx={{
                                        bgcolor: "#e8f5e9",
                                        color: "#2e7d32"
                                    }}
                                >
                                    {doc.medicoNome
                                        ?.split(" ")
                                        .map((n) => n[0])
                                        .slice(0, 2)
                                        .join("")
                                    }
                                </Avatar>

                                <Box flex={1}>

                                    <Typography fontWeight={700}>
                                        {doc.nomeArquivo}
                                    </Typography>

                                    <Typography color="text.secondary" mb={2}>
                                        {doc.medicoNome}
                                    </Typography>

                                    <Box display="flex" alignItems="center" gap={1} mt={2}>

                                        {renderFileIcon(doc.extensao)}

                                        <Typography
                                            sx={{
                                                cursor: "pointer",

                                                "&:hover": {
                                                    textDecoration: "underline"
                                                }
                                            }}
                                            onClick={() =>
                                                handleOpen(doc)
                                            }
                                        >
                                            {doc.nomeOriginal || "Arquivo antigo"}
                                        </Typography>

                                        <Box flex={1} />

                                        <IconButton onClick={() => downloadDocument(doc.id, doc.nomeOriginal)}>
                                            <DownloadIcon color="error" />
                                        </IconButton>

                                    </Box>

                                    <Typography
                                        variant="caption"
                                        color="text.secondary"
                                    >
                                        {new Date(doc.dataUpload)
                                            .toLocaleString("pt-BR")}
                                    </Typography>

                                </Box>

                            </Box>

                        </Paper>

                    </Grid>

                ))}

            </Grid>

            <Dialog
                open={openViewer}
                onClose={() =>
                    setOpenViewer(false)
                }
                maxWidth="lg"
                fullWidth
            >

                <DialogContent
                    sx={{
                        p: 0,
                        height: "90vh",
                        position: "relative"
                    }}
                >

                    <IconButton
                        onClick={() =>
                            setOpenViewer(false)
                        }
                        sx={{
                            position: "absolute",
                            right: 12,
                            top: 12,
                            zIndex: 10,
                            bgcolor: "#fff",

                            "&:hover": {
                                bgcolor: "#f5f5f5"
                            }
                        }}
                    >

                        <CloseIcon />

                    </IconButton>

                    {selectedDoc && (

                        getFileType(selectedDoc.extensao) === "pdf" ? (

                            <object
                                data={viewerUrl}
                                type="application/pdf"
                                width="100%"
                                height="100%"
                            >

                                <Typography
                                    p={4}
                                >
                                    Não foi possível visualizar
                                    o PDF.
                                </Typography>

                            </object>

                        ) : (

                            <Box
                                display="flex"
                                justifyContent="center"
                                alignItems="center"
                                height="100%"
                            >

                                <img
                                    src={viewerUrl}
                                    alt="Documento"
                                    style={{
                                        maxWidth: "100%",
                                        maxHeight: "100%",
                                        objectFit: "contain"
                                    }}
                                />

                            </Box>

                        )

                    )}

                </DialogContent>

            </Dialog>

        </Box>
    );
}