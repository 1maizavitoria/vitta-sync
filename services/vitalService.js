import api from "./api";

export const registerVitalSigns = async (cpf, data) => {
    const response = await api.post(`/sinaisvitais/cadastrar/${cpf}`, data);
    return response.data;
};

export const getVitalSigns = async () => {
    const response = await api.get(`/sinaisvitais/getSinaisVitais`);
    return response.data;
};

export const editVitalSigns = async (id, data) => {
    const response = await api.put(`/sinaisvitais/editar/${id}`, data);
    return response.data;
};

export const deleteVitalSigns = async (id, cpf) => {
    const response = await api.delete(`/sinaisvitais/deletar/${id}/${cpf}`);
    return response.data;
};





