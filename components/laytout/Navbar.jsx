import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import NotificationsIcon from "@mui/icons-material/Notifications";
import Badge from "@mui/material/Badge";
import Button from "@mui/material/Button";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";

export default function Navbar({
    variant = "dashboard",
    title = "",
    subtitle = "",
}) {

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
                {/* ESQUERDA */}

                {/* LOGO */}
                <Box sx={{ p: 2 }}>
                    <Typography variant="h6" fontWeight="bold" color="green">
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

                    {/* Botão de período */}
                    {/* <Button
                        variant="outlined"
                        endIcon={<KeyboardArrowDownIcon />}
                        sx={{
                            textTransform: "none",
                            borderRadius: 3,
                        }}
                    >
                        Últimos 7 dias
                    </Button> */}

                    {/* Notificação */}
                    {/* <IconButton>
                        <Badge badgeContent={1} color="error">
                            <NotificationsIcon />
                        </Badge>
                    </IconButton> */}
                </Box>
            </Toolbar>
        </AppBar>
    );
}