import api from "./api";

export const registerReminder = async (
    cpf,
    data
) => {

    const response = await api.post(
        `/lembretes/registrar/${cpf}`,
        data
    );

    return response.data;
};

export const getReminder = async (
    cpf
) => {

    const response = await api.get(
        `/lembretes/getLembrete/${cpf}`
    );

    return response.data;
};

export const activateReminder = async (
    cpf
) => {

    const response = await api.put(
        `/lembretes/ativar/${cpf}`
    );

    return response.data;
};

export const deactivateReminder = async (
    cpf
) => {

    const response = await api.put(
        `/lembretes/desativar/${cpf}`
    );

    return response.data;
};