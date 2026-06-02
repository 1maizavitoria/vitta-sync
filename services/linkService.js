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

export const generateLinkCode = async (
    pacienteId
) => {

    const response =
        await api.post(
            "/vinculos/gerar",
            {
                pacienteId
            }
        );

    return response.data;
};

export const joinWithCode = async (code, funcao) => {

    const response =
        await api.post(
            "/vinculos/entrar",
            {
                codigo: code,
                funcao
            }
        );

    return response.data;
};

export const sendInviteEmail = async (email, code) => {

    const response =
        await api.post("/vinculos/enviar-email", { email, codigo: code });

    return response.data;
};

export async function getAvailablePatients() {

    const response =
        await api.get(
            "/vinculos/pacientes"
        );

    return response.data;
}

export async function
    getLinksByPatientId(
        patientId
    ) {

    const response =
        await api.get(
            `/vinculos/paciente/${patientId}`
        );

    return response.data;
}