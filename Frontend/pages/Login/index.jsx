import { Box, Button, Paper } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import Link from "../../components/ui/Link";
import { useState } from "react";
import Dialog from "../../components/ui/Dialog";

export default function Login() {
    const [open, setOpen] = useState(false);

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
                    <h1> Login </h1>
                    <Input
                        placeholder="Login"
                    >
                    </Input>
                    <Input
                        placeholder="Senha"
                    >
                    </Input>

                    <ButtonUI>
                        Entrar
                    </ButtonUI>

                    <Link onClick={() => setOpen(true)}>
                        Esqueceu a senha ?
                    </Link>

                    <Dialog
                        open={open}
                        onClose={() => setOpen(false)}
                        title="Este é seu email ?"
                        onConfirm={() => {
                            console.log("Confirmado!");
                            setOpen(false);
                        }}
                    >
                        Você recebera um email para trocar a senha.
                    </Dialog>

                    <Link to="/register" >
                        Não tem conta ? Clique aqui.
                    </Link>
                </Box>
            </Paper >
        </Box >
    )

}