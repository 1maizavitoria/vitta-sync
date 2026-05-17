import { Accordion, AccordionSummary, Box, Typography } from "@mui/material";
import { HabitTracker } from "../../components/ui/HabitTracker";
import { VitalTracker } from "../../components/ui/VitalTracker";


import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ReminderCard from "../../components/ui/cards/RemindCard";
import { SymptomTracker } from "../../components/ui/SymptomTracker";
import { usePatient } from "../../context/PatientContext";

export default function HealthTreacker() {
    const { selectedPatient } = usePatient();

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
                </Typography>

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

                    <Accordion
                        defaultExpanded
                        sx={{
                            borderRadius: "20px !important",
                            overflow: "hidden",
                            boxShadow:
                                "0 4px 14px rgba(0,0,0,0.06)",

                            border: "1px solid #ECECEC",

                            "&:before": {
                                display: "none",
                            },
                        }}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1-content"
                            id="panel1-header"
                        >
                            <Typography variant="h5" fontWeight="bold" >
                                Sinais Vitais
                            </Typography>
                        </AccordionSummary>

                        <VitalTracker />

                    </Accordion>

                    <Accordion
                        defaultExpanded
                        sx={{
                            borderRadius: "20px !important",
                            overflow: "hidden",
                            boxShadow:
                                "0 4px 14px rgba(0,0,0,0.06)",

                            border: "1px solid #ECECEC",

                            "&:before": {
                                display: "none",
                            },
                        }}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1-content"
                            id="panel2-header"
                        >
                            <Typography variant="h5" fontWeight="bold" >
                                Hábitos
                            </Typography>

                        </AccordionSummary>

                        <HabitTracker />

                    </Accordion>

                    <Accordion
                        defaultExpanded
                        sx={{
                            borderRadius: "20px !important",
                            overflow: "hidden",
                            boxShadow:
                                "0 4px 14px rgba(0,0,0,0.06)",

                            border: "1px solid #ECECEC",

                            "&:before": {
                                display: "none",
                            },
                        }}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1-content"
                            id="panel3-header"
                        >
                            <Typography variant="h5" fontWeight="bold" >
                                Diário de Sintomas
                            </Typography>

                        </AccordionSummary>

                        <SymptomTracker />

                    </Accordion>


                </Box>


                <ReminderCard />
            </Box>





        </Box>

    );
}