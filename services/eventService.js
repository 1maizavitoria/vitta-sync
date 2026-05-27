import api from "./api";

export async function getPatientEvents(patientId) {

    const response = await api.get(
        `/eventos/paciente/${patientId}`
    );

    return response.data;
}

export async function getUnreadEventsCount(patientId) {

    const response = await api.get(
        `/eventos/paciente/${patientId}/nao-visualizados`
    );

    return response.data;
}

export async function markEventsAsRead(
    patientId
) {

    await api.put(
        `/eventos/paciente/${patientId}/visualizar`
    );
}