import { Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import { logout } from "../../services/authService";

export default function Dashboard() {

    async function handleLogout() {
        try {
            localStorage.removeItem("token");
            window.location.href = "/";
            await logout();
        } catch (e) {
            console.error("Erro ao fazer logout:", e);
        }
    }

    return (
        <>
            <Typography variant="h4" gutterBottom>
                Home
            </Typography>

            <ButtonUI onClick={handleLogout}>
                Sair
            </ButtonUI>
        </>
    )
}