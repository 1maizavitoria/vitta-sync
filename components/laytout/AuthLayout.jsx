import { Outlet } from "react-router-dom";
import AlertUI from "../ui/Alert";
import { useAlert } from "../../hooks/useAlert";
import Navbar from "./Navbar";

import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import { useTheme } from "@mui/material/styles";

export default function AuthLayout() {
    const { alert } = useAlert();
    const theme = useTheme();

    return (
        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "background.default",
                background: theme.vitta.pageBackground,
                color: "text.primary"
            }}
        >
            <Navbar />

            <Toolbar />

            {alert && (
                <AlertUI
                    type={alert.type}
                    message={alert.message}
                />
            )}

            <Box
                sx={{
                    px: { xs: 2, md: 4 },
                    py: { xs: 2, md: 4 }
                }}
            >
                <Outlet />
            </Box>
        </Box>
    );
}
