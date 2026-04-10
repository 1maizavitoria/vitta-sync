import api from "./api";

export const registerHabits = async (cpf, data) => {
    const response = await api.post(`/habitos/cadastrar/${cpf}`, data);
    return response.data;
};

export const editHabits = async (id, cpf, data) => {
    const response = await api.put(`/habitos/editar/${id}/${cpf}`, data);
    return response.data;
};

export const deleteHabits = async (id, cpf) => {
    const response = await api.delete(`/habitos/deletar/${id}/${cpf}`);
    return response.data;
};

export const getHabits = async (cpf) => {
    const response = await api.get(`/habitos/getHabitos/${cpf}`);
    return response.data;
};




