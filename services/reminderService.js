import api from "./api";

export const registerReminder = async (data) => {
    const response = await api.post(`/lembretes/registrar`, data);
    return response.data;
};

export const getReminder = async () => {
    const response = await api.get(`/lembretes/getLembrete`);
    return response.data;
};

export const activateReminder = async () => {
    const response = await api.put(`/lembretes/ativar`);
    return response.data;
};

export const deactivateReminder = async () => {
    const response = await api.put(`/lembretes/desativar`);
    return response.data;
};

