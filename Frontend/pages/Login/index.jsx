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
import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF";
import { isValidEmail } from "../../utils/formatters/formatEmail";

export default function Login() {

    const [openForgotDialog, setOpenForgotDialog] = useState(false);
    const [openLoginDialog, setOpenLoginDialog] = useState(false);

    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [typeError, setTypeError] = useState("error");
    const [errorCode, setErrorCode] = useState(false);
    const [password, setPassword] = useState("");
    const [CPF, setCPF] = useState("");
    const [code, setCode] = useState("");
    const navigate = useNavigate();

    // estado exclusivo do dialog de troca de senha
    const [forgotError, setForgotError] = useState(false);
    const [forgotErrorMessage, setForgotErrorMessage] = useState("");
    const [forgotTypeError, setForgotTypeError] = useState("error");
    const [forgotErrorCode, setForgotErrorCode] = useState(false);

    const [forgotEmail, setForgotEmail] = useState("");
    const [forgotCode, setForgotCode] = useState("");
    const [forgotNewPassword, setForgotNewPassword] = useState("");

    const canLogin = () => {
        if (CPF === "" && password === "") {
            setError(true);
            setErrorMessage("Preencha todos os campos");
            setTypeError("error");
            return false;
        }

        if (!isValidCpf(CPF)) {
            setError(true);
            setErrorMessage("CPF incorreto");
            setTypeError("error");
            return false;
        }

        setError(false);
        setErrorMessage("Sucesso");
        setTypeError("success");
        return true;

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
            setError(true);
            setTypeError("error");
            setErrorMessage("CPF ou senha inválidos");
            console.error(error);
        }
    }

    async function hadleValidateCode() {

        if (code === "") {
            setError(true);
            setTypeError("error");
            setErrorMessage("Coloque o código enviado por email");
            return;
        }

        const data = {
            codigo: code,
        };

        try {
            await validadeCode(data);
            setOpenLoginDialog(false);
            navigate("/home");
        } catch (error) {
            console.log(error);
            setErrorCode(true);
            setError(true);
            setTypeError("error");
            setErrorMessage("Código inválido");
        }
    }

    //Troca de senha

    const canChangePassword = (email, code, newPassword) => {
        if (!email || !code || !newPassword) {
            setForgotError(true);
            setForgotErrorMessage("Preencha todos os campos");
            return;
        }

        if (!isValidEmail(email)) {
            setForgotError(true);
            setForgotTypeError("error");
            setForgotErrorMessage("Email incorreto");
            return false;
        }

        if (!rulesPassword.isValid) {
            setForgotError(true);
            setForgotTypeError("error");
            setForgotErrorMessage("Verifique as regras de senha");
            return false;
        }

        // if (code) {
        //     setForgotErrorCode(true);
        //     setForgotError(true);
        //     setForgotTypeError("error");
        //     setForgotErrorMessage("Código errado");
        //     return false;
        // }

        setForgotError(false);
        setForgotTypeError("success");
        setForgotErrorMessage("Sucesso");
        return true;

    };

    async function handleValidateCodePassword(emailParam) {
        const data = { email: emailParam };

        try {
            await validadeCodePassword(data);
            setForgotError(true);
            setForgotTypeError("success");
            setForgotErrorMessage("Um código foi enviado para seu email");
        } catch (error) {
            console.log(error);
            setForgotError(true);
            setForgotTypeError("error");
            setForgotErrorMessage("Erro ao Enviar o código");
        }
    }

    async function handleChangeCodePassword({ email, code, newPassword }) {

        if (!canChangePassword(email, code, newPassword)) return;

        const data = {
            codigo: code,
            novaSenha: newPassword
        };

        try {
            await changePassword(data);
            setOpenForgotDialog(false);
            setForgotError(true);
            setForgotTypeError("success");
            setForgotErrorMessage("Senha alterada com sucesso");

        } catch (error) {
            console.log(error)
            setForgotError(true);
            setForgotTypeError("error");
            setForgotErrorMessage("Erro ao trocar senha");
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

    const rulesPassword = validatePassword(forgotNewPassword);

    const forgotTitlePasswordTooltip = (
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

    const activeError = forgotError || error;

    const activeMessage = forgotError
        ? forgotErrorMessage
        : errorMessage;

    const activeType = forgotError
        ? forgotTypeError
        : typeError;

    return (
        <Box>
            {activeError && (
                <AlertUI
                    type={activeType}
                    message={activeMessage}
                />
            )}

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
                                limit={14}
                                error={error && (CPF === "" || !isValidCpf(CPF))}
                                value={formatCPF(CPF)}
                                onChange={(e) => (
                                    setCPF(e.target.value.replace(/\D/g, "")),
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

                            <LinkUI onClick={() => (
                                setOpenForgotDialog(true),
                                setError(false)
                            )}
                            >
                                Esqueceu a senha ?
                            </LinkUI>

                            <Link to="/register" >
                                Não tem conta ? Clique aqui.
                            </Link>

                            {/* Diálogo pra varificação de 2 fatores na trocar de senha */}
                            <DialogUI
                                title={"Trocar de senha"}
                                open={openForgotDialog}
                                onClose={() => (
                                    setOpenForgotDialog(false),
                                    setForgotError(false),
                                    setForgotErrorCode(false),
                                    setForgotError(false),
                                    setForgotCode(""),
                                    setForgotEmail(""),
                                    setForgotNewPassword("")
                                )}
                                onConfirm={() => {
                                    handleChangeCodePassword({
                                        email: forgotEmail,
                                        code: forgotCode,
                                        newPassword: forgotNewPassword
                                    });
                                }}
                            >
                                <Box display="flex" gap={2} alignItems="center">
                                    <InputUI
                                        label="Email"
                                        style={{ flex: 1 }}
                                        error={forgotError && (forgotEmail === "" || !isValidEmail(forgotEmail))}
                                        type="text"
                                        placeholder="Digite seu email"
                                        value={forgotEmail}
                                        onChange={(e) => setForgotEmail(e.target.value)}
                                    />

                                    <ButtonUI
                                        onClick={() => handleValidateCodePassword(forgotEmail)}
                                    >
                                        Enviar Código
                                    </ButtonUI>
                                </Box>

                                <InputUI
                                    label="Código"
                                    error={forgotError && (forgotCode === "" || forgotErrorCode)}
                                    type="text"
                                    placeholder="Digite o código"
                                    value={forgotCode}
                                    onChange={(e) => setForgotCode(e.target.value)}
                                />

                                <Tooltip title={forgotTitlePasswordTooltip} placement="right" arrow>
                                    <InputUI
                                        label="Nova senha"
                                        error={forgotError && (forgotNewPassword === "" || !rulesPassword.isValid)}
                                        type="password"
                                        placeholder="Digite sua nova senha"
                                        showPasswordToggle={true}
                                        value={forgotNewPassword}
                                        onChange={(e) => {
                                            setForgotNewPassword(e.target.value)
                                        }}
                                    />
                                </Tooltip>
                            </DialogUI>

                            {/* Diálogo para de verificação de 2 fatores no login */}
                            <DialogUI
                                open={openLoginDialog}
                                onClose={() => (
                                    setOpenLoginDialog(false),
                                    setError(false),
                                    setCode("")
                                )}
                                title={"Digite seu código"}
                                onConfirm={() => {
                                    if (!code) {
                                        setError(true);
                                        setErrorMessage("Digite o código verificador");
                                        setTypeError("error");
                                        return;
                                    }
                                    hadleValidateCode();
                                }}
                            >
                                <p>Foi enviado um código de verificação para seu email .</p>

                                <InputUI
                                    type="text"
                                    error={error && (!code || errorCode)}
                                    placeholder="Digite o código"
                                    value={code}
                                    onChange={(e) => setCode(e.target.value)}

                                />
                            </DialogUI>

                        </Box>
                    </Paper >
                </Grid >
            </Grid>
        </Box >
    )

}