import { Outlet } from "react-router-dom";
import AlertUI from "../ui/Alert";
import { useAlert } from "../../hooks/useAlert";
import Navbar from "./Navbar";

import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import { useTheme } from "@mui/material/styles";
import Sidebar from "./Sidebar";
import { useState } from "react";

import { usePatient } from "../../context/PatientContext";

export default function MainLayout() {
    const { alert } = useAlert();
    const theme = useTheme();
    const [open, setOpen] = useState(false);
    const { selectedPatient } = usePatient();
    const collapsedSidebarWidth = 84;

    return (
        <Box
            sx={{
                display: "flex",
                minHeight: "100vh",
                bgcolor: "background.default",
                background: theme.vitta.pageBackground,
                color: "text.primary"
            }}
        >

            <Navbar
                open={open}
                setOpen={setOpen}
                selectedPatient={selectedPatient}
            />

            <Sidebar open={open} setOpen={setOpen} />

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    minWidth: 0,
                    ml: {
                        xs: 0,
                        md: `${collapsedSidebarWidth}px`
                    },
                    bgcolor: "transparent"
                }}
            >
                <Toolbar />

                {alert && (
                    <AlertUI
                        type={alert.type}
                        message={alert.message}
                    />
                )}

                <Outlet />
            </Box>
        </Box>
    );
}
