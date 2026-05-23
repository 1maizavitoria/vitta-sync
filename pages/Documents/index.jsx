import { useState } from "react";
import { Box, Tabs, Tab, Paper } from "@mui/material";
import SharedDocuments from "../SharedDocuments";
import HealthHub from "../HealthHub";
import { usePatient } from "../../context/PatientContext";

export default function Documents() {
    const { selectedPatient } = usePatient();

    const userType = localStorage.getItem("tipo");
    const cpfUsuario = localStorage.getItem("cpf");

    const hasPatientSelected =
        selectedPatient &&
        selectedPatient.cpf !== cpfUsuario;

    const [tab, setTab] = useState(
        userType === "saude"
            ? "upload"
            : "documents"
    );

    return (

        <Paper
            elevation={0}
            sx={{
                p: 3,
                borderRadius: 4
            }}
        >

            <Tabs
                value={tab}
                onChange={(_, value) =>
                    setTab(value)
                }
                sx={{
                    mb: 3
                }}
            >



                {hasPatientSelected && (
                    <Tab
                        label="Documento do paciente"
                        value="documents"
                    />
                )}

                {userType === "saude" && (
                    <Tab
                        label="Enviar documento"
                        value="upload"
                    />
                )}



            </Tabs>
            <Box>

                {tab === "documents" && hasPatientSelected && (
                    <SharedDocuments />
                )}

                {tab === "upload" && userType === "saude" && (
                    <HealthHub />
                )}

            </Box>


        </Paper>
    );
}