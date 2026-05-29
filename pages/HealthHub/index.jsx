import { Box, Button, Grid, IconButton, Paper, Typography, Avatar } from "@mui/material";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import DownloadIcon from "@mui/icons-material/Download";
import CloseIcon from "@mui/icons-material/Close";
import { useEffect, useState, useRef } from "react";
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
    const cpfUsuario = localStorage.getItem("cpf");

    const { selectedPatient } = usePatient();
    const hasPatientSelected = selectedPatient && selectedPatient.cpf !== cpfUsuario;

    // ref para resetar o input
    const fileInputRef = useRef(null);

    async function loadDocuments() {

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

    async function handleUpload() {
        if (!selectedPatient || !selectedFile || !nomeArquivo.trim()) {
            return;
        }

        try {
            setLoading(true);

            const formData = new FormData();
            formData.append("arquivo", selectedFile);
            formData.append("nomeArquivo", nomeArquivo.trim());

            const created = await uploadDocument(selectedPatient.cpf, formData);

            await loadDocuments();

            // limpa input e estado
            setSelectedFile(null);
            setNomeArquivo("");
            if (fileInputRef.current) {
                fileInputRef.current.value = "";
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    }

    async function handleDelete(id) {
        try {
            await deleteDocument(id);
            setDocuments((prev) => prev.filter((item) => item.id !== id));

            // limpa input e estado
            setSelectedFile(null);
            setNomeArquivo("");
            if (fileInputRef.current) {
                fileInputRef.current.value = "";
            }
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        loadDocuments();
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
                    disabled={loading || !hasPatientSelected}
                >
                    {loading
                        ? "Enviando..."
                        : !hasPatientSelected
                            ? "Selecione um paciente"
                            : "Enviar documento"}
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
                            ref={fileInputRef}
                            hidden
                            type="file"
                            accept="application/pdf"
                            onChange={(e) => setSelectedFile(e.target.files[0])}
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
                                    <Avatar sx={{ bgcolor: "#e3f2fd", color: "#1565c0" }}>
                                        {doc.pacienteNome
                                            ?.split(" ")
                                            .map((n) => n[0])
                                            .slice(0, 2)
                                            .join("")}
                                    </Avatar>

                                    <Box flex={1}>
                                        <Box display="flex" justifyContent="space-between" alignItems="flex-start">
                                            <Box>
                                                <Typography fontWeight={700}>
                                                    {doc.nomeArquivo}
                                                </Typography>
                                                <Typography color="text.secondary">
                                                    {doc.pacienteNome || "undefined"}
                                                </Typography>
                                            </Box>

                                            <IconButton onClick={() => handleDelete(doc.id)}>
                                                <CloseIcon color="error" />
                                            </IconButton>
                                        </Box>

                                        <Box display="flex" alignItems="center" gap={1} mt={2}>
                                            <PictureAsPdfIcon color="error" />
                                            <Typography>
                                                {doc.nomeOriginal || "Arquivo antigo"}
                                            </Typography>
                                            <Box flex={1} />
                                            <IconButton
                                                onClick={() =>
                                                    downloadDocument(doc.id, doc.nomeOriginal)
                                                }
                                            >
                                                <DownloadIcon color="error" />
                                            </IconButton>
                                        </Box>

                                        <Typography variant="caption" color="text.secondary">
                                            {new Date(doc.dataUpload).toLocaleString("pt-BR")}
                                        </Typography>
                                    </Box>
                                </Box>
                            </Paper>
                        </Grid>
                    ))}
                </Grid>
            </Grid>
        </Box>
    );
}
