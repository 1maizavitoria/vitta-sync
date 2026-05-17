import api from "./api";

export const registerVitalSigns = async (cpf, data) => {
    const response = await api.post(
        `/sinaisvitais/cadastrar/${cpf}`,
        data
    );

    return response.data;
};

export const getVitalSigns = async (cpf) => {
    const response = await api.get(
        `/sinaisvitais/getSinaisVitais/${cpf}`
    );

    return response.data;
};

export const editVitalSigns = async (id, cpf, data) => {
    const response = await api.put(
        `/sinaisvitais/editar/${id}/${cpf}`,
        data
    );

    return response.data;
};

export const deleteVitalSigns = async (id, cpf) => {
    const response = await api.delete(
        `/sinaisvitais/deletar/${id}/${cpf}`
    );

    return response.data;
};