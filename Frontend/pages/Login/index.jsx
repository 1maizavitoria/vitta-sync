import { Box, Button, Grid, Paper, Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import Link from "../../components/ui/Link";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import InputUI from "../../components/ui/Input";
import DialogUI from "../../components/ui/Dialog";
import AlertUI from "../../components/ui/Alert";
import LinkUI from "../../components/ui/Link";

export default function Login() {

    const [open, setOpen] = useState(false);
    const [error, setError] = useState(false);
    const [password, setPassword] = useState("");
    const [login, setLogin] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [typeError, setTypeError] = useState("error");

    const navigate = useNavigate();

    const canLogin = () => {
        if (login !== "" && password !== "") {
            setError(false);
            setErrorMessage("Sucesso");
            setTypeError("success");
            return true;
        } else {
            setError(true);
            setErrorMessage("Preencha todos os campos");
            setTypeError("error");
            return false;
        }
    };

    function handleLogin() {
        if (canLogin()) {
            navigate("/home");
        }
    }

    return (
        <Box>
            {error &&
                <AlertUI
                    type={typeError}
                    message={errorMessage}
                />
            }

            <Grid
                container
                justifyContent="center"
                alignItems="center"
                sx={{ minHeight: "95vh" }}
            >
                <Grid item>

                    <Paper elevation={5} sx={{ p: 4, width: 360, }}>
                        <Box
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            gap={2}

                        >
                            <Typography variant="h4" gutterBottom>
                                Login
                            </Typography>

                            <InputUI
                                placeholder="Login"
                                error={error && login === ""}
                                value={login}
                                onChange={(e) => (
                                    setLogin(e.target.value),
                                    setError(false),
                                    setOpen(false)
                                )}
                            />

                            <InputUI
                                type="password"
                                error={error && password === ""}
                                value={password}
                                placeholder="Senha"
                                onChange={(e) => (
                                    setPassword(e.target.value),
                                    setError(false)
                                )}
                            />

                            <ButtonUI
                                onClick={handleLogin}
                            >
                                Entrar
                            </ButtonUI>

                            <LinkUI onClick={() => setOpen(true)}>
                                Esqueceu a senha ?
                            </LinkUI>

                            <DialogUI
                                open={open && login !== ""}
                                onClose={() => setOpen(false)}
                                title={"Confirmar Email"}
                                onConfirm={() => {
                                    setOpen(false);
                                }}
                            >
                                Este é seu email? <br /> <br />
                                <strong>{login}</strong>
                            </DialogUI>

                            <Link to="/register" >
                                Não tem conta ? Clique aqui.
                            </Link>

                        </Box>
                    </Paper >
                </Grid >
            </Grid>
        </Box>
    )

}