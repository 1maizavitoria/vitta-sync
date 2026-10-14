import api from "./api";

export async function getClinicalTimeline({ cpf, inicio, fim, signal }) {
    const response = await api.get(`/api/linha-tempo/paciente/${cpf}`, {
        params: { inicio, fim },
        signal
    });

    return response.data;
}
