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
import { useAlert } from "../../hooks/useAlert";

export default function Login() {
    const { showAlert } = useAlert();

    const [openForgotDialog, setOpenForgotDialog] = useState(false);
    const [openLoginDialog, setOpenLoginDialog] = useState(false);

    const [error, setError] = useState(false);
    const [errorCode, setErrorCode] = useState(false);
    const [password, setPassword] = useState("");
    const [CPF, setCPF] = useState("");
    const [code, setCode] = useState("");
    const navigate = useNavigate();

    // estado exclusivo do dialog de troca de senha
    const [forgotError, setForgotError] = useState(false);
    const [forgotErrorCode, setForgotErrorCode] = useState(false);
    const [forgotEmailError, setForgotEmailError] = useState(false);
    const [forgotEmailConfirm, setForgotEmailConfirm] = useState(false);

    const [forgotEmail, setForgotEmail] = useState("");
    const [forgotCode, setForgotCode] = useState("");
    const [forgotNewPassword, setForgotNewPassword] = useState("");

    const canLogin = () => {
        if (CPF === "" && password === "") {
            setError(true);
            showAlert("error", "Preencha todos os campos");
            return false;
        }

        if (!isValidCpf(CPF)) {
            setError(true);
            showAlert("error", "CPF incorreto");
            return false;
        }

        setError(false);
        // showAlert("success", "Sucesso");
        return true;

    };

    async function handleLogin() {
        if (!canLogin()) return;

        const data = {
            cpf: CPF,
            senha: password
        };

        try {
            setOpenLoginDialog(true);
            showAlert("success", "Um código foi enviado para seu email");
            await login(data);

        } catch (error) {
            setError(true);
            showAlert("error", "CPF ou senha inválidos");
            console.error(error);
        }
    }

    async function hadleValidateCode() {

        if (code === "") {
            setError(true);
            showAlert("error", "Coloque o código enviado por email");
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
            showAlert("error", "Código inválido");
        }
    }

    //Troca de senha

    const canChangePassword = (code, newPassword) => {

        if (!code || !newPassword) {
            setForgotError(true);
            showAlert("error", "Preencha todos os campos");
            return;
        }

        if (!rulesPassword.isValid) {
            setForgotError(true);
            showAlert("error", "Verifique as regras de senha");
            return false;
        }

        setForgotError(false);
        return true;

    };

    const sendEmail = (email) => {
        if (!email) {
            setForgotEmailError(true);
            showAlert("error", "Preencha o email");
            return;
        }

        if (!isValidEmail(email)) {
            setForgotEmailError(true);
            showAlert("error", "Email incorreto");
            return false;
        }

        return true;
    };

    async function handleValidateCodePassword(emailParam) {
        if (!sendEmail(emailParam)) return;

        const data = { email: emailParam };

        try {
            showAlert("success", "Um código foi enviado para seu email");
            setForgotEmailError(true);
            setForgotEmailConfirm(true);
            await validadeCodePassword(data);
        } catch (error) {
            console.log(error);
            setForgotEmailError(true);
            showAlert("error", "Erro ao Enviar o código");
            setForgotEmailConfirm(false);
        }
    }

    async function handleChangeCodePassword({ code, newPassword }) {

        if (!canChangePassword(code, newPassword)) return;

        const data = {
            codigo: code,
            novaSenha: newPassword
        };

        try {
            await changePassword(data);
            setOpenForgotDialog(false);
            setForgotError(true);
            setForgotEmailConfirm(false);
            showAlert("success", "Senha alterada com sucesso");

        } catch (error) {
            console.log(error)
            setForgotError(true);
            showAlert("error", "Erro ao trocar senha");
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

    return (
        <Box>
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
                                    setForgotEmailError(false),
                                    setForgotEmailConfirm(false),
                                    setForgotCode(""),
                                    setForgotEmail(""),
                                    setForgotNewPassword("")
                                )}
                                onConfirm={() => {
                                    handleChangeCodePassword({
                                        code: forgotCode,
                                        newPassword: forgotNewPassword
                                    });
                                }}
                            >
                                {!forgotEmailConfirm && <Box display="flex" gap={2} alignItems="center">
                                    <InputUI
                                        label="Email"
                                        style={{ flex: 1 }}
                                        error={forgotEmailError && (forgotEmail === "" || !isValidEmail(forgotEmail))}
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
                                </Box>}

                                {forgotEmailConfirm && <InputUI
                                    label="Código"
                                    error={forgotError && (forgotCode === "" || forgotErrorCode)}
                                    type="text"
                                    placeholder="Digite o código"
                                    value={forgotCode}
                                    onChange={(e) => setForgotCode(e.target.value)}
                                />}

                                {forgotEmailConfirm && <Tooltip title={forgotTitlePasswordTooltip} placement="right" arrow>
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
                                </Tooltip>}

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
                                        showAlert("error", "Erro ao trocar senha");

                                        // setErrorMessage("Digite o código verificador");
                                        // setTypeError("error");
                                        return;
                                    }
                                    hadleValidateCode();
                                }}
                            >

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