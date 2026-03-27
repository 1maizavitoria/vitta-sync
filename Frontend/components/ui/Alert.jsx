import { Box } from "@mui/material";
import MuiAlert from "@mui/material/Alert";

export default function AlertUI({ type = "info", message, ...props }) {
    return (
        <Box
            sx={{
                position: "fixed",
                top: 20,
                left: "50%",
                transform: "translateX(-50%)",
                zIndex: 9999,
                width: "100%",
                display: "flex",
                justifyContent: "center",
            }}
        >

            <MuiAlert
                severity={type}
                variant="standard"
                sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    width: "50vh"
                }}
                {...props}>
                {message}
            </MuiAlert>

        </Box>
    );
}