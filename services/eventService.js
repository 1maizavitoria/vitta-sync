import api from "./api";

export async function getPatientEvents(patientId) {

    const response =
        await api.get(
            `/eventos/paciente/${patientId}`
        );

    return response.data;
}