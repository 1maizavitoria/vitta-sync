import { useState } from "react";
import { Box, Tabs, Tab, Paper } from "@mui/material";
import SharedDocuments from "../SharedDocuments";
import HealthHub from "../HealthHub";

export default function Documents() {

    const userType = localStorage.getItem("tipo");

    const [tab, setTab] = useState(0);

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

                <Tab
                    label="Documento do paciente"
                />

                {userType === "saude" &&
                    (<Tab
                        label="Enviar documento"
                    />
                    )}

            </Tabs>

            <Box>

                {tab === 0 && (
                    <SharedDocuments />
                )}

                {tab === 1 && userType === "saude" && (
                    <HealthHub />
                )}

            </Box>

        </Paper>
    );
}