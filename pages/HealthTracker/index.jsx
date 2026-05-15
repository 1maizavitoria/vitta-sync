import { Accordion, AccordionSummary, Box, Typography } from "@mui/material";
import { HabitTracker } from "../../components/ui/HabitTracker";
import { VitalTracker } from "../../components/ui/VitalTracker";


import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ReminderCard from "../../components/ui/RemindCard";

export default function HealthTreacker() {

    return (
        <Box sx={{ display: "flex", flexDirection: "row", gap: 3 }}>
            <Box>
                <Accordion defaultExpanded>
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

                <Accordion defaultExpanded>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1-content"
                        id="panel1-header"
                    >
                        <Typography variant="h5" fontWeight="bold" >
                            Hábitos
                        </Typography>

                    </AccordionSummary>

                    <HabitTracker />

                </Accordion>
            </Box>

            <Box>
                <ReminderCard />
            </Box>


        </Box>

    );
}