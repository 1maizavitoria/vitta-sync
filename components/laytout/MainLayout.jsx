import { Outlet } from "react-router-dom";
import AlertUI from "../ui/Alert";
import { useAlert } from "../../hooks/useAlert";
import Navbar from "./Navbar";

import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Sidebar from "./Sidebar";
import { useState } from "react";

import { usePatient } from "../../context/PatientContext";

export default function MainLayout() {
    const { alert } = useAlert();
    const [open, setOpen] = useState(false);
    const { selectedPatient } = usePatient();

    return (
        <Box sx={{ display: "flex" }}>

            <Navbar
                open={open}
                setOpen={setOpen}
                selectedPatient={selectedPatient}
            />

            <Sidebar open={open} setOpen={setOpen} />

            <Box component="main" sx={{ flexGrow: 1, p: 0 }}>
                <Toolbar /> {/* OFFSET DA NAVBAR */}

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