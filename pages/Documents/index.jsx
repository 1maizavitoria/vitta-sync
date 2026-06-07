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

    const tabs = [
        hasPatientSelected && {
            label: "Documento do paciente",
            value: "documents"
        },

        userType === "saude" && {
            label: "Enviar documento",
            value: "upload"
        }

    ].filter(Boolean);

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
                    mb: 3,
                    p: 0.5,
                    borderRadius: "20px",
                    background: "#F7FAF9",

                    "& .MuiTabs-indicator": {
                        display: "none",
                    },
                }}
            >
                {tabs.map((item) => (

                    <Tab
                        key={item.value}
                        value={item.value}
                        label={item.label}
                        sx={{
                            borderRadius: "16px",
                            minHeight: 52,
                            textTransform: "none",
                            fontWeight: 700,
                            mx: 0.5,
                            color: "#4B5563",

                            "&.Mui-selected": {
                                color: "#1F2937",
                                background:
                                    "linear-gradient(90deg, #BEE8D7 0%, #B7D88A 100%)",
                                boxShadow:
                                    "0 4px 10px rgba(0,0,0,0.10)",
                            },
                        }}
                    />

                ))}
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