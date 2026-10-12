import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react";

import {
    getAvailablePatients
} from "../services/linkService";
import { useAlert } from "../hooks/useAlert";
import { useI18n } from "../src/i18n";
import { classifyApiError } from "../utils/api/classifyApiError";

function getPatientContextErrorKey(error) {
    const errorType = classifyApiError(error).type;
    const translatedErrors = ["network", "forbidden", "notFound", "server"];

    return translatedErrors.includes(errorType)
        ? `patientContext.errors.${errorType}`
        : "patientContext.errors.load";
}

const PatientContext =
    createContext();

export function usePatient() {

    return useContext(
        PatientContext
    );
}

export function PatientProvider({
    children
}) {

    const { showAlert } = useAlert();
    const { t } = useI18n();

    const [
        selectedPatient,
        setSelectedPatient
    ] = useState(null);

    const [
        patients,
        setPatients
    ] = useState([]);

    async function loadPatients() {

        try {

            const data =
                await getAvailablePatients();

            setPatients(data);

            const userType =
                localStorage.getItem("tipo");

            if (
                userType?.toLowerCase() ===
                "paciente"
            ) {

                setSelectedPatient(data[0]);

                return;
            }

            setSelectedPatient(prev => {

                if (!data.length) {
                    return null;
                }

                const stillExists =
                    data.some(
                        patient =>
                            patient.id === prev?.id
                    );

                return stillExists
                    ? prev
                    : data[0];
            });

        } catch (error) {
            console.error(error);
            setPatients([]);
            setSelectedPatient(null);
            showAlert("error", t(getPatientContextErrorKey(error)));
        }
    }

    function addPatient(patient) {

        setPatients(prev => {

            const alreadyExists =
                prev.some(
                    item =>
                        item.id === patient.id
                );

            if (alreadyExists) {
                return prev;
            }

            return [
                ...prev,
                patient
            ];
        });
    }

    function updatePatient(updatedPatient) {

        setPatients(prev =>
            prev.map(patient =>
                patient.id === updatedPatient.id
                    ? updatedPatient
                    : patient
            )
        );

        setSelectedPatient(prev =>
            prev?.id === updatedPatient.id
                ? updatedPatient
                : prev
        );
    }

    function removePatient(id) {

        setPatients(prev => {

            const updated =
                prev.filter(
                    patient =>
                        patient.id !== id
                );

            return updated;
        });

        setSelectedPatient(prev =>
            prev?.id === id
                ? null
                : prev
        );
    }

    useEffect(() => {

        loadPatients();

    }, []);

    return (

        <PatientContext.Provider
            value={{

                selectedPatient,
                setSelectedPatient,

                patients,

                addPatient,
                updatePatient,
                removePatient,

                loadPatients
            }}
        >

            {children}

        </PatientContext.Provider>
    );
}
