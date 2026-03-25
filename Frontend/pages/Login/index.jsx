import { Box, Button, Grid, Paper, Tooltip, Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import Link from "../../components/ui/Link";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import InputUI from "../../components/ui/Input";
import DialogUI from "../../components/ui/Dialog";
import AlertUI from "../../components/ui/Alert";
import LinkUI from "../../components/ui/Link";
import { login, validadeCode, validadeCodePassword, changePassword } from "../../services/userService"

export default function Login() {

    const [openForgotDialog, setOpenForgotDialog] = useState(false);
    const [openLoginDialog, setOpenLoginDialog] = useState(false);

    const [error, setError] = useState(false);
    const [password, setPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [email, setEmail] = useState("");
    const [sendEmail, setSendEmail] = useState(false);

    const [CPF, setCPF] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [typeError, setTypeError] = useState("error");

    const [code, setCode] = useState("");

    const navigate = useNavigate();

    const canLogin = () => {
        if (CPF !== "" && password !== "") {
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

    async function handleLogin() {
        if (!canLogin()) return;

        const data = {
            cpf: CPF,
            senha: password
        };

        try {
            await login(data);
            setOpenLoginDialog(true);

        } catch (error) {
            console.error(error);
        }
    }

    async function hadleValidateCode() {
        const data = {
            codigo: code,
        };

        try {
            await validadeCode(data);
            navigate("/home");
        } catch (error) {
            console.log(error);
        }
    }

    async function handleValidateCodePassword() {
        const data = {
            email: email,
        };

        try {
            await validadeCodePassword(data);
            setSendEmail(true);
        } catch (error) {
            console.log(error);
        }
    }

    async function handleChangeCodePassword() {

        const data = {
            codigo: code,
            novaSenha: newPassword
        };

        try {
            await changePassword(data);
        } catch (error) {
            console.log(error);
        }
    }

    // Regras senha
    const hasMinLength = (password) => password.length >= 8;
    const hasUppercase = (password) => /[A-Z]/.test(password);
    const hasLowercase = (password) => /[a-z]/.test(password);
    const hasNumber = (password) => /\d/.test(password);
    const hasSpecialChar = (password) => /[^A-Za-z\d]/.test(password);

    function validatePassword(password) {
        const result = {
            minLength: hasMinLength(password),
            upperCase: hasUppercase(password),
            lowerCase: hasLowercase(password),
            number: hasNumber(password),
            specialChar: hasSpecialChar(password)
        }

        return {
            ...result,
            isValid: Object.values(result).every(Boolean)
        };
    }

    const rulesPassword = validatePassword(newPassword);

    const titlePasswordTooltip = (
        <Box>
            <Box style={{ color: rulesPassword.minLength ? "lightgreen" : "#ff6b6b" }}>
                • 8 caracteres
            </Box>
            <Box style={{ color: rulesPassword.upperCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra maiúscula
            </Box>
            <Box style={{ color: rulesPassword.lowerCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra minúscula
            </Box>
            <Box style={{ color: rulesPassword.number ? "lightgreen" : "#ff6b6b" }}>
                • Número
            </Box>
            <Box style={{ color: rulesPassword.specialChar ? "lightgreen" : "#ff6b6b" }}>
                • Caractere especial
            </Box>
        </Box>
    )

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
                                placeholder="CPF"
                                limit={11}
                                error={error && CPF === ""}
                                value={CPF}
                                onChange={(e) => (
                                    setCPF(e.target.value),
                                    setError(false),
                                    setOpenForgotDialog(false)
                                )}
                            />

                            <InputUI
                                type="password"
                                error={error && password === ""}
                                showPasswordToggle={true}
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

                            <LinkUI onClick={() => setOpenForgotDialog(true)}>
                                Esqueceu a senha ?
                            </LinkUI>

                            <DialogUI
                                open={openForgotDialog}
                                onClose={() => setOpenForgotDialog(false)}
                                title={"Trocar de senha"}
                                onConfirm={() => {
                                    handleChangeCodePassword();
                                    setOpenForgotDialog(false);
                                }}
                            >
                                <Box display="flex" gap={2} alignItems="center">
                                    <InputUI
                                        style={{ flex: 1 }}
                                        type="text"
                                        placeholder="Digite seu email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                    />

                                    <ButtonUI
                                        onClick={handleValidateCodePassword}
                                    >
                                        Enviar Código
                                    </ButtonUI>
                                </Box>

                                {sendEmail && <p>Foi enviado para seu email um código de verificação.</p>}

                                <InputUI
                                    type="text"
                                    placeholder="Digite o código"
                                    value={code}
                                    onChange={(e) => setCode(e.target.value)}

                                />
                                <Tooltip title={titlePasswordTooltip} placement="right" arrow>
                                    <InputUI
                                        type="password"
                                        placeholder="Digite sua nova senha"
                                        showPasswordToggle={true}
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}

                                    />
                                </Tooltip>
                            </DialogUI>

                            <DialogUI
                                open={openLoginDialog}
                                onClose={() => setOpenLoginDialog(false)}
                                title={"Digite seu código"}
                                onConfirm={() => {
                                    if (!code) {
                                        alert("Digite o código");
                                        return;
                                    }
                                    hadleValidateCode();
                                    setOpenLoginDialog(false);
                                }}
                            >
                                <p>Foi enviado para seu email um código de verificação.</p>

                                <InputUI
                                    type="text"
                                    placeholder="Digite o código"
                                    value={code}
                                    onChange={(e) => setCode(e.target.value)}

                                />
                            </DialogUI>

                            <Link to="/register" >
                                Não tem conta ? Clique aqui.
                            </Link>

                        </Box>
                    </Paper >
                </Grid >
            </Grid>
        </Box >
    )

}