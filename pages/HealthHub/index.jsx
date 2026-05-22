import { Box, Button, Grid, IconButton, Paper, Typography } from "@mui/material";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import DownloadIcon from "@mui/icons-material/Download";
import CloseIcon from "@mui/icons-material/Close";
import { useEffect, useState } from "react";
import { deleteDocument, getDoctorDocuments, uploadDocument, downloadDocument } from "../../services/documentService";
import { usePatient } from "../../context/PatientContext";
import InputUI from "../../components/ui/Input";
import ButtonUI from "../../components/ui/Button";

export default function HealthHub() {

    const [dragging, setDragging] = useState(false);
    const [documents, setDocuments] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);
    const [nomeArquivo, setNomeArquivo] = useState("");
    const [loading, setLoading] = useState(false);

    const { selectedPatient } = usePatient();

    async function handleUpload() {
        if (!selectedPatient || !selectedFile || !nomeArquivo.trim()) {
            return;
        }

        try {
            setLoading(true);

            // Pega a extensão do arquivo real
            const ext = selectedFile.name.split(".").pop().toLowerCase();
            const nomeComExtensao = `${nomeArquivo.trim()}.${ext}`;

            const formData = new FormData();
            formData.append("arquivo", selectedFile);
            formData.append("nomeArquivo", nomeComExtensao); // 👈 com extensão

            const created = await uploadDocument(selectedPatient.cpf, formData);

            setDocuments((prev) => [created, ...prev]);
            setSelectedFile(null);
            setNomeArquivo("");

        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    }

    async function handleDelete(id) {

        try {

            await deleteDocument(id);

            setDocuments((prev) =>
                prev.filter((item) => item.id !== id)
            );

        } catch (error) {

            console.error(error);
        }
    }

    useEffect(() => {

        async function load() {

            try {

                setLoading(true);

                const data = await getDoctorDocuments();

                setDocuments(data);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);
            }
        }

        load();

    }, []);

    return (
        <Box>

            <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
                <Box>
                    <Typography variant="h4" fontWeight={700}>
                        Gerenciar documentos
                    </Typography>

                    <Typography color="text.secondary">
                        Upload e gerenciamento documental.
                    </Typography>
                </Box>

                <ButtonUI
                    variant="contained"
                    startIcon={<UploadFileIcon />}
                    sx={{ borderRadius: 3 }}
                    onClick={handleUpload}
                    disabled={loading}
                >
                    {loading ? "Enviando..." : "Enviar documento"}
                </ButtonUI>
            </Box>

            <Paper
                elevation={0}
                sx={{
                    p: 4,
                    borderRadius: 4,
                    border: "2px dashed #ddd",
                    mb: 4,
                    textAlign: "center",
                    backgroundColor: dragging ? "#f5f5f5" : "#fff",
                    transition: ".2s"
                }}
                onDragOver={(e) => {
                    e.preventDefault();
                    setDragging(true);
                }}
                onDragLeave={() => setDragging(false)}
                onDrop={(e) => {
                    e.preventDefault();
                    setDragging(false);

                    const file = e.dataTransfer.files[0];

                    if (file && file.type === "application/pdf") {
                        setSelectedFile(file);
                    } else {
                        alert("Envie apenas PDFs");
                    }
                }}
            >

                <Box display="flex" alignItems="center" flexDirection="column" gap={3}>

                    <UploadFileIcon sx={{ fontSize: 48, color: "#777" }} />

                    <Typography fontWeight={600}>
                        {dragging ? "Solte o PDF aqui" : "Enviar documento médico"}
                    </Typography>

                    <InputUI
                        label="Nome do documento"
                        value={nomeArquivo}
                        onChange={(e) => setNomeArquivo(e.target.value)}
                        sx={{ maxWidth: 220 }}
                    />

                    <Button variant="outlined" component="label">
                        Selecionar PDF

                        <input
                            hidden
                            type="file"
                            accept="application/pdf"
                            onChange={(e) =>
                                setSelectedFile(e.target.files[0])
                            }
                        />
                    </Button>

                    {selectedFile && (
                        <Typography variant="body2" color="text.secondary">
                            {selectedFile.name}
                        </Typography>
                    )}

                </Box>

            </Paper>

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

                            <Box display="flex" justifyContent="space-between">

                                <Box>
                                    <Typography fontWeight={700}>
                                        {doc.nomeArquivo}
                                    </Typography>

                                    <Typography color="text.secondary" mb={2}>
                                        {doc.pacienteCpf}
                                    </Typography>
                                </Box>

                                <IconButton onClick={() => handleDelete(doc.id)}>
                                    <CloseIcon color="error" />
                                </IconButton>

                            </Box>

                            <Box display="flex" alignItems="center" gap={1}>

                                <PictureAsPdfIcon color="error" />

                                <Typography>
                                    {doc.nomeArquivo}
                                </Typography>

                                <Box flex={1} />

                                <IconButton onClick={() => downloadDocument(doc.id, doc.nomeArquivo)}>
                                    <DownloadIcon color="error" />
                                </IconButton>

                            </Box>

                            <Typography variant="caption" color="text.secondary">
                                {new Date(doc.dataUpload).toLocaleString("pt-BR")}
                            </Typography>

                        </Paper>

                    </Grid>

                ))}

            </Grid>

        </Box>
    );
}