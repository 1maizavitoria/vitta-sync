import { Outlet } from "react-router-dom";
import AlertUI from "../ui/Alert";
import { useAlert } from "../../hooks/useAlert";
import Navbar from "./Navbar";

import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Sidebar from "./Sidebar";

export default function MainLayout() {
    const { alert } = useAlert();

    return (
        <Box sx={{ display: "flex" }}>
            <Navbar />

            <Sidebar />

            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
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