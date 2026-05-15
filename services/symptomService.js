import api from "./api";

export const registerSymptom = async (cpf, data) => {
    const response = await api.post(`/diariosintomas/cadastrar/${cpf}`, data);
    return response.data;
};

export const editSymptom = async (id, cpf, data) => {
    const response = await api.put(`/diariosintomas/editar/${id}/${cpf}`, data);
    return response.data;
};

export const deleteSymptom = async (id, cpf) => {
    const response = await api.delete(`/diariosintomas/deletar/${id}/${cpf}`);
    return response.data;
};

export const getSymptom = async (cpf) => {
    const response = await api.get(`/diariosintomas/getSintomas/${cpf}`);
    return response.data;
};




