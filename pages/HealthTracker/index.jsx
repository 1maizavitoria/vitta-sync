import {
    Box,
    Typography,
    Tabs,
    Tab
} from "@mui/material";

import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import DirectionsWalkIcon from "@mui/icons-material/DirectionsWalk";
import AssignmentOutlinedIcon from "@mui/icons-material/AssignmentOutlined";

import { HabitTracker } from "../../components/ui/HabitTracker";
import { VitalTracker } from "../../components/ui/VitalTracker";

import ReminderCard from "../../components/ui/cards/RemindCard";

import { SymptomTracker } from "../../components/ui/SymptomTracker";
import { usePatient } from "../../context/PatientContext";
import { useState } from "react";

export default function HealthTreacker() {
    const { selectedPatient } = usePatient();
    const userType =
        localStorage
            .getItem("tipo")
            ?.toLowerCase();

    const [tab, setTab] = useState(0);
    const tabs = [
        {
            label: "Sinais Vitais",
            icon: <FavoriteBorderIcon />,
            component: <VitalTracker />
        },
        {
            label: "Hábitos",
            icon: <DirectionsWalkIcon />,
            component: <HabitTracker />
        },
        {
            label: "Sintomas",
            icon: <AssignmentOutlinedIcon />,
            component: <SymptomTracker />
        }
    ];
    return (

        <Box
            sx={{
                gridTemplateColumns: {
                    xs: "1fr",
                    lg: "1fr 340px",
                },
                gap: 3,
                alignItems: "start",
            }}
        >
            <Box
                sx={{
                    mb: 3,
                    p: 3,
                    borderRadius: "20px",
                    background:
                        "linear-gradient(135deg, #F4FFF8 0%, #EEF7FF 100%)",

                    border: "1px solid #E3EAF3",
                }}
            >

                <Typography
                    variant="h4"
                    fontWeight="bold"
                >
                    Acompanhamento de Saúde
                </Typography>

                {userType !== "paciente" &&
                    <Typography
                        variant="body1"
                        color="text.secondary"
                        mt={1}
                    >
                        Paciente selecionado:
                        {" "}
                        <strong>
                            {selectedPatient?.nome || "Nenhum paciente"}
                        </strong>
                    </Typography>}

            </Box>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: {
                        xs: "1fr",
                        lg: "1fr 340px",
                    },
                    gap: 3,
                    alignItems: "start",
                }}>
                <Box>
                    <Tabs
                        value={tab}
                        onChange={(e, newValue) => setTab(newValue)}
                        variant="fullWidth"
                        sx={{
                            mb: 1,
                            p: 0.5,
                            borderRadius: "20px",
                            background: "#F7FAF9",

                            "& .MuiTabs-indicator": {
                                display: "none",
                            },
                        }}
                    >
                        {tabs.map((item, index) => (
                            <Tab
                                key={index}
                                icon={item.icon}
                                iconPosition="start"
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

                    <Box
                        sx={{
                            mt: -1,
                            p: 3,
                            border: "1px solid #ECECEC",
                            borderRadius: "20px",
                            background: "#FFF",
                            boxShadow: "0 4px 14px rgba(0,0,0,0.06)",
                        }}
                    >
                        {tabs[tab].component}
                    </Box>


                </Box>


                <ReminderCard />
            </Box>





        </Box>

    );
}