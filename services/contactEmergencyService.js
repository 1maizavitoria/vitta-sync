import api from "./api";

export async function
    getEmergencyContacts(cpf) {

    const response =
        await api.get(
            `/contatoemergencia/listar/${cpf}`
        );

    return response.data;
}

export async function
    createEmergencyContact(
        cpf,
        data
    ) {

    const response =
        await api.post(
            `/contatoemergencia/cadastrar/${cpf}`,
            data
        );

    return response.data;
}

export async function
    editEmergencyContact(
        id,
        cpf,
        data
    ) {

    const response =
        await api.put(
            `/contatoemergencia/editar/${id}/${cpf}`,
            data
        );

    return response.data;
}

export async function
    deleteEmergencyContact(
        id,
        cpf
    ) {

    const response =
        await api.delete(
            `/contatoemergencia/deletar/${id}/${cpf}`
        );

    return response.data;
}