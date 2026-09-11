import api from "./api";

export async function getClinicalTimeline({ cpf, inicio, fim }) {
    const response = await api.get(`/api/linha-tempo/paciente/${cpf}`, {
        params: { inicio, fim }
    });

    return response.data;
}
