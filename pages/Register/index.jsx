import { Box, Container, IconButton, Paper, Stack, Tooltip, Typography } from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import PersonAddAltOutlinedIcon from "@mui/icons-material/PersonAddAltOutlined";
import MonitorHeartOutlinedIcon from "@mui/icons-material/MonitorHeartOutlined";
import { useTheme } from "@mui/material/styles";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import AutocompleteUI from "../../components/ui/Autocomplete";
import ButtonUI from "../../components/ui/Button";
import DatePickerUI from "../../components/ui/DatePicker";
import InputUI from "../../components/ui/Input";
import LinkUI from "../../components/ui/Link";
import PasswordTooltip from "../../components/ui/Tooltip";
import { useAlert } from "../../hooks/useAlert";
import { useI18n } from "../../src/i18n";
import { createUser } from "../../services/userService";
import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF"
import { formatPhone, isValidPhone } from "../../utils/formatters/formatPhone";
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { getDateLimit, isUnder18 } from "../../utils/validators/dateValidator";
import { isTokenExpired } from "../../utils/auth/auth";
import { validatePassword } from "../../utils/validators/passwordValidator";

export default function Register() {
    const { showAlert } = useAlert();
    const { t } = useI18n();
    const theme = useTheme();
    const vitta = theme.vitta;
    const isDark = theme.palette.mode === "dark";

    const [errorName, setErrorName] = useState(false);
    const [errorCPF, setErrorCPF] = useState(false);
    const [errorEmail, setErrorEmail] = useState(false);
    const [errorUserType, setErrorUserType] = useState(false);
    const [errorBirthDate, setErrorBirthDate] = useState(false);
    const [errorPassword, setErrorPassword] = useState(false);
    const [errorRepeatPassword, setErrorRepeatPassword] = useState(false);
    const [errorAdvice, setErrorAdvice] = useState(false);
    const [errorPhone, setErrorPhone] = useState(false);
    const [errorWeight, setErrorWeight] = useState(false);
    const [errorHeight, setErrorHeight] = useState(false);

    const [loading, setLoading] = useState(false);

    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [CPF, setCPF] = useState("");
    const [advice, setAdvice] = useState("");
    const [userType, setUserType] = useState(null);
    const [birthDate, setBirthDate] = useState(null);
    const [email, setEmail] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [dateLimit, setDateLimit] = useState();
    const [phone, setPhone] = useState("");
    const [initialWeight, setInitialWeight] = useState("");
    const [height, setHeight] = useState("");

    const navigate = useNavigate();

    const rulesPassword = validatePassword(password);
    const repeatRulesPassword = validatePassword(repeatPassword);

    const userTypeOptions = [
        { value: "paciente", label: t("auth.patient") },
        { value: "responsavel", label: t("auth.responsible") },
        { value: "saude", label: t("auth.healthProfessional") }
    ];

    const fieldSx = {
        width: "100%",
        minWidth: 0
    };

    function isValidPositiveNumber(value) {
        return !isNaN(value) && Number(value) > 0;
    }

    const canRegister = () => {
        if (
            email == "" ||
            CPF == "" ||
            name == "" ||
            phone == "" ||
            userType == null ||
            birthDate == null ||
            password == "" ||
            repeatPassword == "" ||
            (userType.value === "paciente" && (initialWeight == "" || height == "")) ||
            (userType.value === "saude" && advice == "")
        ) {
            setErrorName(name == "");
            setErrorCPF(CPF == "");
            setErrorEmail(email == "");
            setErrorPhone(phone == "");
            setErrorWeight(userType?.value === "paciente" && initialWeight == "");
            setErrorHeight(userType?.value === "paciente" && height == "");
            setErrorUserType(userType == null);
            setErrorBirthDate(birthDate == null);
            setErrorPassword(password == "");
            setErrorRepeatPassword(repeatPassword == "");
            setErrorAdvice(userType?.value === "saude" && advice == "");
            showAlert("error", t("messages.fillAll"));
            return false;
        }

        if (name.length < 5) {
            setErrorName(true);
            showAlert("error", t("messages.shortName"));
            return false;
        }

        if (!isValidCpf(CPF)) {
            setErrorCPF(true);
            showAlert("error", t("messages.invalidCpf"));
            return false;
        }

        if (!isValidPhone(phone)) {
            setErrorPhone(true);
            showAlert("error", t("messages.invalidPhone"));
            return false;
        }

        if (userType?.value === "paciente" && !isValidPositiveNumber(initialWeight)) {
            setErrorWeight(true);
            showAlert("error", t("messages.invalidWeight"));
            return false;
        }

        if (userType?.value === "paciente" && !isValidPositiveNumber(height)) {
            setErrorHeight(true);
            showAlert("error", t("messages.invalidHeight"));
            return false;
        }

        if (!isValidEmail(email)) {
            setErrorEmail(true);
            showAlert("error", t("messages.invalidEmail"));
            return false;
        }

        if (!rulesPassword.isValid || !repeatRulesPassword.isValid) {
            setErrorPassword(true);
            setErrorRepeatPassword(true);
            showAlert("error", t("messages.passwordRules"));
            return false;
        }

        if (password !== repeatPassword) {
            setErrorPassword(true);
            setErrorRepeatPassword(true);
            showAlert("error", t("messages.passwordMismatch"));
            return false;
        }

        if (
            (userType?.value === "responsavel" || userType?.value === "saude") &&
            isUnder18(birthDate)
        ) {
            setErrorBirthDate(true);
            showAlert("error", `"${userType?.label}" ${t("messages.adultRequired")}`);
            return false;
        }

        setErrorName(false);
        setErrorCPF(false);
        setErrorEmail(false);
        setErrorPhone(false);
        setErrorWeight(false);
        setErrorHeight(false);
        setErrorUserType(false);
        setErrorBirthDate(false);
        setErrorPassword(false);
        setErrorRepeatPassword(false);
        setErrorAdvice(false);

        return true;
    };

    async function handleRegister() {
        if (loading) return;
        setLoading(true);

        if (!canRegister()) {
            setLoading(false);
            return;
        }

        const data = {
            nome: name,
            email: email,
            telefone: phone,
            senha: password,
            cpf: CPF,
            tipo: userType.value,
            dataNascimento: birthDate,
            ...(userType.value === "paciente" && {
                pesoInicial: Number(initialWeight),
                altura: Number(height),
            }),
            ...(userType.value === "saude" && {
                conselho: advice,
            }),
        };

        try {
            const response = await createUser(data);

            console.log("Usuário criado:", response);
            showAlert("success", t("messages.registerSuccess"));
            navigate("/login");
        } catch (error) {
            console.log("Erro que retorna do backend: ", error.response?.data?.value);
            if (error.response?.data?.value === "duplicateEmail") {
                showAlert("error", t("messages.duplicatedUser"));
                setErrorEmail(true);
                setErrorCPF(true);
                return;
            }
            showAlert("error", t("messages.registerError"));
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        setDateLimit(getDateLimit(userType));
    }, [userType]);

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
                    gap: { xs: 4, md: 7 }
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
                        {t("auth.registerTitle")}
                    </Typography>
                    <Typography sx={{ color: "text.secondary", fontSize: 18, lineHeight: 1.7, maxWidth: 460 }}>
                        {t("auth.registerSubtitle")}
                    </Typography>
                </Box>

                <Paper
                    elevation={0}
                    sx={{
                        width: "100%",
                        maxWidth: 560,
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
                            <PersonAddAltOutlinedIcon />
                        </Box>
                        <Typography sx={{ fontWeight: 800, fontSize: 26 }}>
                            {t("brand")}
                        </Typography>
                        <Typography sx={{ color: "text.secondary", textAlign: "center" }}>
                            {t("auth.registerSubtitle")}
                        </Typography>
                    </Stack>

                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: { xs: "1fr", sm: "repeat(2, minmax(0, 1fr))" },
                            columnGap: 1.5,
                            rowGap: 0.25,
                            alignItems: "start",
                            "& > *": {
                                minWidth: 0
                            }
                        }}
                    >
                        <Box sx={{ gridColumn: "1 / -1", ...fieldSx }}>
                            <InputUI
                                label={t("auth.name")}
                                placeholder={t("auth.placeholders.name")}
                                type="string"
                                error={errorName}
                                value={name}
                                onChange={(e) => {
                                    const onlyLetters = e.target.value.replace(/[^a-zA-ZÀ-ÿ\s]/g, "");
                                    setName(onlyLetters);
                                    setErrorName(false);
                                }}
                            />
                        </Box>

                        <Box sx={fieldSx}>
                            <InputUI
                                label={t("auth.cpf")}
                                placeholder={t("auth.placeholders.cpf")}
                                limit={14}
                                error={errorCPF}
                                value={formatCPF(CPF)}
                                onChange={(e) => {
                                    setCPF(e.target.value.replace(/\D/g, ""));
                                    setErrorCPF(false);
                                }}
                            />
                        </Box>

                        <Box sx={fieldSx}>
                            <InputUI
                                label={t("auth.phone")}
                                placeholder={t("auth.placeholders.phone")}
                                limit={15}
                                error={errorPhone}
                                value={formatPhone(phone)}
                                onChange={(e) => {
                                    setPhone(e.target.value.replace(/\D/g, ""));
                                    setErrorPhone(false);
                                }}
                            />
                        </Box>

                        <Box sx={{ gridColumn: "1 / -1", ...fieldSx }}>
                            <InputUI
                                label={t("auth.email")}
                                placeholder={t("auth.placeholders.email")}
                                type="email"
                                error={errorEmail}
                                value={email}
                                onChange={(e) => (
                                    setEmail(e.target.value),
                                    setErrorEmail(false)
                                )}
                            />
                        </Box>

                        <Box sx={fieldSx}>
                            <AutocompleteUI
                                label={t("auth.userType")}
                                error={errorUserType && userType == null}
                                value={userType}
                                onChange={(newValue) => setUserType(newValue)}
                                options={userTypeOptions}
                                fullWidth
                            />
                        </Box>

                        <Box sx={fieldSx}>
                            <DatePickerUI
                                label={t("auth.birthDate")}
                                dateLimit={dateLimit}
                                error={errorBirthDate && birthDate == null}
                                value={birthDate}
                                onChange={setBirthDate}
                            />
                        </Box>

                        {userType?.value === "paciente" && (
                            <>
                                <Box sx={fieldSx}>
                                    <InputUI
                                        label={t("auth.initialWeight")}
                                        placeholder={t("auth.placeholders.weight")}
                                        error={errorWeight}
                                        value={initialWeight}
                                        onChange={(e) => {
                                            let value = e.target.value.replace(",", ".");
                                            if (value.length > 4) return;

                                            setInitialWeight(value);
                                            setErrorWeight(false);
                                        }}
                                    />
                                </Box>

                                <Box sx={fieldSx}>
                                    <InputUI
                                        label={t("auth.height")}
                                        placeholder={t("auth.placeholders.height")}
                                        error={errorHeight}
                                        value={height}
                                        onChange={(e) => {
                                            let raw = e.target.value.replace(/\D/g, "");
                                            if (raw.length === 3) {
                                                const num = parseFloat(raw[0] + "." + raw.slice(1));
                                                if (num >= 0.5 && num <= 2.7) {
                                                    setHeight(num.toFixed(2));
                                                    setErrorHeight(false);
                                                }
                                            } else {
                                                setHeight(raw);
                                            }
                                        }}
                                    />
                                </Box>
                            </>
                        )}

                        {userType?.value === "saude" && (
                            <Box sx={{ gridColumn: "1 / -1", ...fieldSx }}>
                                <InputUI
                                    label={t("auth.advice")}
                                    placeholder={t("auth.placeholders.advice")}
                                    error={errorAdvice}
                                    value={advice}
                                    onChange={(e) => {
                                        setAdvice(e.target.value);
                                        setErrorAdvice(false);
                                    }}
                                />
                            </Box>
                        )}

                        <Tooltip title={<PasswordTooltip rules={rulesPassword} />} placement="right" arrow>
                            <Box sx={fieldSx}>
                                <InputUI
                                    label={t("auth.password")}
                                    placeholder={t("auth.placeholders.password")}
                                    type="password"
                                    showPasswordToggle={true}
                                    error={errorPassword}
                                    value={password}
                                    onChange={(e) => (
                                        setPassword(e.target.value),
                                        setErrorPassword(false)
                                    )}
                                />
                            </Box>
                        </Tooltip>

                        <Tooltip title={<PasswordTooltip rules={repeatRulesPassword} />} placement="right" arrow>
                            <Box sx={fieldSx}>
                                <InputUI
                                    label={t("auth.repeatPassword")}
                                    placeholder={t("auth.repeatPassword")}
                                    type="password"
                                    showPasswordToggle={true}
                                    error={errorRepeatPassword}
                                    value={repeatPassword}
                                    onChange={(e) => (
                                        setRepeatPassword(e.target.value),
                                        setErrorRepeatPassword(false)
                                    )}
                                />
                            </Box>
                        </Tooltip>
                    </Box>

                    <ButtonUI onClick={handleRegister} disabled={loading} sx={{ width: "100%", mt: 2 }}>
                        {loading ? t("auth.registering") : t("auth.registerAction")}
                    </ButtonUI>

                    <Typography sx={{ mt: 2, textAlign: "center", fontSize: 14, color: "text.secondary" }}>
                        {t("auth.alreadyAccount")}
                        <LinkUI to="/login" variant="action">
                            {t("auth.loginAction")}
                        </LinkUI>
                    </Typography>
                </Paper>
            </Box>
        </Container>
    )
}
