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
                px: 2,
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
                    width: "min(520px, 100%)",
                    borderRadius: 2,
                    boxShadow: "0 18px 40px rgba(15, 23, 42, 0.16)",
                    "& .MuiAlert-message": {
                        overflowWrap: "anywhere"
                    }
                }}
                {...props}>
                {message}
            </MuiAlert>

        </Box>
    );
}
