import { Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import { logout } from "../../services/authService";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
    const navigate = useNavigate();

    async function handleLogout() {
        try {
            await logout();
            localStorage.removeItem("token");
            navigate("/login");
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