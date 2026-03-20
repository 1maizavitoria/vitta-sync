import { Box, Paper } from "@mui/material";
import Input from "../../components/ui/Input";
import ButtonUI from "../../components/ui/Button";


export default function Register() {
    return (
        <Box
            sx={{
                minHeight: "100vh",
                width: "100vw",
                bgcolor: "#ffffff",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <Paper elevation={5} sx={{ p: 4, width: 360, }}>
                <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    gap={2}
                >
                    <h1> Cadastro </h1>

                    <Input
                        placeholder="Email"
                    >
                    </Input>

                    <Input
                        placeholder="Senha"
                    >
                    </Input>

                    <Input
                        placeholder="Repetir Senha"
                    >
                    </Input>

                    <ButtonUI>
                        Cadastrar
                    </ButtonUI>
                </Box>
            </Paper >
        </Box >

    )

}