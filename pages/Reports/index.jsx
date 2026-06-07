import { Box } from "@mui/material";
import Perfil
    from "../../components/ui/Perfil";

import EmergencyContactsCard
    from "../../components/ui/cards/EmergencyContactsCard";

export default function Reports() {

    return (

        <Box sx={{ ml: 10 }}>
            <Perfil />
            <EmergencyContactsCard />
        </Box>

    );
}