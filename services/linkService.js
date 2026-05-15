import api from "./api";

export const getLinks = async () => {

    const response =
        await api.get("/vinculos");

    return response.data;
};

export const removeLink = async (id) => {

    const response =
        await api.delete(`/vinculos/${id}`);

    return response.data;
};

export const generateLinkCode = async () => {

    const response =
        await api.post("/vinculos/gerar");

    return response.data;
};

export const joinWithCode = async (code) => {

    const response =
        await api.post("/vinculos/entrar", {
            codigo: code
        });

    return response.data;
};

export const sendInviteEmail = async (email, code) => {

    const response =
        await api.post("/vinculos/enviar-email", { email, codigo: code });

    return response.data;
};