import api from "./api";

export const registerVitalSigns = async (cpf, data) => {
    const response = await api.post(`/sinaisvitais/cadastrar/${cpf}`, data);
    return response.data;
};

export const getVitalSigns = async () => {
    const response = await api.get(`/sinaisvitais/getSinaisVitais`);
    return response.data;
};




