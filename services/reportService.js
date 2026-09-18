import api from "./api";

export async function previewReport(cpf, filters, signal) {
    const response = await api.post(`/relatorio/exportar/${encodeURIComponent(cpf)}`,
        { ...filters, preview: true }, { signal });
    return response.data;
}

export async function downloadReport(cpf, filters, signal) {
    try {
        const response = await api.post(`/relatorio/exportar/${encodeURIComponent(cpf)}`,
            { ...filters, preview: false }, { responseType: "blob", signal });
        const disposition = response.headers["content-disposition"] || "";
        const filename = disposition.match(/filename="?([^";]+)"?/i)?.[1];
        return {
            blob: response.data,
            filename: (filename || `relatorio.${filters.formato.toLowerCase()}`).replace(/[\\/]/g, "-"),
        };
    } catch (error) {
        // Erros JSON também chegam como Blob quando a requisição espera um arquivo.
        if (error.response?.data instanceof Blob) {
            try {
                error.response.data = JSON.parse(await error.response.data.text());
            } catch {
                // Preserva status HTTP para a mensagem de erro na interface.
            }
        }
        throw error;
    }
}
