import api from "./api";

export async function getGoals(cpf) {
    const response = await api.get(`/metaacompanhamento/getMetas/${cpf}`);
    return response.data;
}

export async function createGoal(cpf, data) {
    const response = await api.post(`/metaacompanhamento/cadastrar/${cpf}`, data);
    return response.data;
}

export async function updateGoal(id, cpf, data) {
    const response = await api.put(`/metaacompanhamento/editar/${id}/${cpf}`, data);
    return response.data;
}

export async function deleteGoal(id, cpf) {
    await api.delete(`/metaacompanhamento/deletar/${id}/${cpf}`);
}

export async function completeGoal(id, cpf) {
    const response = await api.post(`/metaacompanhamento/concluir/${id}/${cpf}`);
    return response.data;
}
