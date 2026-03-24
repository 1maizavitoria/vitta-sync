import { Box, Grid, Paper, Tooltip, Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import InputUI from "../../components/ui/Input";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AlertUI from "../../components/ui/Alert";
import { createUser } from "../../services/userService"


export default function Register() {
    const [error, setError] = useState(false);
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [typeError, setTypeError] = useState("error");

    const navigate = useNavigate();

    // Regras email
    const isValidEmail = (email) => {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    };

    // Regras senha
    const hasMinLength = (password) => password.length >= 8;
    const hasUppercase = (password) => /[A-Z]/.test(password);
    const hasLowercase = (password) => /[a-z]/.test(password);
    const hasNumber = (password) => /\d/.test(password);
    const hasSpecialChar = (password) => /[^A-Za-z\d]/.test(password);

    const rulesPassword = validatePassword(password);
    const repeatRulesPassword = validatePassword(repeatPassword);

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

    const canRegister = () => {

        if (email == "" && password == "" && repeatPassword == "") {
            setError(true);
            setErrorMessage("Preencha todos os campos");
            setTypeError("error");
            return false;
        }

        if (!isValidEmail(email)) {
            setError(true);
            setErrorMessage("Email inválido");
            setTypeError("error");
            return false;
        }

        if (!rulesPassword.isValid || !repeatRulesPassword.isValid) {
            setError(true);
            setErrorMessage("Verifique as regras de senha");
            setTypeError("error");
            return false;
        }

        if (password !== repeatPassword) {
            setError(true);
            setErrorMessage("As senhas devem ser iguais");
            setTypeError("error");
            return false;
        }

        setError(false);
        setErrorMessage("Sucesso");
        setTypeError("success");
        return true;

    };

    // function handleRegister() {
    //     if (canRegister()) {
    //         navigate("/");
    //     }
    // }

    async function handleRegister() {

        if (!canRegister()) return;

        const userData = {
            email,
            senha: password
        };

        try {
            const response = await createUser(userData);

            setError(false);
            setErrorMessage("Cadastro realizado com sucesso");
            setTypeError("success");

            console.log("Usuário criado:", response);

            navigate("/");

        } catch (error) {
            console.error(error);

            setError(true);
            setErrorMessage("Erro ao cadastrar usuário");
            setTypeError("error");
        }
    }

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

    const titleRepeatPasswordTooltip = (
        <Box>
            <Box style={{ color: repeatRulesPassword.minLength ? "lightgreen" : "#ff6b6b" }}>
                • 8 caracteres
            </Box>
            <Box style={{ color: repeatRulesPassword.upperCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra maiúscula
            </Box>
            <Box style={{ color: repeatRulesPassword.lowerCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra minúscula
            </Box>
            <Box style={{ color: repeatRulesPassword.number ? "lightgreen" : "#ff6b6b" }}>
                • Número
            </Box>
            <Box style={{ color: repeatRulesPassword.specialChar ? "lightgreen" : "#ff6b6b" }}>
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
                                Cadastro
                            </Typography>

                            <InputUI
                                placeholder="exemplo@gmail.com"
                                type="email"
                                error={error && (email === "" || !isValidEmail(email))}
                                value={email}
                                onChange={(e) => (
                                    setEmail(e.target.value),
                                    setError(false)
                                )}
                            >
                            </InputUI>

                            <Tooltip title={titlePasswordTooltip} placement="right" arrow>
                                <InputUI
                                    placeholder="Digite sua senha"
                                    type="password"
                                    showPasswordToggle={true}
                                    error={error && (password === "" || !rulesPassword.isValid || password !== repeatPassword)}
                                    value={password}
                                    onChange={(e) => (
                                        setPassword(e.target.value),
                                        setError(false)
                                    )}
                                >
                                </InputUI>
                            </Tooltip>

                            <Tooltip title={titleRepeatPasswordTooltip} placement="right" arrow>
                                <InputUI
                                    placeholder="Repita sua senha"
                                    type="password"
                                    showPasswordToggle={true}
                                    error={error && (repeatPassword === "" || !repeatRulesPassword.isValid || password !== repeatPassword)}
                                    value={repeatPassword}
                                    onChange={(e) => (
                                        setRepeatPassword(e.target.value),
                                        setError(false)
                                    )}
                                >
                                </InputUI>
                            </Tooltip>

                            <ButtonUI
                                onClick={handleRegister}
                            >
                                Cadastrar
                            </ButtonUI>
                        </Box>
                    </Paper>
                </Grid>
            </Grid>
        </Box>
    )
}