import { Box, Button, Grid, Paper, Tooltip, Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import Link from "../../components/ui/Link";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import InputUI from "../../components/ui/Input";
import DialogUI from "../../components/ui/Dialog";
import AlertUI from "../../components/ui/Alert";
import LinkUI from "../../components/ui/Link";
import { login, validadeCode, validadeCodePassword, changePassword } from "../../services/userService"
import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF";
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { useAlert } from "../../hooks/useAlert";

import { validatePassword } from "../../utils/validators/passwordValidator";
import PasswordTooltip from "../../components/ui/Tooltip";

export default function Login() {
    const { showAlert } = useAlert();

    const [openForgotDialog, setOpenForgotDialog] = useState(false);
    const [openLoginDialog, setOpenLoginDialog] = useState(false);

    const [error, setError] = useState(false);
    const [errorCode, setErrorCode] = useState(false);
    const [password, setPassword] = useState("");
    const [CPF, setCPF] = useState("");
    const [code, setCode] = useState("");

    // estado exclusivo do dialog de troca de senha
    const [forgotError, setForgotError] = useState(false);
    const [forgotErrorCode, setForgotErrorCode] = useState(false);
    const [forgotEmailError, setForgotEmailError] = useState(false);
    const [forgotEmailConfirm, setForgotEmailConfirm] = useState(false);

    const [forgotEmail, setForgotEmail] = useState("");
    const [forgotCode, setForgotCode] = useState("");
    const [forgotNewPassword, setForgotNewPassword] = useState("");

    // Estado para controlar o tempo de reenvio do código
    const [seconds, setSeconds] = useState(0);
    const disabled = seconds > 0;


    // Regras de validação da senha para o tooltip
    const rulesPassword = validatePassword(forgotNewPassword);

    const navigate = useNavigate();

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
            setOpenLoginDialog(false);
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
            const response = await validadeCode(data);
            const token = response;

            localStorage.setItem("token", token);
            localStorage.setItem("CPF", CPF);
            setOpenLoginDialog(false);
            navigate("/dashboard");
        } catch (error) {
            console.log(error);
            setErrorCode(true);
            setError(true);
            showAlert("error", "Código inválido");
        }
    }

    async function handleResendCode() {

        if (!canLogin()) return;

        const data = {
            cpf: CPF,
            senha: password
        };

        try {
            showAlert("success", "Um código foi enviado para seu email");
            setSeconds(10);
            await login(data);

        } catch (error) {
            setError(true);
            showAlert("error", "CPF ou senha inválidos");
            console.error(error);
        }

    }

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

            setOpenForgotDialog(false);
            setForgotError(false);
            setForgotErrorCode(false);
            setForgotError(false);
            setForgotEmailError(false);
            setForgotEmailConfirm(false);
            setForgotCode("");
            setForgotEmail("");
            setForgotNewPassword("");

        } catch (error) {
            console.log(error)
            setForgotError(true);
            showAlert("error", "Erro ao trocar senha");
        }
    }

    useEffect(() => {
        if (seconds <= 0) return;

        const timer = setTimeout(() => {
            setSeconds((prev) => prev - 1);
        }, 1000);

        return () => clearTimeout(timer);

    }, [seconds]);

    return (
        <Box>
            <Grid
                container
                justifyContent="center"
                alignItems="center"
            >
                <Grid item>

                    <Paper elevation={5} sx={{ p: 4, width: 360, }}>
                        <Box
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            gap={2}

                        >
                            <Box textAlign="center" mb={2}>
                                <Typography
                                    sx={{
                                        fontFamily: 'Inter, sans-serif',
                                        fontWeight: 600,
                                        fontSize: '28px',
                                        color: '#1a1a1a',
                                        letterSpacing: '0.5px',
                                    }}
                                >
                                    Vitta<span style={{ fontWeight: 400 }}>Sync</span>
                                </Typography>

                                <Typography
                                    sx={{
                                        fontFamily: 'Inter, sans-serif',
                                        fontWeight: 500,
                                        fontSize: '20px',
                                        color: '#4a4a4a',
                                        mt: 0.5,
                                    }}
                                >
                                    Login
                                </Typography>
                            </Box>

                            <InputUI
                                label="CPF"
                                placeholder="999.999.999-99"
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
                                label="Senha"
                                placeholder="Digite sua senha"
                                type="password"
                                error={error && password === ""}
                                showPasswordToggle={true}
                                value={password}
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
                                variant="action"
                            >
                                Esqueceu a senha ?
                            </LinkUI>

                            <p style={{ fontFamily: 'Inter, sans-serif', fontSize: '14px' }}>
                                Não tem conta?
                                <LinkUI to="/register" variant="action">
                                    Cadastrar
                                </LinkUI>
                            </p>

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
                                disabledConfirm={!forgotEmailConfirm}
                                onConfirm={() => {
                                    handleChangeCodePassword({
                                        code: forgotCode,
                                        newPassword: forgotNewPassword,
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

                                {forgotEmailConfirm && <Tooltip
                                    title={<PasswordTooltip rules={rulesPassword} />}
                                    placement="right"
                                    arrow
                                >
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
                                        return;
                                    }
                                    hadleValidateCode();
                                }}
                            >
                                <Box display="flex" gap={2} alignItems="center">
                                    <InputUI
                                        type="text"
                                        error={error && (!code || errorCode)}
                                        placeholder="Digite o código"
                                        value={code}
                                        onChange={(e) => setCode(e.target.value)}

                                    />

                                    <ButtonUI
                                        minWidth="240px"
                                        disabled={disabled}
                                        onClick={handleResendCode}

                                    >
                                        {disabled ? `Aguarde ${seconds}s` : "Reenviar Código"}
                                    </ButtonUI>

                                </Box>
                            </DialogUI>

                        </Box>
                    </Paper >
                </Grid >
            </Grid>
        </Box >
    )

}