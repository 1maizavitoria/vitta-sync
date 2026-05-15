import { Outlet } from "react-router-dom";
import AlertUI from "../ui/Alert";
import { useAlert } from "../../hooks/useAlert";
import Navbar from "./Navbar";

import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";

export default function AuthLayout() {
    const { alert } = useAlert();

    return (
        <Box>
            <Navbar />

            <Toolbar />

            {alert && (
                <AlertUI
                    type={alert.type}
                    message={alert.message}
                />
            )}

            <Box sx={{ p: 2 }}>
                <Outlet />
            </Box>
        </Box>
    );
}