import { useLocation, useNavigate } from "react-router-dom";

import Drawer from "@mui/material/Drawer";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import Avatar from "@mui/material/Avatar";

import DashboardIcon from "@mui/icons-material/Dashboard";
import DescriptionIcon from "@mui/icons-material/Description";
import LinkIcon from "@mui/icons-material/Link";
import ShowChartIcon from "@mui/icons-material/ShowChart";

const drawerWidth = 240;

const menuItems = [
    { label: "Dashboard", icon: <DashboardIcon />, path: "/dashboard" },
    { label: "Registros", icon: <DescriptionIcon />, path: "/health-tracker" },
    { label: "Vínculos", icon: <LinkIcon />, path: "/links" },
    { label: "Relatórios", icon: <ShowChartIcon />, path: "/reports" },
];

export default function Sidebar() {
    const location = useLocation();
    const navigate = useNavigate();

    return (
        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                    boxSizing: "border-box",
                    borderRight: "1px solid #eee",
                },
            }}
        >

            {/* MENU */}
            <List sx={{ p: 1, mt: 10 }}>
                {menuItems.map((item) => {
                    const isActive = location.pathname === item.path;

                    return (
                        <ListItemButton
                            key={item.label}
                            onClick={() => navigate(item.path)}
                            sx={{
                                borderRadius: 3,
                                mb: 0.5,

                                ...(isActive && {
                                    background: "linear-gradient(90deg, #a8e6cf, #dcedc1)",
                                    boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
                                }),
                            }}
                        >
                            <ListItemIcon sx={{ color: "inherit" }}>
                                {item.icon}
                            </ListItemIcon>

                            <ListItemText primary={item.label} />
                        </ListItemButton>
                    );
                })}
            </List>

            {/* FOOTER (user) */}
            <Box sx={{ mt: "auto", p: 2 }}>
                <Divider sx={{ mb: 2 }} />

                <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                    <Avatar sx={{ bgcolor: "#a8e6cf", color: "#000" }}>
                        AS
                    </Avatar>

                    <Box>
                        <Typography variant="body2" fontWeight="bold">
                            Ana Silva
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                            Paciente
                        </Typography>
                    </Box>
                </Box>
            </Box>
        </Drawer>
    );
}