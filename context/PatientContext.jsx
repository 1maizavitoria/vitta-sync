import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react";

import {
    getAvailablePatients
} from "../services/linkService";

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

    const [selectedPatient,
        setSelectedPatient] =
        useState(null);

    const [patients,
        setPatients] =
        useState([]);

    async function refreshPatients() {

        try {

            const data =
                await getAvailablePatients();

            setPatients(data);

            setSelectedPatient((prev) => {

                if (prev) {
                    return prev;
                }

                return data[0] || null;
            }); {

                setSelectedPatient(
                    data[0]
                );
            }

        } catch (error) {

            console.error(error);
        }
    }

    useEffect(() => {

        refreshPatients();

    }, []);

    return (

        <PatientContext.Provider
            value={{
                selectedPatient,
                setSelectedPatient,

                patients,
                setPatients,

                refreshPatients
            }}
        >

            {children}

        </PatientContext.Provider>
    );
}

