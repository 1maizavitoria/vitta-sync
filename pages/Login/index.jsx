import { Box, Container, FormControlLabel, FormLabel, IconButton, Paper, Radio, RadioGroup, Stack, Tooltip, Typography } from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import MonitorHeartOutlinedIcon from "@mui/icons-material/MonitorHeartOutlined";
import { useTheme } from "@mui/material/styles";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import ButtonUI from "../../components/ui/Button";
import DialogUI from "../../components/ui/Dialog";
import InputUI from "../../components/ui/Input";
import LinkUI from "../../components/ui/Link";
import PasswordTooltip from "../../components/ui/Tooltip";
import { useAlert } from "../../hooks/useAlert";
import { useI18n } from "../../src/i18n";
import { changePassword, login, validadeCode, validadeCodePassword } from "../../services/userService"
import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF";
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { isTokenExpired } from "../../utils/auth/auth";
import { validatePassword } from "../../utils/validators/passwordValidator";

export default function Login() {
    const { showAlert } = useAlert();
    const { t } = useI18n();
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";

    const [channel, setChannel] = useState("email");
    const [openForgotDialog, setOpenForgotDialog] = useState(false);
    const [openLoginDialog, setOpenLoginDialog] = useState(false);

    const [password, setPassword] = useState("");
    const [CPF, setCPF] = useState("");
    const [code, setCode] = useState("");

    const [errorCode, setErrorCode] = useState(false);
    const [errorCPF, setErrorCPF] = useState(false);
    const [errorPassword, setErrorPassword] = useState(false);
    const [errorLoginCode, setErrorLoginCode] = useState(false);

    const [loadingLogin, setLoadingLogin] = useState(false);
    const [loadingValidateCode, setLoadingValidateCode] = useState(false);
    const [loadingForgotPassword, setLoadingForgotPassword] = useState(false);
    const [loadingChangePassword, setLoadingChangePassword] = useState(false);

    const [forgotError, setForgotError] = useState(false);
    const [forgotErrorCode, setForgotErrorCode] = useState(false);
    const [forgotEmailError, setForgotEmailError] = useState(false);
    const [forgotEmailConfirm, setForgotEmailConfirm] = useState(false);
    const [forgotEmail, setForgotEmail] = useState("");
    const [forgotCode, setForgotCode] = useState("");
    const [forgotNewPassword, setForgotNewPassword] = useState("");

    const [seconds, setSeconds] = useState(0);
    const disabled = seconds > 0;

    const rulesPassword = validatePassword(forgotNewPassword);
    const navigate = useNavigate();

    const canLogin = () => {
        if (CPF === "" || password === "") {
            setErrorCPF(CPF === "");
            setErrorPassword(password === "");
            showAlert("error", t("messages.fillAll"));
            return false;
        }

        if (!isValidCpf(CPF)) {
            setErrorCPF(true);
            showAlert("error", t("messages.wrongCpf"));
            return false;
        }

        setErrorCPF(false);
        setErrorPassword(false);
        return true;
    };

    function getChannelMessage(selectedChannel) {
        switch (selectedChannel) {
            case "sms":
                return t("messages.codeBySms");
            case "ambos":
                return t("messages.codeByBoth");
            default:
                return t("messages.codeByEmail");
        }
    }

    function handleLogin() {
        if (!canLogin()) return;
        if (loadingLogin) return;

        setLoadingLogin(true);

        const data = { cpf: CPF, senha: password, canal: channel };

        login(data)
            .then(() => {
                setOpenLoginDialog(true);
                showAlert("success", getChannelMessage(channel));
            })
            .catch((error) => {
                showAlert("error", t("messages.invalidLogin"));
                console.error(error);
            })
            .finally(() => {
                setLoadingLogin(false);
            });
    }

    async function handleValidateCode() {
        if (loadingValidateCode) return;

        if (code === "") {
            setErrorLoginCode(true);
            showAlert("error", t("messages.typeSentCode"));
            return;
        }

        setLoadingValidateCode(true);

        try {
            const response = await validadeCode({ codigo: code });
            const token = response;

            localStorage.setItem("token", token);
            localStorage.setItem("CPF", CPF);
            setOpenLoginDialog(false);
            const redirect = new URLSearchParams(location.search).get("redirect");

            navigate(redirect || "/dashboard");
        } catch (error) {
            console.log(error);
            setErrorCode(true);
            setErrorLoginCode(true);
            showAlert("error", t("messages.invalidCode"));
        } finally {
            setLoadingValidateCode(false);
        }
    }

    async function handleResendCode() {
        if (!canLogin()) return;

        const data = {
            cpf: CPF,
            senha: password,
            canal: channel
        };

        try {
            showAlert("success", getChannelMessage(channel));
            setSeconds(10);
            await login(data);
        } catch (error) {
            setErrorCPF(true);
            setErrorPassword(true);
            showAlert("error", t("messages.invalidLogin"));
            console.error(error);
        }
    }

    const sendEmail = (email) => {
        if (!email) {
            setForgotEmailError(true);
            showAlert("error", t("messages.fillEmail"));
            return false;
        }

        if (!isValidEmail(email)) {
            setForgotEmailError(true);
            showAlert("error", t("messages.invalidEmail"));
            return false;
        }

        return true;
    };

    async function handleValidateCodePassword(emailParam) {
        if (loadingForgotPassword) return;
        if (!sendEmail(emailParam)) return;

        setLoadingForgotPassword(true);

        const data = { email: emailParam, canal: "email" };

        try {
            await validadeCodePassword(data);
            showAlert("success", t("messages.codeByEmail"));
            setForgotEmailError(false);
            setForgotEmailConfirm(true);
        } catch (error) {
            console.log(error);
            setForgotEmailError(true);
            showAlert("error", t("messages.codeSendError"));
            setForgotEmailConfirm(false);
        } finally {
            setLoadingForgotPassword(false);
        }
    }

    const canChangePassword = (nextCode, newPassword) => {
        if (!nextCode || !newPassword) {
            setForgotError(true);
            showAlert("error", t("messages.fillAll"));
            return false;
        }

        if (!rulesPassword.isValid) {
            setForgotError(true);
            showAlert("error", t("messages.passwordRules"));
            return false;
        }

        setForgotError(false);
        return true;
    };

    async function handleChangeCodePassword({ code: nextCode, newPassword }) {
        if (loadingChangePassword) return;
        if (!canChangePassword(nextCode, newPassword)) return;

        setLoadingChangePassword(true);

        const data = {
            codigo: nextCode,
            novaSenha: newPassword
        };

        try {
            await changePassword(data);

            setOpenForgotDialog(false);
            setForgotError(false);
            setForgotErrorCode(false);
            setForgotEmailError(false);
            setForgotEmailConfirm(false);
            setForgotCode("");
            setForgotEmail("");
            setForgotNewPassword("");

            showAlert("success", t("messages.passwordChanged"));
        } catch (error) {
            console.log(error)
            setForgotError(true);
            showAlert("error", t("messages.passwordChangeError"));
        } finally {
            setLoadingChangePassword(false);
        }
    }

    useEffect(() => {
        if (seconds <= 0) return;

        const timer = setTimeout(() => {
            setSeconds((prev) => prev - 1);
        }, 1000);

        return () => clearTimeout(timer);
    }, [seconds]);

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token && !isTokenExpired(token)) {
            navigate("/dashboard");
        }
    }, [navigate]);

    return (
        <Container maxWidth="lg">
            <Box
                sx={{
                    minHeight: "calc(100vh - 150px)",
                    display: "grid",
                    gridTemplateColumns: { xs: "1fr", md: "0.9fr 1.1fr" },
                    alignItems: "center",
                    gap: { xs: 4, md: 7 },
                    py: { xs: 4, md: 6 }
                }}
            >
                <Box sx={{ display: { xs: "none", md: "block" } }}>
                    <Box
                        sx={{
                            width: 64,
                            height: 64,
                            borderRadius: 3,
                            display: "grid",
                            placeItems: "center",
                            color: "#ffffff",
                            background: "linear-gradient(135deg, #16a34a 0%, #0f766e 72%, #0ea5e9 100%)",
                            mb: 3
                        }}
                    >
                        <MonitorHeartOutlinedIcon sx={{ fontSize: 34 }} />
                    </Box>
                    <Typography sx={{ fontSize: { md: 44, lg: 54 }, lineHeight: 1.05, fontWeight: 800, mb: 2 }}>
                        {t("auth.loginTitle")}
                    </Typography>
                    <Typography sx={{ color: "text.secondary", fontSize: 18, lineHeight: 1.7, maxWidth: 460 }}>
                        {t("auth.loginSubtitle")}
                    </Typography>
                </Box>

                <Paper
                    elevation={0}
                    sx={{
                        width: "100%",
                        maxWidth: 460,
                        justifySelf: "center",
                        borderRadius: 3,
                        p: { xs: 2.5, sm: 4 },
                        border: "1px solid",
                        borderColor: vitta.border,
                        bgcolor: "background.paper",
                        boxShadow: vitta.shadow
                    }}
                >
                    <IconButton
                        aria-label="Voltar para início"
                        onClick={() => navigate("/")}
                        sx={{
                            mb: 1,
                            color: "primary.main",
                            bgcolor: isDark ? "rgba(34, 197, 94, 0.12)" : "rgba(22, 163, 74, 0.08)",
                            "&:hover": {
                                bgcolor: isDark ? "rgba(34, 197, 94, 0.18)" : "rgba(22, 163, 74, 0.14)"
                            }
                        }}
                    >
                        <ArrowBackIcon />
                    </IconButton>

                    <Stack alignItems="center" spacing={1} sx={{ mb: 2 }}>
                        <Box
                            sx={{
                                width: 48,
                                height: 48,
                                borderRadius: 2,
                                display: "grid",
                                placeItems: "center",
                                color: "primary.main",
                                bgcolor: isDark ? "rgba(34, 197, 94, 0.14)" : "rgba(22, 163, 74, 0.12)"
                            }}
                        >
                            <LockOutlinedIcon />
                        </Box>
                        <Typography sx={{ fontWeight: 800, fontSize: 26 }}>
                            {t("brand")}
                        </Typography>
                        <Typography sx={{ color: "text.secondary", textAlign: "center" }}>
                            {t("auth.loginSubtitle")}
                        </Typography>
                    </Stack>

                    <InputUI
                        label={t("auth.cpf")}
                        placeholder={t("auth.placeholders.cpf")}
                        limit={14}
                        error={errorCPF}
                        value={formatCPF(CPF)}
                        onChange={(e) => (
                            setCPF(e.target.value.replace(/\D/g, "")),
                            setErrorCPF(false),
                            setOpenForgotDialog(false)
                        )}
                    />

                    <InputUI
                        label={t("auth.password")}
                        placeholder={t("auth.placeholders.password")}
                        type="password"
                        error={errorPassword}
                        showPasswordToggle={true}
                        value={password}
                        onChange={(e) => (
                            setPassword(e.target.value),
                            setErrorPassword(false)
                        )}
                    />

                    <Box width="100%" sx={{ mt: 1 }}>
                        <FormLabel sx={{ fontSize: 14, color: "text.secondary", fontWeight: 600 }}>
                            {t("auth.receiveCodeBy")}
                        </FormLabel>

                        <RadioGroup row value={channel} onChange={(e) => setChannel(e.target.value)}>
                            <FormControlLabel value="email" control={<Radio size="small" />} label="Email" />
                            <FormControlLabel value="sms" control={<Radio size="small" />} label="SMS" />
                        </RadioGroup>
                    </Box>

                    <ButtonUI onClick={handleLogin} disabled={loadingLogin} sx={{ width: "100%", mt: 2 }}>
                        {loadingLogin ? t("auth.loggingIn") : t("auth.loginAction")}
                    </ButtonUI>

                    <Stack spacing={1.5} alignItems="center" sx={{ mt: 2 }}>
                        <LinkUI onClick={() => setOpenForgotDialog(true)} variant="action">
                            {t("auth.forgotPassword")}
                        </LinkUI>

                        <Typography sx={{ fontSize: 14, color: "text.secondary" }}>
                            {t("auth.noAccount")}
                            <LinkUI to="/register" variant="action">
                                {t("auth.registerAction")}
                            </LinkUI>
                        </Typography>
                    </Stack>

                    <DialogUI
                        title={t("auth.changePassword")}
                        disabledClose={loadingForgotPassword || loadingChangePassword}
                        disabledConfirm={loadingChangePassword || !forgotEmailConfirm}
                        open={openForgotDialog}
                        confirmText={loadingChangePassword ? t("auth.changing") : t("auth.confirm")}
                        onClose={() => (
                            setOpenForgotDialog(false),
                            setForgotError(false),
                            setForgotErrorCode(false),
                            setForgotEmailError(false),
                            setForgotEmailConfirm(false),
                            setForgotCode(""),
                            setForgotEmail(""),
                            setForgotNewPassword("")
                        )}
                        onConfirm={() => {
                            handleChangeCodePassword({
                                code: forgotCode,
                                newPassword: forgotNewPassword,
                            });
                        }}
                    >
                        {!forgotEmailConfirm && (
                            <Box sx={{ display: "flex", flexDirection: { xs: "column", sm: "row" }, gap: 2, alignItems: "center" }}>
                                <InputUI
                                    label={t("auth.email")}
                                    style={{ flex: 1 }}
                                    error={forgotEmailError && (forgotEmail === "" || !isValidEmail(forgotEmail))}
                                    type="text"
                                    placeholder={t("auth.placeholders.email")}
                                    value={forgotEmail}
                                    onChange={(e) => setForgotEmail(e.target.value)}
                                />

                                <ButtonUI disabled={loadingForgotPassword} onClick={() => handleValidateCodePassword(forgotEmail)}>
                                    {loadingForgotPassword ? t("auth.sending") : t("auth.sendCode")}
                                </ButtonUI>
                            </Box>
                        )}

                        {forgotEmailConfirm && (
                            <InputUI
                                label={t("auth.code")}
                                error={forgotError && (forgotCode === "" || forgotErrorCode)}
                                type="text"
                                placeholder={t("auth.placeholders.code")}
                                value={forgotCode}
                                onChange={(e) => setForgotCode(e.target.value)}
                            />
                        )}

                        {forgotEmailConfirm && (
                            <Tooltip title={<PasswordTooltip rules={rulesPassword} />} placement="right" arrow>
                                <InputUI
                                    label={t("auth.newPassword")}
                                    error={forgotError && (forgotNewPassword === "" || !rulesPassword.isValid)}
                                    type="password"
                                    placeholder={t("auth.placeholders.newPassword")}
                                    showPasswordToggle={true}
                                    value={forgotNewPassword}
                                    onChange={(e) => setForgotNewPassword(e.target.value)}
                                />
                            </Tooltip>
                        )}
                    </DialogUI>

                    <DialogUI
                        disabledClose={loadingValidateCode}
                        disabledConfirm={loadingValidateCode}
                        confirmText={loadingValidateCode ? t("auth.validating") : t("auth.confirm")}
                        open={openLoginDialog}
                        onClose={() => (
                            setOpenLoginDialog(false),
                            setErrorLoginCode(false),
                            setErrorCode(false),
                            setCode("")
                        )}
                        title={t("auth.typeCode")}
                        onConfirm={() => {
                            if (!code) {
                                setErrorLoginCode(true);
                                showAlert("error", t("messages.typeSentCode"));
                                return;
                            }

                            handleValidateCode();
                        }}
                    >
                        <Box sx={{ display: "flex", flexDirection: { xs: "column", sm: "row" }, gap: 2, alignItems: "center" }}>
                            <InputUI
                                type="text"
                                error={errorLoginCode || errorCode}
                                placeholder={t("auth.placeholders.code")}
                                value={code}
                                onChange={(e) => {
                                    setCode(e.target.value);
                                    setErrorLoginCode(false);
                                    setErrorCode(false);
                                }}
                            />

                            <ButtonUI minWidth="180px" disabled={disabled} onClick={handleResendCode}>
                                {disabled ? `${t("auth.wait")} ${seconds}s` : t("auth.resendCode")}
                            </ButtonUI>
                        </Box>
                    </DialogUI>
                </Paper>
            </Box>
        </Container>
    )
}
