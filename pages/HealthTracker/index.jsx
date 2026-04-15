import { Accordion, AccordionSummary, Box, Typography } from "@mui/material";
import { HabitTracker } from "../../components/ui/HabitTracker";
import { VitalTracker } from "../../components/ui/VitalTracker";


import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export default function HealthTreacker() {


    return (
        <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
            <Accordion>
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

            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel1-content"
                    id="panel1-header"
                >
                    <Typography variant="h5" fontWeight="bold" >
                        Habitos
                    </Typography>

                </AccordionSummary>

                <HabitTracker />

            </Accordion>



        </Box>

    );
}