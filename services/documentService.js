import api from "./api";

export async function
    getPatientDocuments(cpf) {

    const response =
        await api.get(
            `/documentos/getDocumentosPaciente/${cpf}`
        );

    return response.data;
}

export async function
    getDoctorDocuments() {

    const response =
        await api.get(
            "/documentos/getDocumentosMedico"
        );

    return response.data;
}

export async function
    uploadDocument(
        cpfPaciente,
        formData
    ) {

    const response =
        await api.post(
            `/documentos/upload/${cpfPaciente}`,
            formData,
            {
                headers: {
                    "Content-Type":
                        "multipart/form-data"
                }
            }
        );

    return response.data;
}

export async function
    deleteDocument(id) {

    await api.delete(
        `/documentos/deletarDocumento/${id}`
    );
}

export function
    getViewDocumentUrl(id) {

    return `${api.defaults.baseURL}/documentos/${id}/visualizarDocumento`;
}

export async function downloadDocument(id, nomeArquivo) {
    const response = await api.get(
        `/documentos/${id}/downloadDocumento`,
        { responseType: "blob" }
    );

    const ext = nomeArquivo?.split(".").pop().toLowerCase();
    const typeMap = {
        pdf: "application/pdf",
        png: "image/png",
        jpg: "image/jpeg",
        jpeg: "image/jpeg",
    };
    const type = typeMap[ext] || "application/octet-stream";

    const blob = new Blob([response.data], { type });
    const url = URL.createObjectURL(blob);

    // Cria um link temporário e clica automaticamente
    const link = document.createElement("a");
    link.href = url;
    link.download = nomeArquivo;
    link.click();

    URL.revokeObjectURL(url); // limpa a memória
}

export async function viewDocument(id, nomeArquivo) {
    const response = await api.get(
        `/documentos/${id}/visualizarDocumento`,
        { responseType: "blob" }
    );

    // Detecta o tipo pelo nome do arquivo
    const ext = nomeArquivo?.split(".").pop().toLowerCase();
    const typeMap = {
        pdf: "application/pdf",
        png: "image/png",
        jpg: "image/jpeg",
        jpeg: "image/jpeg",
    };
    const type = typeMap[ext] || "application/octet-stream";

    // Recria o blob com o tipo correto
    const blob = new Blob([response.data], { type });
    return URL.createObjectURL(blob);
}
