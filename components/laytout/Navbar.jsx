import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import Badge from "@mui/material/Badge";
import Button from "@mui/material/Button";

import NotificationsIcon from "@mui/icons-material/Notifications";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import MenuIcon from '@mui/icons-material/Menu';

import ButtonUI from "../ui/Button";
import { Navigate, useLocation, useNavigate } from "react-router-dom";

export default function Navbar({
    title = "",
    subtitle = ""
}) {
    const navigate = useNavigate();
    const location = useLocation();

    return (

        <AppBar
            position="fixed"
            elevation={1}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                backgroundColor: "#fff",
                color: "#000"
            }}
        >
            <Toolbar
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                }}
            >
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>

                    <Typography
                        sx={{
                            fontSize: "2rem",
                            fontWeight: 700,
                            color: "#5A9B4D",
                            lineHeight: 1,
                        }}
                    >
                        VittaSync
                    </Typography>
                </Box>

                <Box>
                    <Typography variant="h6" fontWeight="bold">
                        {title}
                    </Typography>

                    <Typography variant="body2" color="text.secondary">
                        {subtitle}
                    </Typography>
                </Box>

                {/* DIREITA */}
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>

                    {location.pathname === "/" && <ButtonUI
                        variant="outlined"
                        sx={{
                            textTransform: "none",
                            borderRadius: 3,
                        }}
                        onClick={() => navigate("/login")}
                    >
                        Entrar / Registrar
                    </ButtonUI>}



                </Box>
            </Toolbar>
        </AppBar>
    );
}