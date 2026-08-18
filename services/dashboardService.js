import api from "./api";

export async function getDashboard({ cpf, inicio, fim, categorias }) {
    const response = await api.get(`/dashboard/pacientes/${cpf}`, {
        params: {
            inicio,
            fim,
            ...(categorias ? { categorias } : {})
        }
    });

    return response.data;
}
